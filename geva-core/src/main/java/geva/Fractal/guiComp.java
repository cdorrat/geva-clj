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

/*
 * guiComp.java
 *
 * Created on January 28, 2008, 11:27 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package geva.Fractal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.Scrollable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author patrickmiddleburgh
 */

public class guiComp
     extends JFrame
  implements ActionListener,
             Comparator<LSystem2Config>,
             LSystem2Config.SelectAction
{

//public class guiComp extends JFrame {

    public static final int SM_PHENOTYPE = 0;
    public static final int SM_FITNESS = 1;
    public static final int SM_FRACTALDIMENSION = 2;
    
    public static int sortMethod = SM_FITNESS;
    
    /**
     * Was not picked as a fit individual
     */
    public static final int LF_NOFIT = 0;
    /**
     * Was picked as a fit individual
     */
    public static final int LF_FIT = 1;
    /**
     * Was picked as an unfit individual - to be purged
     */
    public static final int LF_UNFIT = 2;
    
    // Because the window is destroyed and re-created for each generation, all
    //  the settings the user specified (number of parents, position of window)
    //  will be lost, so these following static varialbes keeps them around
    private static Rectangle saveBounds;
    private static int saveParents = 0;

    public guiComp
    (   String[] arrayPheno,
        String[] arrayDepth,
        String[] arrayAngle,
        double[] arrayFitns,
        int valids,
        int generation,
        int generations
    ){  coalesceGrammar = new HashMap<String, LSystem2Config>();
        for(int i = 0; i < arrayPheno.length; i++)
        {   String key = namePheno(arrayPheno[i], arrayDepth[i], arrayAngle[i]);
            if(coalesceGrammar.containsKey(key) == false)
                coalesceGrammar.put
                (   key,
                    new LSystem2Config
                    (   i,
                        arrayPheno[i],
                        Integer.parseInt(arrayDepth[i]),
                        Float.parseFloat(arrayAngle[i]),
                        arrayFitns[i]
                    )
                );
            else
            {   LSystem2Config config = coalesceGrammar.get(key);
                config.indexes.add(i);
                if(config.fitness > arrayFitns[i])
                    config.fitness = arrayFitns[i];
            }

        }
        fits = new int[arrayPheno.length];
        
        init(generation, generations);
        this.pack();
        this.setVisible(true);

    }

    /**
     * Generate a name for this LSystem that will be the same as another LSystem
     *  that outputs the same result
     * @param pheno
     * @param depth
     * @param angle
     * @return
     */
    private String namePheno(String pheno, String depth, String angle)
    {   LSystem2 lSystem = new LSystem2
        (   pheno,
            Integer.parseInt(depth),
            Float.parseFloat(angle)
        );
        return lSystem.getDerivedGrammar() + ":"
             + (int)(lSystem.getAngle() * 1000);
    }
    
    /** Initializes the applet prototype */
    public void init(int generation, int generations) {
        initComponents(generation, generations);
    }
    
    private void initComponents(int generation, int generations) {
        JButton cmdStop;
        JButton cmdGenerate;
        JButton cmdAbout;
        
        /*/ 
        *
        *componant instansiantion
        *
        //*/
        
        header = new JPanel();
        body = new protoCompContainer();
        JScrollPane bodyScroll = new JScrollPane(body);
        footer = new JPanel();

        /*/
         *
         *setting applet size and layout
         *
        //*/
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension size = new Dimension(1000,700);
        if(saveBounds != null)
        {   super.setBounds(saveBounds);
            size.width = saveBounds.width;
            size.height = saveBounds.height;
        }
        super.setPreferredSize(size);
        super.setSize(size);
        
        setLayout(new BorderLayout());
        
        FlowLayout bodyLayout = new FlowLayout(FlowLayout.LEFT);
        FlowLayout footerLayout = new FlowLayout();
        body.setLayout(bodyLayout);
        footer.setLayout(footerLayout);
        super.setVisible(true);

        // Render settings  

        int w = getBestWidth();

        ArrayList<LSystem2Config> configs = new ArrayList<LSystem2Config>();
        for(LSystem2Config config : coalesceGrammar.values())
            configs.add(config);
        Collections.sort(configs, this);
        for(LSystem2Config config : configs)
            body.add(new protoComp(this, config, w));
        
        cmdStop = new JButton("Stop run");
        cmdStop.addActionListener(new ActionListener()
        {   public void actionPerformed(ActionEvent e)
            {   cmdStop_onClick();
            }
        });
        
        cmdGenerate = new JButton();
        cmdGenerate.setText("Generate");
        cmdGenerate.addActionListener(this);
        
        numMaxParents = new JSpinner(new SpinnerNumberModel(saveParents, 0, fits.length, 1));
        numMaxParents.addChangeListener(new ChangeListener()
        {   public void stateChanged(ChangeEvent e)
            {   saveParents = Integer.parseInt(numMaxParents.getValue().toString());
            }
        });
        
        jLabel_tital = new javax.swing.JLabel();
        jLabel_tital.setFont(new java.awt.Font("Lucida Grande", 1, 36));
        jLabel_tital.setText("GELsys");

        JLabel lblMaxParents = new JLabel("Maximum parents: ");
        JLabel lblGenerations = new JLabel("[" + generation + "] of [" + generations + "]");
        
        cmdAbout = new JButton("About");
        cmdAbout.addActionListener(new ActionListener()
        {   public void actionPerformed(ActionEvent arg0)
            {   cmdAbout_onClick();
            }
        });
        
        /*/
         *
         *adding conponants
         *
        //*/
        
        add(header, BorderLayout.NORTH);
        add(bodyScroll, BorderLayout.CENTER);
        //add(body, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

//        bodyScroll.add(body);

        header.add(jLabel_tital);
        
        footer.add(cmdAbout);
        footer.add(new JSeparator(JSeparator.VERTICAL));
        footer.add(lblMaxParents);
        footer.add(numMaxParents);
        footer.add(cmdGenerate);
        footer.add(lblGenerations);
        footer.add(new JSeparator(JSeparator.VERTICAL));
        footer.add(cmdStop);

    }// </editor-fold>                        

    private int getBestWidth()
    {   int width = super.getContentPane().getWidth() - 9;
        int height = super.getContentPane().getHeight() - 9;
        int layer = 2;
        double w = (double)coalesceGrammar.size() / layer;

        // Loop until the height exceeds the bottom of the visible window
        while(10 + (width / w + 25) * layer < height)
        {   layer++;
            w = (double)coalesceGrammar.size() / layer;
        }
        // Use the previous layer which was the last successful one
        w = Math.ceil((double)coalesceGrammar.size() / (layer - 1));
        w = width / w - 5;
        // Don't be extreme
        if(w < 80) w = 80;
        if(w > 256) w = 256;
        return (int)w;
    }
    
    public int compare(LSystem2Config o1, LSystem2Config o2)
    {
        
        switch(sortMethod)
        {
            case SM_PHENOTYPE:
                return comparePhenotype(o1, o2);

            case SM_FITNESS:
                if(o1.fitness < o2.fitness)
                    return -1;
                else
                if(o1.fitness > o2.fitness)
                    return 1;
                return 0;

            case SM_FRACTALDIMENSION:
                return compareFractalDimension(o1, o2);
                
        }
        
        return 0;

    }

    int comparePhenotype(LSystem2Config o1, LSystem2Config o2)
    {   int b1 = 0;
        int b2 = 0;
        int d = 1;

        for(int i = 0; i < o1.grammar.length(); i++)
            if(o1.grammar.charAt(i) == 'F')
                b1++;

        for(int i = 0; i < o2.grammar.length(); i++)
            if(o2.grammar.charAt(i) == 'F')
                b2++;

        return b2 - b1;
        
    }

    int compareFractalDimension(LSystem2Config o1, LSystem2Config o2)
    {   int b1;
        int b2;

        b1 = (int)(new LSystem2FDBoxCounting
        (   null,
            o1.grammar,
            o1.depth,
            o1.angle,
            256
        ).calcFractalDimension() * 1000);
        b2 = (int)(new LSystem2FDBoxCounting
        (   null,
            o2.grammar,
            o2.depth,
            o2.angle,
            256
        ).calcFractalDimension() * 1000);

        return b2 - b1;
        
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        close();
    }
    
    public boolean stillRunning(){
        return stillRunning;
    }

    public int[] getGELSYSFitness(){
        for(LSystem2Config config : coalesceGrammar.values())
            if(config.select == true)
                for(Integer index : config.indexes)
                    fits[index] = LF_FIT;
            else
            if(config.purge == true)
                for(Integer index : config.indexes)
                    fits[index] = LF_UNFIT;
        return fits;
    }

    void cmdStop_onClick()
    {   System.exit(0);
    }

    void cmdAbout_onClick()
    {   new AboutDialog(this);
        
    }
    
    void close()
    {   stillRunning = false;
        saveBounds = super.getBounds();
        synchronized(this) { super.notify(); }
        super.dispose();
    }
    
    public void selectPerformed()
    {   int maxParents = Integer.parseInt(numMaxParents.getValue().toString());
        if(maxParents != 0)
            for(LSystem2Config config : coalesceGrammar.values())
                if(config.select == true)
                    if(--maxParents == 0)
                        close();
    }
    
    // Variables declaration - do not modify
    //GUI componants
    private JPanel header;
    private JPanel body;
    private JPanel footer;
    private JLabel jLabel_tital;
    
    HashMap<String, LSystem2Config> coalesceGrammar;
    int[]       fits;
    JSpinner numMaxParents;
    final String Grammar1 = "F=F[+F]F[-F]F+F-F";
    private boolean stillRunning = true;
    //final String Grammar2 = "F=F[+F]F[-F]F+F-F+F";
    
    
    // End of variables declaration                   

    private class AboutDialog extends JDialog
    {
        
        public AboutDialog(Dialog owner)
        {   super(owner);
            init();
        }
        
        public AboutDialog(Frame owner)
        {   super(owner);
            init();
        }
        
        private void init()
        {   JPanel guiAbout = new JPanel();
            JPanel guiText = new JPanel();
            JLabel guiImage;
            JButton cmdOk = new JButton("OK");
            Dimension size = new Dimension(600, 450);
            super.setTitle("About GE LSystem");
            super.setPreferredSize(size);
            super.setSize(size);
            super.setBounds
            (   super.getOwner().getX() + (super.getOwner().getWidth() - getWidth()) / 2,
                super.getOwner().getY() + (super.getOwner().getHeight() - getHeight()) / 2,
                size.width,
                size.height
            );
            super.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            super.setModal(true);
            super.setLayout(new BorderLayout());
            
            guiImage = new JLabel(new ImageIcon(ClassLoader.getSystemResource
            (   "UI/res/gelsbout.png"
            )));
            guiImage.setBorder(BorderFactory.createEtchedBorder());
            guiImage.setBackground(Color.white);
            guiImage.setOpaque(true);
            
            guiAbout.setLayout(new BorderLayout());
            guiAbout.add(guiImage, BorderLayout.CENTER);
            guiAbout.add(guiText, BorderLayout.SOUTH);
            guiText.setLayout(new GridBagLayout());
            gridAdd(guiText, new JLabel("1."), 0, 0, 0);
            gridAdd(guiText, new Label("Individual LSystem. This can be clicked to view the LSystem in a separate window."), 1, 0, 1);
            gridAdd(guiText, new JLabel("2."), 0, 1, 0);
            gridAdd(guiText, new Label("Fitness: the smaller the red bar, the better the fitness of this LSystem. Each time an LSystem is chosen as a parent, its fitness improves."), 1, 1, 1);
            gridAdd(guiText, new JLabel("3."), 0, 2, 0);
            gridAdd(guiText, new Label("Choose which LSystems should become parents of the next generation. Parents will have their fitness improved.\nChoosing which to purge will forcably remove that LSystem from the next generation."), 1, 2, 1);
            gridAdd(guiText, new JLabel("4."), 0, 3, 0);
            gridAdd(guiText, new Label("Update the population based on the chosen parents. Parents will automatically be chosen if too few have been checked.\nMaximum parents specifies how many parents must be chosen before the next generation will be automatically generated. Setting maximum parents to zero will switch off automatic generation."), 1, 3, 1);
            gridAdd(guiText, new JLabel("5."), 0, 4, 0);
            gridAdd(guiText, new Label("Stop the run early. The run will normally end when the specified number of generations has been reached."), 1, 4, 1);
            
            cmdOk.addActionListener(new ActionListener()
            {   public void actionPerformed(ActionEvent arg0)
                {   AboutDialog.super.dispose();
                }
            });
            
            super.add(guiAbout, BorderLayout.CENTER);
            super.add(cmdOk, BorderLayout.SOUTH);
            super.setVisible(true);
        }

        private class Label extends JTextArea
        {   public Label(String text)
            {   super(text);
                setEditable(false);
                setLineWrap(true);
                setWrapStyleWord(true);
                setBackground((Color)UIManager.get("Label.background"));
                setForeground((Color)UIManager.get("Label.foreground"));
                setFont((Font)UIManager.get("Label.font"));
            }
        }

        private void gridAdd
        (   Container container,
            Container control,
            int       gridX,
            int       gridY,
            double    weightX
        ){  GridBagConstraints constraints = new GridBagConstraints
            (   gridX, gridY,
                1, 1,
                weightX, 0,
                GridBagConstraints.NORTH,
                GridBagConstraints.BOTH,
                new Insets(1, 1, 1, 1),
                5, 5
            );
            constraints.anchor = GridBagConstraints.NORTH;
            container.add(control, constraints);
            if(control instanceof JLabel)
                ((JLabel)control).setVerticalAlignment(JLabel.NORTH);
        }

    }
    
}

