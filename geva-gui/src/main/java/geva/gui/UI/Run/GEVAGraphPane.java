/*
Grammatical Evolution in Java
Release: GEVA-v1.2.zip
Copyright (C) 2008 Michael O'Neill, Erik Hemberg, Anthony Brabazon, Conor Gilligan 
Contributors Patrick Middleburgh, Eliott Bartley, Jonathan Hugosson, Jeff Wrigh

Separate licences for asm, bsf, antlr, groovy, jscheme, commons-logging, jsci is included in the lib folder. 
Separate licence for rieps is included in src/com folder.

This licence refers to GEVA-v1.2.

This software is distributed under the terms of the GNU General Public License.


This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
/>.
*/

package geva.gui.UI.Run;

import geva.gui.UI.Run.GEVAConfigurationStreamParser.Event;
import geva.gui.UI.Run.JSci.GEVAGraph;
import geva.gui.UI.Run.JSci.GEVAGraphColours;
import geva.gui.UI.Run.JSci.GEVAGraphModel;
import geva.gui.UI.Run.JSci.GEVAGraphStatistics;
import geva.gui.Util.Constants;
import geva.gui.Util.FileNameExtensionFilter;
import geva.gui.Util.GEVAHelper;
import geva.gui.Util.GEVAUncaught;
import geva.gui.Util.I18N;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

/*
 * This file includes a feature that only work in Java 1.6 and has been
 *  commented out for 1.5 compatibility. This feature places a checkbox on each
 *  tab in the graph pane so that all graphable items under that tab can be
 *  checked/unchecked from a single point. To include this feature, search for
 *  all occurrences of the string /*# and replace with //*#, then recompile
 *  under 1.6
 */

/**
 * Output pane displayed while running GEVA that shows graph information
 * @author eliott bartley
 */
