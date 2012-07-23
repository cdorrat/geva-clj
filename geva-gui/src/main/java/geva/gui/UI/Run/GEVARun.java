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

import geva.gui.UI.GEVAConfig;
import geva.gui.UI.GEVAFitness;
import geva.gui.UI.GEVAMessage;
import geva.gui.UI.Run.GEVAGlobalStreamParser.Event;
import geva.gui.Util.Constants;
import geva.gui.Util.GEVAUncaught;
import geva.gui.Util.I18N;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Run the GEVA process in a new window. Closing this window while GEVA is still
 *  running will kill the GEVA process.
 * @author eliottbartley
 */
public class GEVARun
     extends JDialog
  implements WindowListener,
             GEVAPaneManager,
             GEVAGlobalStreamParser.Listener<GEVAGlobalStreamParser.Event>
{

    private static class Panes
                 extends HashMap<GEVAPane, GEVAPaneDetails> { }

    private Process gevaProcess;
    private GEVAStreamReader gevaInputReader;
    private GEVAStreamReader gevaErrorReader;
    private JPanel guiToolBar;
    private JButton cmdStop;
    private JButton cmdClose;
    private JTabbedPane pages;
    private GEVAGlobalStreamParser globalParser;
    private GEVAConfigurationStreamParser configParser;
    private GEVAStandardStreamParser standardParser;
    private GEVAErrorStreamParser errorParser;
    private GEVAGraphStreamParser.LabelStreamParser labelParser;
    private GEVAGraphStreamParser.DataStreamParser dataParser;
    private GEVALSystemStreamParser lsystemParser;
    private Panes panes = new Panes();
    private JPanel guiStatusBar;

    public GEVARun(Dialog owner, String propertiesFilePath, GEVAFitness fitness)
    {   super(owner);
        init(propertiesFilePath, fitness);
    }

    public GEVARun(Frame owner, String propertiesFilePath, GEVAFitness fitness)
    {   super(owner);
        init(propertiesFilePath, fitness);
    }

    private void init(String propertiesFilePath, GEVAFitness fitness)
    {   initialiseUI();
        run(propertiesFilePath, fitness);
    }

    private void initialiseUI()
    {   GEVAGraphStreamParser graphParser;
        GEVAOutputPane guiOutput;
        GEVAGraphPane guiGraph;
        GEVALSystemPane guiLSystem;
        pages = new JTabbedPane();

        guiToolBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
        guiStatusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));

        guiToolBar.add(cmdStop = new JButton(I18N.get("ui.run.ctrl.stop")));
        guiToolBar.add(cmdClose = new JButton(I18N.get("ui.run.ctrl.exit")));
        guiToolBar.add(new JSeparator(JSeparator.VERTICAL));

        //[BEGIN_PANE_INIT[
        guiOutput = new GEVAOutputPane(this);
        guiGraph = new GEVAGraphPane(this);
        guiLSystem = new GEVALSystemPane(this);
        //]END_PANE_INIT]

        GEVAStreamParser.begin();
        //[BEGIN_PARSER_INIT[
        globalParser = new GEVAGlobalStreamParser();
        configParser = new GEVAConfigurationStreamParser();
        standardParser = new GEVAStandardStreamParser();
        errorParser = new GEVAErrorStreamParser();
        graphParser = new GEVAGraphStreamParser();
        labelParser = graphParser.new LabelStreamParser();
        dataParser = graphParser.new DataStreamParser();
        lsystemParser = new GEVALSystemStreamParser();
        //]END_PARSER_INIT]
        GEVAStreamParser.end();

        //[BEGIN_PANE_LISTENER[
        globalParser.addParserListener(this);
        configParser.addParserListener(guiGraph.getConfigListenee());
        standardParser.addParserListener(guiOutput.getStandardListenee());
        errorParser.addParserListener(guiOutput.getErrorListenee());
        labelParser.addParserListener(guiGraph.getLabelListenee());
        dataParser.addParserListener(guiGraph.getDataListenee());
        lsystemParser.addParserListener(guiLSystem);
        //]END_PANE_LISTENER]

        pages.addChangeListener(new ChangeListener()
        {   public void stateChanged(ChangeEvent e)
            {   pages_onChange();
            }
        });
        cmdStop.addActionListener(new ActionListener()
        {   public void actionPerformed(ActionEvent e)
            {   cmdStop_onClick();
            }
        });
        cmdClose.addActionListener(new ActionListener()
        {   public void actionPerformed(ActionEvent e)
            {   cmdClose_onClick();
            }
        });
        cmdClose.setVisible(false);

        super.addWindowListener(this);
        super.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        super.getContentPane().setLayout(new BorderLayout());
        super.getContentPane().add(guiToolBar, BorderLayout.NORTH);
        super.getContentPane().add(pages, BorderLayout.CENTER);
        super.getContentPane().add(guiStatusBar, BorderLayout.SOUTH);
        super.setSize(640, 480);
        super.setVisible(true);

    }

    private void run(String propertiesFilePath, GEVAFitness fitness)
    {   String command;
        String classPath = fitness.getJar();

        // If the classPath is null at this point, it means that getJar has
        //  not yet compiled a list of jar files from the search path and the
        //  user has specified that they don't want to wait for the search to
        //  complete, so display a cancelled message by faking a stream write
        //  directly to the output pane and don't start GEVA
        if(classPath == null)
        {   errorParser.injectLine(I18N.get("ui.run.stop"));
            return;
        }

        // Build up the command line that will be used to execute GEVA
        //  Set the name of the java runtime
        command = GEVAConfig.getJavaName();
        //  Set any arbitrary additional command arguments specified by the user
        command += fitness.getCmd();
        //  Set all the jar files for libraries, and class path for main
        if(classPath != null && classPath.length() != 0)
            command += " -cp " + classPath;
        // Set the max memory allocation for GEVA
        if(GEVAConfig.getHeapSize() != null) {
            command += " -Xmx" + GEVAConfig.getHeapSize();
        }
        if(GEVAConfig.isServer()) {
            command += " -" + Constants.cfgServer;
        }

        command += " " + GEVAConfig.getClassName();
        //  Set the properties file that is to be loaded for this run. If the
        //  properties has been modified and not saved before the run, this
        //  would mean that the run will use the old property values, except!
        //  this is not the case. All properties are saved before the run, just
        //  that they are saved to a temporary file, thus ensuring each run will
        //  always use the properties as they are specified in the GUI
        command += " -properties_file " + propertiesFilePath;

        try
        {

            // Run GEVA as a separate process
            if(GEVAConfig.getWorkingAbsPath() == null
            || GEVAConfig.getWorkingAbsPath().length() == 0)
                gevaProcess = Runtime.getRuntime().exec(command);
            else
                gevaProcess = Runtime.getRuntime().exec
                (   command,
                    null,
                    new File(GEVAConfig.getWorkingAbsPath())
                );

            // Fake another stream write directly to the output pane giving
            //  information about the command and working directory used for
            //  this run
            standardParser.injectLine(I18N.get("ui.run.rcmd") + " " + command);
            standardParser.injectLine
            (   I18N.get("ui.run.wkpa") + " "
              + new File(GEVAConfig.getWorkingAbsPath())
            );

            // Start reading the standard output and error streams of the GEVA
            //  process
            gevaInputReader = new GEVAStreamReader
            (   GEVAUncaught.output,
                gevaProcess.getInputStream(),
                GEVAStreamReader.ID_STD_OUT

            );
            gevaErrorReader = new GEVAStreamReader
            (   GEVAUncaught.error,
                gevaProcess.getErrorStream(),
                GEVAStreamReader.ID_ERR_OUT

            );

            // All is set up, GEVA should be running, so let it be known..
            //  Note: see streamListener below for when processing is known to
            //  be finished, and the done message is displayed
            this.setTitle(I18N.get("ui.run.begn"));

            // Setup all GUI information displays to listen to the appropriate
            //  streams required to parse the information what they needs
            //  Note. order here is important - global must be first,
            //  configuration must be second, graph must be data followed by
            //  label, and graph must be before standard
            //[BEGIN_STREAM_LISTENER[
            gevaInputReader.addStreamListener(globalParser);
            gevaInputReader.addStreamListener(configParser);
            gevaInputReader.addStreamListener(lsystemParser);
            gevaInputReader.addStreamListener(dataParser);
            gevaInputReader.addStreamListener(labelParser);
            gevaInputReader.addStreamListener(standardParser);
            gevaErrorReader.addStreamListener(errorParser);
            //]END_STREAM_LISTENER]

        }
        catch(IOException e)
        {   if(gevaProcess != null)
            {   gevaProcess.destroy();
                gevaProcess = null;
            }
            GEVAMessage.showMessage
            (   this,
                I18N.get("ui.run.err", e),
                GEVAMessage.ERROR_TYPE
            );
            e.printStackTrace();
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    // GEVAPaneManager stuff
    //

    /**
     * The calling pane is telling that it exists. Allocate memory for it
     */
    public void addPane(GEVAPane pane, String title)
    {   GEVAPaneDetails paneDetails = panes.get(pane);
        if(paneDetails == null)
            panes.put(pane, paneDetails = new GEVAPaneDetails(title));
    }

    /**
     * The calling pane wants to force itself to be made visible. Currently
     *  (2008y08M21d) only the output window calls this when an error occurs
     */
    public void viewPane(GEVAPane pane)
    {   viewPane(pane, null);
    }

    /**
     * The calling pane wants itself to be made visible, but this will only
     *  carry out the request the first time it's called by the pane for a given
     *  reason
     */
    public void viewPane(GEVAPane pane, String reason)
    {   GEVAPaneDetails paneDetails = panes.get(pane);
        if(paneDetails == null)
            throw new IllegalStateException(I18N.get("ui.err.cons.abev"));
        // First, add a tab for this run pane, if one isn't already in place
        if(paneDetails.getIndex() == -1)
        {   pages.addTab(paneDetails.getTitle(), pane);
            paneDetails.setIndex(pages.getTabCount() - 1);
        }
        // Check if the reason being viewed hasn't already been done, don't do
        //  the view if view already do
        if(reason != null)
            if(paneDetails.isViewed(reason) == true)
                return;
            else
                paneDetails.setViewed(reason);
        // Note: there's a return statement above if viewing shouldn't occur, so
        //  this line may never be reached. Note too: that this code will always
        //  be reached if reason is null. (This code does the actual viewing
        //  that this function is all about)
        pages.setSelectedIndex(paneDetails.getIndex());
    }

    /**
     * Test if a particular pane is the currently visible pane. Currently
     *  (2008y08M21d) unused
     */
    public boolean isViewingPane(GEVAPane pane)
    {   GEVAPaneDetails paneDetails = panes.get(pane);
        if(paneDetails == null)
            return false;
        return pages.getSelectedIndex() == paneDetails.getIndex();
    }

    /**
     * The calling pane is adding a component that it wants added to the toolbar
     *  when it is visible
     */
    public void addPaneTool(GEVAPane pane, Component component)
    {   GEVAPaneDetails paneDetails = panes.get(pane);
        if(paneDetails == null)
            throw new IllegalStateException(I18N.get("ui.err.cons.abea"));
        paneDetails.addTool(component);
        if(isViewingPane(pane) == true)
            guiToolBar.add(component);
    }

    /**
     * The calling pane is removing a component that it no longer wants added to
     *  the toolbar when it is visible
     */
    public void removePaneTool(GEVAPane pane, Component component)
    {   GEVAPaneDetails paneDetails = panes.get(pane);
        if(paneDetails == null)
            throw new IllegalStateException(I18N.get("ui.err.cons.aber"));
        paneDetails.removeTool(component);
        if(isViewingPane(pane) == true)
            guiToolBar.remove(component);
    }

    public void addPaneStatus(GEVAPane pane, Component component)
    {   GEVAPaneDetails paneDetails = panes.get(pane);
        if(paneDetails == null)
            throw new IllegalStateException(I18N.get("ui.err.cons.abea"));
        paneDetails.addStatus(component);
    }

    public void removePaneStatus(GEVAPane pane, Component component)
    {   GEVAPaneDetails paneDetails = panes.get(pane);
        if(paneDetails == null)
            throw new IllegalStateException(I18N.get("ui.err.cons.aber"));
        paneDetails.removeStatus(component);
    }

    ////////////////////////////////////////////////////////////////////////////
    // event stuff
    //

    /**
     * Go through all run panes, remove tools for invisible ones, and adding
     *  tools for visible ones
     */
    void pages_onChange()
    {   for(GEVAPaneDetails pane : panes.values())
            for(GEVAPaneDetails.GEVAPaneToolDetails tool
              : pane.getTools())
                if(pane.getIndex() == pages.getSelectedIndex())
                    guiToolBar.add(tool.component);
                else
                   guiToolBar.remove(tool.component);
        guiToolBar.paintImmediately(guiToolBar.getBounds());
    }

    void cmdStop_onClick()
    {   gevaProcess.destroy();
        gevaProcess = null;
    }

    void cmdClose_onClick()
    {   close();
    }

    /**
     * Close this window, making sure to clean up before doing so. This may mean
     *  the window is not actually closed; if GEVA was still running and the
     *  user requested that it not be stopped, the window will remain open
     */
    public void close()
    {   windowClosing(null);
    }

    public void lineParsed(Event event) { }
    /**
     * Listen to the stream for when GEVA ends and display a message to the user
     */
    public void streamParsed()
    {   this.setTitle(I18N.get("ui.run.fins"));
        cmdStop.setVisible(false);
        cmdClose.setVisible(true);
    }

    public void windowClosing(WindowEvent event)
    {
        // If either stream reader is still reading, the process is still alive.
        //  Kill this process before closing the window, as leaving it running,
        //  particularly if the process hasn't ended due to loopy (of the
        //  infinite category) code, will cause the process to continue
        //  invisibly (Most GEVA setups don't have UIs or ways to terminate)
        if(gevaProcess != null)
            if(gevaInputReader != null && gevaInputReader.isAlive() == true
            || gevaErrorReader != null && gevaErrorReader.isAlive() == true)
                if(GEVAMessage.showMessage
                (   this,
                    I18N.get("ui.run.geru.wrn"),
                    GEVAMessage.QUESTION_TYPE
                  | GEVAMessage.OK_CANCEL_OPTION
                ) == GEVAMessage.OK_OPTION)
                {   // Implicitly, after this runs and the process has ended,
                    //  all the threads (running the stream readers) should exit.
                    //  This fakes a user clicking the stop button action
                    cmdStop_onClick();
                }
                else
                    // Don't close this window if the user cancels the operation
                    return;
        // Close this window
        this.dispose();
        // Get this run to remove itself from the running list. The running list
        //  tracks (in the event that the main window is closed) which child
        //  windows are open and need to be closed
        GEVARunning.remove(this);

    }
    public void windowActivated(WindowEvent event) { }
    public void windowClosed(WindowEvent event) { }
    public void windowDeactivated(WindowEvent event) { }
    public void windowDeiconified(WindowEvent event) { }
    public void windowIconified(WindowEvent event) { }
    public void windowOpened(WindowEvent event) { }

    /**
     * All run panes are initially hidden, and only come into view when data for
     *  them appears - so if GEVA crashed before graphable data was output, the
     *  graph pane would never show. This class handles whether a pane should be
     *  shown or not by only allowing a pane to be made visible the first time
     *  new data comes in for it, rather than every time new data comes in. This
     *  keeps it from flicking between panes as they read data. The
     *  <var>reason</var> string allows a pane to be shown for different reasons.
     *  If the output pane asked to be shown for new output, which was then
     *  hidden by graph data, the output will not be shown again, but by having
     *  a different reason to be shown, e.g. an error being output, the output
     *  will be shown again for this new reason. Actually, it handles all the
     *  configuration stuff associated with each run pane
     * @author eliott bartley
     */
    class GEVAPaneDetails
    {

        private class ReasonViewed extends HashMap<String, Boolean> { }
        private class Tools extends ArrayList<GEVAPaneToolDetails> { }
        private class Statuses extends HashMap<Component, JPanel> { }

        private int index = -1;
        private String title;
        private ReasonViewed reasonViewed = new ReasonViewed();
        private Tools tools = new Tools();
        private Statuses statuses = new Statuses();

        public GEVAPaneDetails(String title)
        {   this.title = title;
            this.index = -1;
        }

        /**
         * Get the JTabbedPane's tab index for this run pane.
         * @return Tab pane index or -1 if it is not yet set
         */
        public int getIndex()
        {   return index;
        }

        /**
         * Set the JTabbedPane's tab index for this run pane. This is a write-
         *  once value. Setting the index a second time will have no effect
         */
        public void setIndex(int index)
        {   if(this.index == -1)
                this.index = index;
        }

        /**
         * Get the title to use in the JTabbedPane's tab for this run pane.
         */
        public String getTitle()
        {   return title;
        }

        /**
         * Set whether this run pane has asked GEVARun to view it
         */
        public void setViewed(String reason)
        {   reasonViewed.put(reason, true);
        }

        /**
         * Get whether this run pane has asked GEVARun to view it
         */
        public boolean isViewed(String reason)
        {   if(reasonViewed.containsKey(reason) == false)
                return false;
            return reasonViewed.get(reason);
        }

        /**
         * Add a component that is to be displayed along with this run pane
         */
        public void addTool(Component component)
        {   tools.add(new GEVAPaneToolDetails(component));
        }

        /**
         * Remove a component that would have been displayed along with this run
         *  pane. If the component is already being displayed, removeTool will
         *  not remove it from display, just from the list of 'to-be-displayed'
         */
        public void removeTool(Component component)
        {   Iterator<GEVAPaneToolDetails> tool;
            for(tool = tools.iterator(); tool.hasNext();)
                if(tool.next().component == component)
                {   tool.remove();
                    return;
                }
        }

        /**
         * Get the list of tools that are to be displayed for this run pane
         */
        public Tools getTools()
        {   return tools;
        }

        /**
         * Add a panel with a lower bevel border to add the status item so the
         *  hierarchy is MainWindow (GEVARun.this) - StatusBar (guiStatusBar) -
         *  StatusItem (JPanel) - Item (user component)
         */
        public void addStatus(Component component)
        {   JPanel status;
            if(statuses.containsKey(component) == true)
                removeStatus(component);
            status = new JPanel();
            status.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
            status.setBorder(BorderFactory.createLoweredBevelBorder());
            status.add(component);
            guiStatusBar.add(status);
            statuses.put(component, status);
        }

        /**
         * Remove user status item from StatusItem, and remove StatusItem from
         *  StatusBar
         */
        public void removeStatus(Component component)
        {   JPanel status = statuses.get(component);
            if(status == null)
                return;
            statuses.remove(component);
            status.remove(component);
            guiStatusBar.remove(status);
        }

        /**
         * Structure of component details
         */
        public class GEVAPaneToolDetails
        {   public Component component;
            public GEVAPaneToolDetails(Component component)
            {   this.component = component;
            }
        }

    }

}