/**
 * JPanel sits in a JScrollPane and has a FlowLayout layout. Because the
 *  JScrollPane gives the JPanel all the width it can grab, it doesn't bother
 *  wrapping the flowing content to the JScrollPane's JViewport, and ends up
 *  displaying as one wide line that can be scrolled. There is no simple
 *  way to change this so that it wraps with the width of the JViewport and
 *  scrolls vertically, but this class is a workaround that resizes the JPanel
 *  automatically to the width of the JViewport
 * @author Internet
 */
class protoCompContainer extends JPanel implements Scrollable
{

    @Override
    public void setBounds(int x, int y, int width, int height)
    {   super.setBounds(x, y, getParent().getWidth(), height);
    }

    @Override
    public Dimension getPreferredSize()
    {   return new Dimension(getWidth(), getPreferredHeight());
    }

    public Dimension getPreferredScrollableViewportSize()
    {   return super.getPreferredSize();
    }

    public int getScrollableUnitIncrement
    (   Rectangle visibleRect,
        int orientation,
        int direction
    ){  int hundredth =
        (   orientation == SwingConstants.VERTICAL
            ? super.getParent().getHeight()
            : super.getParent().getWidth()
        ) / 100;
        return hundredth == 0 ? 1 : hundredth; 
    }

    public int getScrollableBlockIncrement
    (   Rectangle visibleRect,
        int orientation,
        int direction
    ){  return orientation == SwingConstants.VERTICAL 
             ? super.getParent().getHeight()
             : super.getParent().getWidth();
    }

    public boolean getScrollableTracksViewportWidth()
    {   return true;
    }

    public boolean getScrollableTracksViewportHeight()
    {   return false;
    }

    private int getPreferredHeight()
    {   assert super.getLayout() instanceof FlowLayout
             : super.getLayout().getClass().getName();
        int rv = 0;
        for(int k = 0, count = super.getComponentCount(); k < count; k++)
        {   Component comp = super.getComponent(k);
            Rectangle r = comp.getBounds();
            int height = r.y + r.height;
            if(height > rv)
                rv = height;
        }
        rv += ((FlowLayout)super.getLayout()).getVgap();
        return rv;
    }

}