public class GEVAGraphPane
     extends GEVAPane
  implements Runnable,
             MouseMotionListener,
             MouseListener
{

    private static final String[] scaleRatios =
    {   "1000:1", "100:1", "50:1", "10:1", "5:1", "2:1", "1:1",
        "1:2", "1:5", "1:10", "1:50", "1:100", "1:1000"
    };

    private GEVAGraph guiGraph;
    private GEVAGraphModel model;
    private JTabbedPane guiCategories;
    private JLabel guiStatus;
    private LabelListener labelListener = new LabelListener();
    private DataListener dataListener = new DataListener();
    private ConfigListener configListener = new ConfigListener();
    // Holds all GUI related series stuff (appears below the graph and allows
    //  the user to show/hide entries, and view statistics
    private GEVAGraphPaneSerii serii = new GEVAGraphPaneSerii();
    // The data can come in at a very high speed that can noticably be lost
    //  if the graph were to update for every input (this updating also requires
    //  calculating statistics) so graph updates are done in a separate thread.
    //  This thread is only created when the first piece of data comes in, and
    //  remains running in a loop, performing updates, until it is told to stop
    private Thread graphUpdater = null;
    private boolean hoverMode = false; // If the mouse is over the graph, this
                                       //  is true and values are read from the
                                       //  mouse position, else (false) values
                                       //  are read from last received data
                                       //  position
    private int generations = 0; // Total number of generations this run, graph
                                 //  is sized to allow this much data to fit. If
                                 //  run terminates early (match found), this
                                 //  is updated to match the last generation so
                                 //  graph is rescaled to fit data
    private long startTime = -1; // Calculate the ETA from this time, the number
                                 //  of generations in total, the number of
                                 //  generations past, and the current time
    // A load of stuff just for dragging and scaling the graph with the mouse
    private int mouseX = 0; // Remember where the mouse was at the time of a
    private int mouseY = 0; //  drag - updated every frame
    private int mouseU = 0; // Remember where the mouse was when scaling started
    private int mouseV = 0; //  Graph moves to keep this point after scaling
    private int mouseB = MouseEvent.NOBUTTON; // Track what button started drag
    private float graphX = 0; // Store graph offset when dragging
    private float graphY = 0;
    private float graphU = 1; // Store graph scale (each axis can scale
    private float graphV = 1; //  independently of the other)
    private boolean graphF = false; // After scaling, flag that graph needs to
                                    //  correct its position (see mouseU/V)

    public GEVAGraphPane(GEVAPaneManager paneManager)
    {   super(paneManager, I18N.get("ui.run.graf.name"));
        super.setLayout(new BorderLayout());
        super.addMouseMotionListener(this);
        super.addMouseListener(this);

        model = new GEVAGraphModel();
        guiGraph = new GEVAGraph(model);
        guiCategories = new JTabbedPane();
        guiStatus = new JLabel();

        JButton cmdButton;

        super.addTool
        (   cmdButton = new JButton
            (   I18N.get("ui.run.graf.ctrl.saim"),
                new ImageIcon(ClassLoader.getSystemResource(Constants.IMG_SAVE))
            )
        );
        cmdButton.addActionListener(new ActionListener()
        {   public void actionPerformed(ActionEvent e)
            {   cmdSave_onClick();
            }
        });
        super.addTool(new JSeparator(JSeparator.VERTICAL));
        super.addTool(cmdButton = new JButton(I18N.get("ui.run.graf.ctrl.regr")));
        cmdButton.addActionListener(new ActionListener()
        {   public void actionPerformed(ActionEvent e)
            {   graphX = 0;
                graphY = 0;
                graphU = 1;
                graphV = 1;
                redraw();
            }
        });
        for(GEVAGraphPaneCategory category
          : GEVAGraphPaneConfig.getCategories(Constants.GRAPH_CATEGORY_GEVA))
        {

            JPanel guiStatistics = new JPanel(new GridBagLayout());

            gridAddI18NLabel(guiStatistics, "visi",  0);
            gridAddI18NLabel(guiStatistics, "hili",  1);
            gridAddI18NLabel(guiStatistics, "name",  2);
            gridAddI18NLabel(guiStatistics, "valu",  3);
            gridAddI18NLabel(guiStatistics, "mini",  4);
            gridAddI18NLabel(guiStatistics, "maxi",  5);
            gridAddI18NLabel(guiStatistics, "mean",  6);
            gridAddI18NLabel(guiStatistics, "medi",  7);
            gridAddI18NLabel(guiStatistics, "stdd",  8);
            gridAddI18NLabel(guiStatistics, "scal",  9);
            gridAddI18NLabel(guiStatistics, "offs", 10);
            gridAddI18NLabel(guiStatistics, "colr", 11);

            guiCategories.addTab(category.getName(), guiStatistics);
/*#         ifdef(VERSION_1_6_COMPATIBLE)
            guiCategories.setTabComponentAt
            (   guiCategories.getTabCount() - 1,
                new GEVAGraphTabPane(guiCategories)
            );
//#         endif
//*/
            category.setContainer(guiStatistics);

        }

        super.addStatus(guiStatus);

        super.add(guiGraph, BorderLayout.CENTER);
        super.add(guiCategories, BorderLayout.SOUTH);

    }

    /**
     * Helper for calling the gridAdd helper method, used just above
     * @param guiStatistics The statics pane to add the label to
     * @param label The I18N label name (appended to known "ui.run.graf.labl.")
     * @param gridX Grid element to add label to
     */
    private void gridAddI18NLabel(JPanel guiStatistics, String label, int gridX)
    {   GEVAHelper.gridAdd
        (   guiStatistics,
            new JLabel(I18N.get("ui.run.graf.labl." + label)),
            gridX, 0, 0
        );
    }

    private void cmdSave_onClick()
    {   JFileChooser chooser = new JFileChooser();
        BufferedImage image = new BufferedImage
        (   guiGraph.getWidth(),
            guiGraph.getHeight(),
            BufferedImage.TYPE_INT_RGB
        );
        guiGraph.paint(image.getGraphics());
        for(String format : ImageIO.getReaderFormatNames())
        {   FileFilter filter = new FileNameExtensionFilter(format, format);
            chooser.setFileFilter(filter);
        }
        // TODO - this overwrites without warning
        if(chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            String filename = chooser.getSelectedFile().toString();
            if(filename.toLowerCase().endsWith
            (   "." + chooser.getFileFilter()
            ) == false)
                filename += "." + chooser.getFileFilter();

            try
            {   ImageIO.write
                (   image,
                    chooser.getFileFilter().toString(),
                    new File(filename)
                );
            }
            catch(IOException ex) {}
        }
    }

    /**
     * Get the listener for label events
     */
    public LabelListener getLabelListenee()
    {   return labelListener;
    }

    /**
     * Get the listener for data events
     */
    public DataListener getDataListenee()
    {   return dataListener;
    }

    /**
     * Get the listener for configuration events
     */
    public ConfigListener getConfigListenee()
    {   return configListener;
    }

    public class LabelListener
      implements GEVAStandardStreamParser.Listener
                 <GEVAGraphStreamParser.LabelStreamParser.Event>
    {   public void lineParsed
        (   GEVAGraphStreamParser.LabelStreamParser.Event event
        ){  GEVAGraphPane.super.viewMe("data");

            // Go through all labels adding them to the graph
            for(String label : event.getLabels())
            {

                GEVAGraphPaneCategory category;
                GEVAGraphPaneItem item = null;
                GEVAGraphPaneSeries series;

                // Find which category this item was defined under (defined
                //  in graph.config) and from this, get the configuration
                //  details for this item
                category = GEVAGraphPaneConfig.getCategoryWithItem
                (   Constants.GRAPH_CATEGORY_GEVA,
                    label
                );
                if(category != null)
                    item = category.getItem(label);

                // Add to the GUI series (for output) and to the model
                //  series (for input). The GUI series also takes the model
                //  series's index, because the GUI internally deals with
                //  asking the model for all its statistics when it updates.
                //  This class is however, responsible for telling each GUI
                //  series to do that update (so that's not completely
                //  internal)
                series = new GEVAGraphPaneSeries
                (   label,
                    model.addSeries(),
                    category
                );
                serii.add(series);

                // Make this item's details match those specified in
                //  graph.config
                if(item != null)
                {   boolean show = false;
                    boolean bold = false;
                    if(item.getVisible() == GEVAGraphPaneItem.VISIBLE_SHOW)
                        show = true;
                    else
                    if(item.getVisible() == GEVAGraphPaneItem.VISIBLE_BOLD)
                        show = bold = true;
                    model.setSeriesVisible(series.seriesId, show);
                    model.setSeriesSelected(series.seriesId, bold);
                    if(bold == true)
                        model.setSeriesOnTop(series.seriesId);
                    model.setSeriesColour(series.seriesId, item.getColour());
                    // Update the GUI to match the colour of the item
                    series.setColour(model.getSeriesColour(series.seriesId));
                }
                else
                    // If there's no details for a particular item, hide it
                    //  from the graph
                    model.setSeriesVisible(series.seriesId, false);

            }

            // Go through all labels again, setting the error bars. This
            //  couldn't be done in the loop above as, when refering to a
            //  error bar series, it may not have been added yet
            for(String label : event.getLabels())
            {

                GEVAGraphPaneItem item = GEVAGraphPaneConfig.getItem
                (   Constants.GRAPH_CATEGORY_GEVA,
                    label
                );
                if(item != null && item.getErrorName() != null)
                {   model.setSeriesErrorSeries
                    (   serii.get(item.getName()).seriesId,
                        serii.get(item.getErrorName()).seriesId
                    );
                    serii.get(item.getErrorName()).setErrorSeries(true);
                }

            }

            // Match the tabs with the contents
/*#         ifdef(VERSION_1_6_COMPATIBLE)
            syncAllVisible();
//#         endif
//*/
        }
        public void streamParsed() { }
    }

    public class DataListener
      implements GEVAErrorStreamParser.Listener
                 <GEVAGraphStreamParser.DataStreamParser.Event>
    {   public void lineParsed
        (   GEVAGraphStreamParser.DataStreamParser.Event event
        ){

            model.addData(event.getData());
            calcETA((int)model.getSeriesValue(serii.get("Gen").seriesId));
            if(graphUpdater == null)
            {   graphUpdater = new Thread(GEVAUncaught.graph, GEVAGraphPane.this);
                graphUpdater.start();
            }
            // TODO (GRAPH_SYNC_TODO) The graphUpdater only needs to run when
            //  new data has been added. If the fitness function is very slow,
            //  the graphUpdater will spend most of its time in a useless loop.
            //  However, this monitor, which was suppose to put the thread to
            //  sleep until data actually appears, keeps giving me an
            //  IllegalMonitorStateException exception? Don't know why? Usenet
            //  says synchronize it, but that hasn't helped. So right now, the
            //  thread wastes cycles in a tightish loop
            //else
              //  synchronized(graphUpdater) { graphUpdater.notify(); }

        }

        public void streamParsed()
        {
            // Let the graph updater die
            if(graphUpdater != null)
            {   // TODO - see GRAPH_SYNC_TODO (search for it, it explains)
                //synchronized(graphUpdater) { graphUpdater.notify(); }
                // TODO - if above line is uncommented (and worked) the thread
                //  could run and reenter its monitor before the next line had a
                //  chance to execute, which would mean the thread would remain
                //  suspended until the GUI ended. The graphUpdater = null;
                //  should really be before graphUpdater.notify(); so maybe, a
                //  new variable should be chosen for terminating the thread. If
                //  the GRAPH_SYNC_TODO error is fixed, this one will also need
                //  to be fixed
                graphUpdater = null;
                generations = (int)model.getSeriesValue
                (   serii.get("Gen").seriesId
                );
                removeStatus(guiStatus);
                redraw();
            }
        }
    }

    public class ConfigListener
      implements GEVAConfigurationStreamParser.Listener
                 <GEVAConfigurationStreamParser.Event>
    {   public void lineParsed(Event event)
        {   generations = event.getGenerations();
        }
        public void streamParsed() { }
    }

    /**
     * Calculate how long the run is expected to take
     */
    private void calcETA(int generation)
    {   long eta;
        long currentTime = System.currentTimeMillis();
        long deltaTime;
        int hours;
        int minutes;
        int seconds;
        String format;

        if(startTime == -1)
            // Ignore the first generation, and simply use it to set the start
            //  time. This is done because the first generation cannot be
            //  separated from the GEVA start-up time, so would be inaccurate.
            //  For this reason, the generation/generations subtracts 1 before
            //  being used to calculate ETA
            startTime = currentTime;
        else
        {

            try
            {

                // Calculate how long the run is expected to take in millisecs
                deltaTime = currentTime - startTime;
                eta = (long)((double)deltaTime
                    / (generation - 1)
                    * (generations - 1));
                eta -= deltaTime;
                // Add almost 1 second so that when time gets down to 1 second
                //  remaining, the instant it goes to 0 seconds, it should end,
                //  rather than saying there are 0 seconds remaining for the
                //  remainder of the second while the milliseconds are still
                //  counting down
                eta += 999;

                // Calculate as how many seconds, minutes, hours, days ('eta'
                //  will contain days after this step is done)
                eta /= 1000;
                seconds = (int)(eta % 60); eta /= 60;
                minutes = (int)(eta % 60); eta /= 60;
                hours   = (int)(eta % 24); eta /= 24;

                // Only show days/hours/minutes parts if it is expected to take
                //  in these regions of time
                if(eta > 0)
                    format = "({0,number,0}) " +
                        "{1,number,00}:{2,number,00}:{3,number,00}";
                else
                if(hours > 0)
                    format = "{1,number,0}:{2,number,00}:{3,number,00}";
                else
                if(minutes > 0)
                    format = "{2,number,0}:{3,number,00}";
                else
                    format = "{3,number,0}";

                // Output the estimated time to completion
                guiStatus.setText(I18N.get
                (   "ui.run.graf.stat.time",
                    format,
                    eta,
                    hours,
                    minutes,
                    seconds
                ));

            }
            catch(ArithmeticException e)
            {   // If there's an error calculating the ETA, ignore it. Probably
                //  a divide-by-zero or sometime equally uninteresting. It
                //  should correct itself when the numbers better suit it. But
                //  for now, the setText shouldn't have happened, so the ETA
                //  that was specified before the error, will remain
            }

        }

    }

    /**
     * Perform graph updates asynchronously while the data is being added, as
     *  graph renders can be significantly slower than data adds. They can also
     *  be significantly faster, and for that, see GRAPH_SYNC_TODO (search for
     *  it)
     */
    public void run()
    {   long time;
        long renderTime = 0;
        long statsTime = 0;

        // While this class knows about this thread, it keeps running. But the
        //  second this class looks away, this thread will kill itself
        //  (graphUpdater, which refers to this thread, is also what determines
        //  when it should end)
        while(graphUpdater != null)
            try
            {

                // Only calculate stats after waiting 10 times longer than it
                //  took to last calculate the stats
                if(statsTime <= 0)
                {   time = System.currentTimeMillis();
                    for(GEVAGraphPaneSeries series : serii)
                        series.updateStatistics();
                    statsTime = (System.currentTimeMillis() - time) * 10;
                }

                // Only render after waiting 5 times longer than it took to last
                //  render (render times should be significantly faster than
                //  stats calculation times)
                if(renderTime <= 0)
                {   time = System.currentTimeMillis();
                    redraw();
                    renderTime = (System.currentTimeMillis() - time) * 5;
                    if(hoverMode == false)
                        value();
                }

                // Don't completely hog the processor
                Thread.sleep(100);
                statsTime -= 100;
                renderTime -= 100;
                // TODO - see GRAPH_SYNC_TODO (search for it, it explains all)
                //synchronized(graphUpdater) { wait(); }

            }
            catch(InterruptedException e)
            { // don't care about interrupts, it will just try again in the loop
            }

        // Do one last update before leaving, as the end request may have
        //  happened in the middle of a previous, now out-of-date, update
        for(GEVAGraphPaneSeries series : serii)
            series.updateStatistics();
        redraw();

    }

    // When the mouse isn't being dragged, record mouse and scale points for
    //  reference when drag occurs
    public void mouseMoved(MouseEvent e)
    {
        hover(e.getX());
        mouseX = mouseU = e.getX();
        mouseY = mouseV = e.getY();
    }

    // When dragging..
    public void mouseDragged(MouseEvent e)
    {
        hover(e.getX());
        switch(mouseB)
        {   // Move with the left mouse button
            case MouseEvent.BUTTON1:
                // Update the graph x, y based on the difference in mouse
                //  position in its current position against its last position
                graphX -= guiGraph.getDataIndexBetween(e.getX(), mouseX)
                        / graphU;
                graphY -= guiGraph.getDataValueBetween(e.getY(), mouseY)
                        / graphV;
                redraw();
                break;
            // Scale with the right mouse button
            case MouseEvent.BUTTON3:
                // Update the graph x, y scale factors based on the difference
                //  in mouse position in its current position against its last
                //  position
                graphU *= -(float)(e.getX() - mouseX) / 100 + 1;
                graphV *=  (float)(e.getY() - mouseY) / 100 + 1;
                // Make sure the scaling cannot go so low as to cause the graph
                //  to except (e.g. minimum cannot be greater than maximum when
                //  setting extrema) TODO - this should be updated to alter the
                //  minimum/maximum explicitly because, as the graph gets larger,
                //  the minimum of 0.001 stops being useful
                if(graphU < 0.001) graphU = 0.001f;
                if(graphV < 0.001) graphV = 0.001f;
                // Flag that the position the mouse scaled from will have moved
                //  and needs to be repositioned. i.e. The point over which
                //  the mouse was hovering on the graph will be scaled and this
                //  will cause it to move away from where it was. Setting this
                //  flag causes the redraw to get what data point was hovered
                //  over and finding how much that point has moved by after
                //  scaling, and then moving the graph back by that much keeping
                //  the point scaled from centered
                graphF = true;
                redraw();
                break;
        }
        mouseX = e.getX();
        mouseY = e.getY();
    }

    public void mouseClicked(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { hoverMode = true; }
    public void mouseExited(MouseEvent e) { hoverMode = false; }
    public void mousePressed(MouseEvent e) { mouseB = e.getButton(); }
    public void mouseReleased(MouseEvent e) { mouseB = MouseEvent.NOBUTTON; }

    protected void hover(int x)
    {
        try
        {   for(GEVAGraphPaneSeries series : serii)
                series.updateValue
                (   guiGraph.calcSeriesValueAt(series.seriesId, x)
                );
        }
        catch(ConcurrentModificationException e)
        {   // Don't care about concurrent problems here because it will only
            //  occur when the display is being updated, which means the user
            //  won't have time to even notice that the error occurred before
            //  new data is being displayed (and corrected). It's a flash error
        }
    }

    protected void value()
    {

        for(GEVAGraphPaneSeries series : serii)
            series.updateValue
            (   model.getSeriesValue(series.seriesId)
            );

    }

    /**
     * Set the minimum/maximum bounds of the graph. The graph will always render
     *  to the width/height of the window, so this will cause it to scale to fit
     */
    public void rebound()
    {

        // Scale the graph to fit the data
        guiGraph.setXExtremaSafe
        (   (model.getXExtremaMin() + graphX) * graphU,
            (generations + graphX) * graphU
        );
        guiGraph.setYExtremaSafe
        (   (model.calcYExtremaMin() + graphY) * graphV,
            (model.calcYExtremaMax() + graphY) * graphV
        );

    }

    /**
     * Get the graph to redraw itself
     */
    public void redraw()
    {   float fixX = 0;
        float fixY = 0;

        // if redraw occurred because of zoom, record the data point under the
        //  mouse zoom center
        if(graphF == true)
        {   fixX = guiGraph.getDataIndexAt(mouseU);
            fixY = guiGraph.getDataValueAt(mouseV);
        }

        // Move/scale the graph
        rebound();

        // if redraw occurred because of zoom, test the point under the mouse
        //  zoom center against the point recorded previously. Then shift the
        //  graph by this amount so the center point goes back where it came
        //  from
        if(graphF == true)
        {   graphX -= (guiGraph.getDataIndexAt(mouseU) - fixX) / graphU;
            graphY -= (guiGraph.getDataValueAt(mouseV) - fixY) / graphV;
            rebound();
            graphF = false;
        }

        // Show time!
        guiGraph.redraw();

    }

    /**
     * When a tab's visible check-box is clicked, this goes through all the
     *  check-boxes under the tab, and sets them to the same check value
     * @param tab
     * @param visible
     */
/*# ifdef(VERSION_1_6_COMPATIBLE)
    private void setAllVisibleUnder(GEVAGraphTabPane tab, boolean visible)
    {   int index = guiCategories.indexOfTabComponent(tab);
        if(index != -1)
            if(guiCategories.getComponentAt(index) instanceof JPanel)
            {   JPanel panel = (JPanel)guiCategories.getComponentAt(index);
                for(Component component : panel.getComponents())
                    if(component instanceof JCheckBox)
                        ((JCheckBox)component).setSelected(visible);
            }
    }
//# endif
//*/

    /**
     * This goes throug all tabs and all check-boxes under those tabs, and if
     *  any one of those check-boxes is ticked, the tab's check-box becomes
     *  ticked, otherwise, the tab's check-box becomes unticked
     */
/*# ifdef(VERSION_1_6_COMPATIBLE)
    private void syncAllVisible()
    {   boolean visible;

        for(int index = 0; index < guiCategories.getTabCount(); index++)
        {
            visible = false;
            if(guiCategories.getComponentAt(index) instanceof JPanel)
            {   JPanel panel = (JPanel)guiCategories.getComponentAt(index);
                for(Component component : panel.getComponents())
                    if(component instanceof JCheckBox)
                        if(((JCheckBox)component).isSelected() == true)
                        {   visible = true;
                            break;
                        }
                if(guiCategories.getTabComponentAt(index)
                    instanceof GEVAGraphTabPane
                )   ((GEVAGraphTabPane)guiCategories
                        .getTabComponentAt(index))
                        .setVisibleCheckbox(visible);
            }

        }
    }
//# endif
//*/

    ////////////////////////////////////////////////////////////////////////////
    // Series GUI specifics
    //

    /**
     * Custom tab that shows a checkbox for making all series under that tab
     *  visible or hidden
     */
    class GEVAGraphTabPane extends JPanel
    {

        private JTabbedPane pane;
        private JCheckBox chkVisible;

        public GEVAGraphTabPane(JTabbedPane pane)
        {   super(new FlowLayout(FlowLayout.LEFT, 0, 0));
            assert pane != null : "Where's the pane at";
            super.setOpaque(false);

            this.pane = pane;

            chkVisible = new JCheckBox();
/*#         ifdef(VERSION_1_6_COMPATIBLE)
            chkVisible.addActionListener(new ActionListener()
            {   public void actionPerformed(ActionEvent e)
                {   cboVisible_onClick();
                }
            });
//#         endif
//*/
            chkVisible.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

            JLabel lblTitle = new JLabel()
/*#         ifdef(VERSION_1_6_COMPATIBLE)
            {   @Override
                public String getText()
                {   return GEVAGraphTabPane.this.lblTitle_getText();
                }
            }
//#         endif
//*/
            ;
            lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

            super.add(chkVisible);
            super.add(lblTitle);

        }

        // When the item is checked, go through all components that are under
        //  the tab this relates to, and set all checkboxes found within to the
        //  same check value
/*#     ifdef(VERSION_1_6_COMPATIBLE)
        private void cboVisible_onClick()
        {   setAllVisibleUnder(this, chkVisible.isSelected());
            // Select the tab as an additional response to ticking the check-box
            if(chkVisible.isSelected() == true)
            {   int index = guiCategories.indexOfTabComponent(this);
                if(index != -1)
                    guiCategories.setSelectedIndex(index);
            }
        }

        public void setVisibleCheckbox(boolean visible)
        {   chkVisible.setSelected(visible);
        }

        private String lblTitle_getText()
        {   int index = pane.indexOfTabComponent(this);
            if(index != -1)
                return pane.getTitleAt(index);
            return null;
        }
//#     endif
//*/

    }

    /**
     * Helper for an ArrayList of series that includes a get by label name
     */
    static class GEVAGraphPaneSerii extends ArrayList<GEVAGraphPaneSeries>
    {

        public GEVAGraphPaneSeries get(String label)
        {

            for(GEVAGraphPaneSeries series : this)
                if(series.name.equals(label) == true)
                    return series;

            return null;

        }

    }

    /**
     * Split out the series GUI specifics from the rest of the GUI
     */
    class GEVAGraphPaneSeries
    {

        private int seriesId;
        private String name;
        private JCheckBox chkVisible;
        private JRadioButton optSelect;
        private JLabel lblName;
        private JLabel lblValue;
        private JLabel lblMinimum;
        private JLabel lblMaximum;
        private JLabel lblMean;
        private JLabel lblMedian;
        private JLabel lblStdDev;
        private JComboBox cboScale;
        private JSpinner numOffset;
        private GEVAColourChooser cboColour;

        /**
         * Set up the UI for each series
         */
        public GEVAGraphPaneSeries
        (   String name,
            int seriesId,
            GEVAGraphPaneCategory category
        ){  this.seriesId = seriesId;
            this.name = name;
            chkVisible = new JCheckBox();
            optSelect = new JRadioButton();
            lblName = new JLabel(name);
            lblValue = new JLabel();
            lblMinimum = new JLabel();
            lblMaximum = new JLabel();
            lblMean = new JLabel();
            lblMedian = new JLabel();
            lblStdDev = new JLabel();
            cboScale = new JComboBox(scaleRatios);
            numOffset = new JSpinner
            (   new SpinnerNumberModel
                (   0.0f,
                    -Float.MAX_VALUE,
                    Float.MAX_VALUE,
                    1.0f
                )
            );
            cboColour = new GEVAColourChooser();

            if(category != null && category.getItem(name) != null)
                lblName.setText(category.getItem(name).getTitle());

            chkVisible.setSelected(true);
            chkVisible.addChangeListener(new ChangeListener()
            {   public void stateChanged(ChangeEvent e)
                {   chkVisible_onClick();
                }
            });

            optSelect.addActionListener(new ActionListener()
            {   public void actionPerformed(ActionEvent e)
                {   optSelect_onClick();
            }   });

            cboScale.setSelectedItem("1:1");
            cboScale.setEditable(true);
            cboScale.setMinimumSize(new Dimension(50, 0));
            cboScale.setPreferredSize(new Dimension(70, 0));
            cboScale.addActionListener(new ActionListener()
            {   public void actionPerformed(ActionEvent e)
                {   cboScale_onChange();
            }   });

            numOffset.setMinimumSize(new Dimension(50, 0));
            numOffset.setPreferredSize(new Dimension(70, 0));
            numOffset.addChangeListener(new ChangeListener()
            {   public void stateChanged(ChangeEvent e)
                {   numOffset_onChange();
                }
            });

            for(int colourIndex : GEVAGraphColours.colourIndexes)
                cboColour.addItem(new Color(colourIndex));
            cboColour.setMinimumSize(new Dimension(30, 0));
            cboColour.setPreferredSize(new Dimension(100, 0));
            cboColour.setSelectedItem(model.getSeriesColour(seriesId));
            cboColour.addActionListener(new ActionListener()
            {   public void actionPerformed(ActionEvent e)
                {   cboColour_onClick();
            }   });

            if(category != null)
            {   GEVAGraphPaneItem item = category.getItem(name);
                boolean show = false;
                boolean bold = false;
                if(item != null)
                    if(item.getVisible() == GEVAGraphPaneItem.VISIBLE_SHOW)
                        show = true;
                    else
                    if(item.getVisible() == GEVAGraphPaneItem.VISIBLE_BOLD)
                        show = bold = true;
                chkVisible.setSelected(show);
                this.optSelect.setSelected(bold);

            }

            // Don't add the GUI element to the window if it's not been
            //  configured, but keep all the GUI stuff and classes, as the data
            //  for them will still be coming in, and will be for me to just
            //  hide the inputs, than get rid of them, and have to start parsing
            //  the data input and linking it up to visible containers
            if(category != null && category.getContainer() != null)
            {

                Container container = category.getContainer();

                GEVAHelper.gridAdd(container, chkVisible, 0, seriesId + 1, 0);
                GEVAHelper.gridAdd(container, optSelect,  1, seriesId + 1, 0);
                GEVAHelper.gridAdd(container, lblName,    2, seriesId + 1, 1);
                GEVAHelper.gridAdd(container, lblValue,   3, seriesId + 1, 1);
                GEVAHelper.gridAdd(container, lblMinimum, 4, seriesId + 1, 1);
                GEVAHelper.gridAdd(container, lblMaximum, 5, seriesId + 1, 1);
                GEVAHelper.gridAdd(container, lblMean,    6, seriesId + 1, 1);
                GEVAHelper.gridAdd(container, lblMedian,  7, seriesId + 1, 1);
                GEVAHelper.gridAdd(container, lblStdDev,  8, seriesId + 1, 1);
                GEVAHelper.gridAdd(container, cboScale,   9, seriesId + 1, 1);
                GEVAHelper.gridAdd(container, numOffset, 10, seriesId + 1, 1);
                GEVAHelper.gridAdd(container, cboColour, 11, seriesId + 1, 1);

            }
            else
                // Not an important error, dump it to the console, if any
                System.out.println("No configuration for [" + name + "]");

        }

        /**
         * Updates the GUI to remove things that don't make sense if this is an
         *  errors series. Removes highlighting, scaling, and offset, as all of
         *  these things are inherited from the series that this is an error
         *  series to, and have no real purpose to act on the error series alone
         * @param errorSeries
         */
        public void setErrorSeries(boolean errorSeries)
        {   optSelect.setVisible(!errorSeries);
            numOffset.setVisible(!errorSeries);
        }

        /**
         * When the user clicks the visible check box..
         */
        private void chkVisible_onClick()
        {
/*#         ifdef(VERSION_1_6_COMPATIBLE)
            syncAllVisible();
//#         endif
//*/
            model.setSeriesVisible(seriesId, chkVisible.isSelected());
            redraw();
        }

        /**
         * When the user clicks the select highlight..
         */
        private void optSelect_onClick()
        {   for(GEVAGraphPaneSeries series : serii)
                if(series != this)
                {   series.optSelect.setSelected(false);
                    model.setSeriesSelected(series.seriesId, false);
                }
            model.setSeriesSelected(seriesId, optSelect.isSelected());
            if(optSelect.isSelected() == true)
                model.setSeriesOnTop(seriesId);
            else
                model.setSeriesOnTop(-1);
            redraw();
        }

        private void cboScale_onChange()
        {   String[] ratio;
            float scale;
            try
            {   ratio = cboScale.getSelectedItem().toString().split("[:/]");
                scale = Float.parseFloat(ratio[0]);
                if(ratio.length > 1)
                    scale /= Float.parseFloat(ratio[1]);
            }
            catch(NumberFormatException e) { scale = 1; }
            model.setSeriesScale(seriesId, scale);
            redraw();
        }

        private void numOffset_onChange()
        {   model.setSeriesOffset
            (   seriesId,
                new Float((Double)numOffset.getValue())
            );
            redraw();
        }

        /**
         * When the user selects a colour
         */
        private void cboColour_onClick()
        {   assert cboColour.getSelectedItem() instanceof Color
                 : cboColour.getSelectedItem().getClass().getName();
            model.setSeriesColour(seriesId, (Color)cboColour.getSelectedItem());
            redraw();
        }

        public void setColour(Color colour)
        {   cboColour.setSelectedItem(colour);
        }

        /**
         * Update the mouse-hover statistic value. That is, when the mouse moves
         *  over the graph, the value of the series under the vertical position
         *  of the mouse should be output
         * @param value
         */
        public void updateValue(float value)
        {   updateStatistic(lblValue, value);
        }

        /**
         * Update all the GUI fields with the statistics data taken from the
         *  graph
         * @param value
         */
        public void updateStatistics()
        {   GEVAGraphStatistics statistics = model.getSeriesStatistics(seriesId);
            updateStatistic(lblMinimum, statistics.getExtremaMin());
            updateStatistic(lblMaximum, statistics.getExtremaMax());
            updateStatistic(lblMean, statistics.calcMean());
            updateStatistic(lblMedian, statistics.calcMedian());
            updateStatistic(lblStdDev, statistics.calcStandardDeviation());
        }

        /**
         * Helper for updating statictic labels with the lastest values
         */
        private void updateStatistic(JLabel label, float value)
        {   label.setText(String.format(Constants.GRAPH_FORMAT, value));
        }

        /**
         * ComboBox that renders the colour as an image beside the colour name
         */
        public class GEVAColourChooser extends JComboBox
        {

            public GEVAColourChooser()
            {   initialise();
            }

            public GEVAColourChooser(Vector<?> items)
            {   super(items);
                initialise();
            }

            public GEVAColourChooser(Object[] items)
            {   super(items);
                initialise();
            }

            public GEVAColourChooser(ComboBoxModel aModel)
            {   super(aModel);
                initialise();
            }

            public void initialise()
            {   setRenderer(new GEVAColourItem());
            }

            class GEVAColourItem extends JPanel implements ListCellRenderer
            {

                private JPanel guiColour;
                private JLabel txtColour;

                public GEVAColourItem()
                {   super.setOpaque(true);
                    super.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));
                    guiColour = new JPanel();
                    guiColour.setPreferredSize
                    (   new Dimension
                        (   super.getFont().getSize(),
                            super.getFont().getSize()
                    )   );
                    txtColour = new JLabel();
                    GEVAHelper.gridAdd(this, guiColour, 0, 0, 1);
                    GEVAHelper.gridAdd(this, txtColour, 1, 0, 1);
                }

                public Component getListCellRendererComponent
                (   JList           list,
                    Object         value,
                    int            index,
                    boolean   isSelected,
                    boolean cellHasFocus
                ){  Color         colour;

                    if(isSelected == true)
                    {   setBackground(list.getSelectionBackground());
                        setForeground(list.getSelectionForeground());
                    }
                    else
                    {   setBackground(list.getBackground());
                        setForeground(list.getForeground());
                    }

                    if(value instanceof Color)
                    {   colour = (Color)value;
                        guiColour.setBackground(colour);
                        txtColour.setText(GEVAGraphColours.getColourName(colour));
                    }

                    return this;

                }

            }

        }

    }

}
