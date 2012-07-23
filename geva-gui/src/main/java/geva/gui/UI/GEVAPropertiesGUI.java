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

package geva.gui.UI;

import geva.gui.UI.Run.GEVAGraphPaneCategory;
import geva.gui.UI.Run.GEVAGraphPaneConfig;
import geva.gui.UI.Run.GEVAGraphPaneItem;
import geva.gui.UI.Run.GEVARun;
import geva.gui.UI.Run.GEVARunning;
import geva.gui.UI.Run.JSci.GEVAGraphColours;
import geva.gui.Util.Constants;
import geva.gui.Util.GEVAHelper;
import geva.gui.Util.GEVAUncaught;
import geva.gui.Util.I18N;

import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * Main dialog window for configuring the GEVA properties files
 * @author eliottbartley
 */
public class GEVAPropertiesGUI
     extends JFrame
  implements ActionListener,
             WindowListener,
             MouseListener,
             GEVAActionListener,
             GEVADirtyListener
{

    private static class FitnessDetails extends HashMap<String, GEVAFitness> { }

    /**
     * During setClean(), clean by saving (after prompting user if they want to)
     */
    private static final int SCF_PROMPT = 0;
    /**
     * During setClean(), clean by just marking as such and disabling apply
     */
    private static final int SCF_MARK = 1;
    /**
     * The filename of the currently loaded properties file, null if none loaded
     */
    private String propertiesFile;
    /**
     * The currently loaded properties file, null if none loaded.
     */
    private Properties properties;
    /**
     * When building the list of grammar files for global grammar drop-down,
     *  save the list for the properties grammar drop-down
     */
    private String grammarFiles;
    /**
     * Store list of fitness functions and their associated details, default
     *  grammar, dependency jar files, etc.
     */
    private FitnessDetails fitnessDetails = new FitnessDetails();
    /**
     * Store the currently selected fitness function
     */
    private GEVAFitness fitness;
    /**
     * Set to true while the UI is initialising. Useful to prevent actions
     *  happening before the UI is ready to respond to them
     */
    private boolean initialising;
    /**
     * Store whether the properties need to be saved. Set to true when a
     * property value is modified. Set to false when save() or load() is
     * executed
     */
    private boolean isDirty = false;
    /**
     * Store whether a dirty action will actually mark it dirty. This is used
     *  when internally modifying fields to a formatted equivalent, e.g.
     *  changing a value to its cononical form - basically, this is only used
     *  when the modification makes no difference to the meaning of the value
     *  and could be used correctly either way
     */
    private boolean canDirty = true;
    /**
     * Store how window was closed, will be either OK_OPTION or CANCEL_OPTION
     */
    private int resultOption;

    // Controls on this UI
    private JLabel             lblPropertiesFiles;
    private JComboBox          cboPropertiesFiles;
    private JButton            cmdPropertiesFilesNew;
    private JButton            cmdPropertiesFilesDelete;
    private GEVABookContainer  guiProperties;
    private GEVAChoiceProperty guiFitnessFunctions;
    private GEVAChoiceProperty guiGrammarFiles;
    private GEVAControl        guiPopulationSize;
    private GEVAControl        guiReplacementType;
    private GEVAControl        guiEliteSize;
    private GEVAControl        guiSelection;
    private GEVAControl        guiTournamentSize;
    private GEVAControl        guiPickSize;
    private JButton            cmdRun;
    private JButton            cmdSave;
    private JButton            cmdClose;
    private JButton            cmdConfig;

    /**
     * Create a new dialog using the default settings
     */
    private GEVAPropertiesGUI()
    {   if(this.initialiseUI() == true)
            this.showUI();
    }

    /**
     * Initiailise all the controls visible on the UI
     */
    private boolean initialiseUI()
    {   Dimension screen;
        boolean initOk = true;

        // flag that UI actions can't respond as UI isn't complete
        this.initialising = true;

        // whip out the splash screen - will show for 2 seconds and close itself
        new GEVASplash(this);

        // centre the window on the screen
        screen = Toolkit.getDefaultToolkit().getScreenSize();
        super.setBounds
        (   (screen.width - 500) / 2,
            (screen.height - 740) / 2,
            500,
            740
        );
        super.setTitle(I18N.get("ui.gui"));
        super.addWindowListener(this);
        super.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        super.getContentPane().setLayout(new BorderLayout());

        // List properties files
        initialisePropertiesFilesUI();

        // Load and prepare all configuration details
        initOk = initialiseFFConfig() & initOk;
        initOk = initialiseGraphConfig() & initOk;

        // Show individual properties of selected properties file
        initialisePropertiesUI();

        // OK/Apply/Cancel
        initialiseCommandUI();

        // flag that UI actions can respond as UI is complete
        this.initialising = false;

        // Autoload the HelloWorld.properties, or the first properties file
        cboPropertiesFiles.setSelectedItem(GEVAConfig.getSelectedPropertiesFile());
        if(cboPropertiesFiles.getSelectedItem() == null)
            unloadPropertiesFile();

        if(initOk == false)
            return configure(true);

        return true;

    }

    /**
     * Prepare all interface stuff for displaying the properties' files
     *  creating new properties files, and updating or deleting existing ones
     */
    private void initialisePropertiesFilesUI()
    {   JPanel guiPropertiesFiles = new JPanel();
        File file;
        String[] files;

        guiPropertiesFiles.setLayout(new GridBagLayout());

        lblPropertiesFiles = new JLabel(I18N.get("ui.gui.labl.prop"));
        cboPropertiesFiles = new JComboBox();
        cmdPropertiesFilesNew = new JButton(I18N.get("ui.gui.ctrl.pnew"));
        cmdPropertiesFilesDelete = new JButton(I18N.get("ui.gui.ctrl.pdel"));

        file = new File(GEVAConfig.getPropertiesAbsPath());
        files = file.list();

        // Build a list of *.properties files
        if(files != null)
            for(int i = 0; i < files.length; i++)
                if(files[i].length()
                 > Constants.txtPropertiesExt.length()
                && files[i]
                    .substring
                    (   files[i].length()
                      - Constants.txtPropertiesExt.length()
                    )
                    .equalsIgnoreCase(Constants.txtPropertiesExt)
                )   cboPropertiesFiles.addItem(files[i]);

        cboPropertiesFiles.addActionListener(this);
        cmdPropertiesFilesNew.addActionListener(this);
        cmdPropertiesFilesDelete.addActionListener(this);

        GEVAHelper.gridAdd(guiPropertiesFiles, lblPropertiesFiles,       0, 0, 0);
        GEVAHelper.gridAdd(guiPropertiesFiles, cboPropertiesFiles,       1, 0, 1);
        GEVAHelper.gridAdd(guiPropertiesFiles, cmdPropertiesFilesNew,    2, 0, 0);
        GEVAHelper.gridAdd(guiPropertiesFiles, cmdPropertiesFilesDelete, 3, 0, 0);

        file = new File(GEVAConfig.getGrammarAbsPath());
        files = file.list();

        // Build a list of *.bnf files
        if(files != null)
            for(int i = 0; i < files.length; i++)
                if(files[i].length()
                 > Constants.txtGrammarExt.length()
                && files[i]
                    .substring
                    (   files[i].length()
                      - Constants.txtGrammarExt.length()
                    )
                    .equalsIgnoreCase(Constants.txtGrammarExt)
                )   if(grammarFiles == null)
                        grammarFiles = files[i];
                    else
                        grammarFiles += "," + files[i];

        this.getContentPane().add(guiPropertiesFiles, BorderLayout.NORTH);

    }

    /**
     * Parse ff.config
     */
    private boolean initialiseFFConfig()
    {   FileInputStream stream;
        BufferedReader reader;
        String line;
        int mode = 0;   // 0=class files, 1=jar files, 2=cmd extras

        try
            {

            stream = new FileInputStream
            (   GEVAConfig.getConfigAbsPath() + Constants.cfgFFFile
            );
            reader = new BufferedReader(new InputStreamReader(stream));
            while((line = reader.readLine()) != null)
            {   // Strip off the comments
                if(line.indexOf('#') != -1)
                    line = line.substring(0, line.indexOf('#'));
                // Strip off the fluff
                line = line.trim();
                // Parse
                if(line.length() != 0)
                    if(line.charAt(0) == '-')
                        if(line.equals("-class") == true)
                            mode = 0;
                        else
                        if(line.equals("-jar") == true)
                            mode = 1;
                        else
                        if(line.equals("-cmd") == true)
                            mode = 2;
                        else
                            mode = -1;
                    else
                    if(line.indexOf(',') != -1)
                        switch(mode)
                        {   case 0: initialiseFFConfigClass(line); break;
                            case 1: initialiseFFConfigJar(line); break;
                            case 2: initialiseFFConfigCmd(line); break;
                        }
            }

        }
        catch(IOException e)
        {   GEVAMessage.showMessage
            (   this,
                I18N.get("ui.gui.fflo.err", e.getMessage()),
                GEVAMessage.ERROR_TYPE
            );
            e.printStackTrace();
            return false;
        }

        GEVAJarHunter.start();
        
        return true;

    }

    /**
     * Make sense of the -class section
     */
    private void initialiseFFConfigClass(String line)
    {   String[] parts = GEVAHelper.trim(line.split(","));
        GEVAFitness ff = new GEVAFitness();
        ff.grammarFile = parts[1];
        for(int i = 2; i < parts.length; i++)
            ff.names.add(parts[i]);
        fitnessDetails.put(parts[0], ff);
    }

    /**
     * Make sense of the -jar section
     */
    private void initialiseFFConfigJar(String line)
    {   String[] parts = GEVAHelper.trim(line.split(","));
        GEVAJarHunter.add(parts[0], parts[1]);
    }

    /**
     * Make sense of the -cmd section
     */
    private void initialiseFFConfigCmd(String line)
    {   int index = line.indexOf(',');
        GEVAFitness.fitnessCommands.put
        (   line.substring(0, index),
            line.substring(index + 1)
        );
    }

    /**
     * Parse graph.config
     */
    private boolean initialiseGraphConfig()
    {   FileInputStream stream;
        BufferedReader reader;
        String line;
        GEVAGraphPaneCategory category = null;

        try
        {

            stream = new FileInputStream
            (   GEVAConfig.getConfigAbsPath() + Constants.cfgGraphFile
            );
            reader = new BufferedReader(new InputStreamReader(stream));
            while((line = reader.readLine()) != null)
            {   // Strip comments
                if(line.indexOf('#') != -1)
                    line = line.substring(0, line.indexOf('#'));
                line = line.trim();
                // Parse categories
                if(line.length() != 0)
                    // Parse page
                    if(line.charAt(0) == '-')
                    {   category = new GEVAGraphPaneCategory(line.substring(1));
                        GEVAGraphPaneConfig.addCategory
                        (   Constants.GRAPH_CATEGORY_GEVA,
                            category
                        );
                    }
                    else
                    if(line.length() != 0 && category != null)
                        initialiseGraphConfigItem(category, line);
            }

        }
        catch(IOException e)
        {   GEVAMessage.showMessage
            (   this,
                I18N.get("ui.gui.fflo.err", e.getMessage()),
                GEVAMessage.ERROR_TYPE
            );
            e.printStackTrace();
            return false;
        }

        return true;

    }

    /**
     * Parse the item lines in a given category
     */
    private void initialiseGraphConfigItem
    (   GEVAGraphPaneCategory category,
        String line
    ){  String[] parts = GEVAHelper.trim(line.split(","));
        String name = "";
        int visible = GEVAGraphPaneItem.VISIBLE_HIDE;
        Color colour = null;
        String title = "";
        String measure = "";
        String errorName = "";
        if(parts.length < 1)
            return;
        name = parts[0];
        if(parts.length > 1)
            if(parts[1].equalsIgnoreCase("show") == true)
                visible = GEVAGraphPaneItem.VISIBLE_SHOW;
            else
            if(parts[1].equalsIgnoreCase("bold") == true)
                visible = GEVAGraphPaneItem.VISIBLE_BOLD;

        if(parts.length > 2) colour = initialiseGraphConfigItemColour(parts[2]);
        if(parts.length > 3) title = parts[3];
        if(parts.length > 4) measure = parts[4];
        if(parts.length > 5) errorName = parts[5];
        category.addItem
        (   new GEVAGraphPaneItem
            (   name,
                visible,
                colour,
                title,
                measure,
                errorName
            )
        );
    }
    private Color initialiseGraphConfigItemColour(String colour)
    {   if((colour.length() == 3 || colour.length() == 6)
        && Pattern.matches("[0-9a-fA-F]*", colour) == true)
        {   int red;
            int green;
            int blue;
            if(colour.length() == 3)
            {   red = Integer.parseInt(colour.substring(0, 1), 16);
                green = Integer.parseInt(colour.substring(1, 2), 16);
                blue = Integer.parseInt(colour.substring(2, 3), 16);
                return new Color
                (   red * 16 + red,
                    green * 16 + green,
                    blue * 16 + blue
                );
            }
            else
            {   red = Integer.parseInt(colour.substring(0, 2), 16);
                green = Integer.parseInt(colour.substring(2, 4), 16);
                blue = Integer.parseInt(colour.substring(4, 6), 16);
                return new Color(red, green, blue);
            }
        }
        else
            return GEVAGraphColours.getNamedColour(colour);
    }

    /**
     * Prepare the OK, Cancel, etc (main GUI buttons)
     */
    private void initialiseCommandUI()
    {   JPanel guiCommand = new JPanel();
        JPanel guiSeparator = new JPanel();

        guiCommand.setLayout(new GridBagLayout());

        cmdRun    = new JButton(I18N.get("ui.gui.ctrl.prun"));
        cmdSave   = new JButton(I18N.get("ui.gui.ctrl.save"));
        cmdClose  = new JButton(I18N.get("ui.ctrl.exit"));
        cmdConfig = new JButton(I18N.get("ui.gui.ctrl.conf"));

        cmdRun.addActionListener(this);
        cmdSave.addActionListener(this);
        cmdSave.addMouseListener(this);
        cmdClose.addActionListener(this);
        cmdConfig.addActionListener(this);

        cmdSave.setEnabled(false);

        GEVAHelper.gridAdd(guiCommand, cmdRun,       0, 0, 0);
        GEVAHelper.gridAdd(guiCommand, guiSeparator, 1, 0, 1);
        GEVAHelper.gridAdd(guiCommand, cmdConfig,    2, 0, 0);
        GEVAHelper.gridAdd(guiCommand, cmdSave,      3, 0, 0);
        GEVAHelper.gridAdd(guiCommand, cmdClose,     4, 0, 0);

        this.getContentPane().add(guiCommand, BorderLayout.SOUTH);

    }

    /**
     * Prepare all interface stuff for displaying the actual properties
     *  available in the properties files
     */
    private void initialisePropertiesUI()
    {

        guiProperties = new GEVABookContainer(this, I18N.get("ui.gui.tip"));

        initialisePropGeneralUI();

        this.getContentPane().add
        (   guiProperties.getComponent(),
            BorderLayout.CENTER
        );

    }

    private void initialisePropGeneralUI()
    {   GEVAControlGroup tournamentGroup = new GEVAControlGroup();
        GEVAControlGroup userpickGroup = new GEVAControlGroup();
        GEVAControlGroup generationGapGroup = new GEVAControlGroup();
        GEVAControlGroup initialiserGroup = new GEVAControlGroup();
        GEVAControlGroup rampedInitGroup = new GEVAControlGroup();
	GEVAControlGroup lSysGroup = new GEVAControlGroup();
	guiProperties.addControlGroup("lSysGroup", lSysGroup);
        guiProperties.addControlGroup("tournamentGroup", tournamentGroup);
        guiProperties.addControlGroup("userpickGroup", userpickGroup);
        guiProperties.addControlGroup("generationGapGroup", generationGapGroup);
        guiProperties.addControlGroup("initialiserGroup", initialiserGroup);
        guiProperties.addControlGroup("rampedInitGroup", rampedInitGroup);

        String fitnessFunctions = null;

        GEVAPageContainer guiPageGeneral = new GEVAPageContainer
        (   this,
            guiProperties,
            I18N.get("ui.gui.geva"),
            I18N.get("ui.gui.geva.tip")
        );

        // Container for general general settings
        GEVAPropertyRowContainer container = new GEVAPropertyRowContainer
        (   this,
            guiPageGeneral,
            I18N.get("ui.gui.gene"),
            I18N.get("ui.gui.gene.tip")
        );
        new GEVAChoiceProperty
        (   this,
            container,
            GEVAChoiceProperty.PT_BOOL,
            I18N.get("ui.gui.gene.solv"),
            geva.gui.Util.Constants.STOP_WHEN_SOLVED,
            I18N.get("ui.gui.gene.solv.tip"),
            I18N.get("ui.gui.gene.solv.true"),
            I18N.get("ui.gui.gene.solv.true") + ","
          + I18N.get("ui.gui.gene.solv.fals")
        );
        new GEVAChoiceProperty
        (   this,
            container,
            GEVAChoiceProperty.PT_GROUP,
            I18N.get("ui.gui.gene.codo"),
            "null",
            I18N.get("ui.gui.gene.codo.tip"),
            I18N.get("ui.gui.gene.codo.int"),
            I18N.get("ui.gui.gene.codo.int")
        );
        new GEVANumberProperty
        (   this,
            container,
            GEVANumberProperty.PT_INT,
            I18N.get("ui.gui.gene.gner"),
            geva.gui.Util.Constants.GENERATION,
            I18N.get("ui.gui.gene.gner.tip"),
            "20",
            "1,0," + (GEVAConfig.isExpert() == true ? "" : "20000")
        );
        guiPopulationSize = new GEVANumberProperty
        (   this,
            container,
            GEVANumberProperty.PT_INT,
            I18N.get("ui.gui.gene.popu"),
            geva.gui.Util.Constants.POPULATION_SIZE,
            I18N.get("ui.gui.gene.popu.tip"),
            "100",
            "1,0," + (GEVAConfig.isExpert() == true ? "" : "5000")
        );
        guiPopulationSize.addActionListener(this);
        new GEVANumberProperty
        (   this,
            container,
            GEVANumberProperty.PT_INT,
            I18N.get("ui.gui.gene.wrap"),
            geva.gui.Util.Constants.MAX_WRAPS,
            I18N.get("ui.gui.gene.wrap.tip"),
            "1",
            "1,0,"
        );
        new GEVAFileProperty
        (   this,
            container,
            GEVAFileProperty.PT_FILE_OR_FOLDER,
            I18N.get("ui.gui.gene.outf"),
            geva.gui.Util.Constants.OUTPUT,
            I18N.get("ui.gui.gene.outf.tip"),
            "",
            ",," + GEVAFileProperty.PP_IGNORE_EXIST
        );

        // Container for general fitness settings
        container = new GEVAPropertyRowContainer
        (   this,
            guiPageGeneral,
            I18N.get("ui.gui.fifu"),
            I18N.get("ui.gui.fifu.tip")
        );
        for(String fitnessFunctionEx : fitnessDetails.keySet())
            if(fitnessFunctions == null)
                fitnessFunctions = fitnessFunctionEx;
            else
                fitnessFunctions += "," + fitnessFunctionEx;
        guiFitnessFunctions = new GEVAChoiceProperty
        (   this,
            container,
            GEVAChoiceProperty.PT_ENUM,
            I18N.get("ui.gui.fifu.fifu"),
            geva.gui.Util.Constants.FITNESS_FUNCTION,
            I18N.get("ui.gui.fifu.fifu.tip"),
            "FitnessEvaluation.PatternMatch.WordMatch",
            fitnessFunctions
        );
        guiFitnessFunctions.addActionListener(this);
        guiGrammarFiles = new GEVAChoiceProperty
        (   this,
            container,
            GEVAChoiceProperty.PT_ENUM,
            I18N.get("ui.gui.fifu.gram"),
            geva.gui.Util.Constants.GRAMMAR_FILE,
            I18N.get("ui.gui.fifu.gram.tip"),
            null,
            grammarFiles
        );
        guiGrammarFiles.addActionListener(this);

        // Container for general fitness settings
        container = new GEVAPropertyRowContainer
        (   this,
            guiPageGeneral,
            I18N.get("ui.gui.rese"),
            I18N.get("ui.gui.rese.tip")
        );
        guiReplacementType = new GEVAChoiceProperty
        (   this,
            container,
            GEVAChoiceProperty.PT_GROUP,
            I18N.get("ui.gui.rese.rety"),
            geva.gui.Util.Constants.REPLACEMENT_TYPE,
            I18N.get("ui.gui.rese.rety.tip"),
            "steady_state",
            "steady_state,,generational,generationGapGroup"
        );
	guiReplacementType.addActionListener(this);
        guiEliteSize = new GEVANumberProperty
        (   this,
            container,
            GEVANumberProperty.PT_INT,
            I18N.get("ui.gui.rese.elit"),
            geva.gui.Util.Constants.ELITE_SIZE,
            I18N.get("ui.gui.rese.elit.tip"),
            "1",
            "1,0,"
        );
        guiEliteSize.addActionListener(this);
        guiSelection = new GEVAChoiceProperty
        (   this,
            container,
            GEVAChoiceProperty.PT_GROUP,
            I18N.get("ui.gui.rese.seop"),
            geva.gui.Util.Constants.SELECTION_OPERATION,
            I18N.get("ui.gui.rese.seop.tip"),
            "Operator.Operations.TournamentSelect",
            "Operator.Operations.TournamentSelect,tournamentGroup,"
          + "Operator.Operations.ScaledRouletteWheel,,"
          + "Operator.Operations.ProportionalRouletteWheel,,"
          + "Operator.Operations.SimpleUserSelect,userpickGroup,"
          + "Operator.Operations.LSystemSelect,lSysGroup,"
        );
        guiTournamentSize = new GEVANumberProperty
        (   this,
            container,
            GEVANumberProperty.PT_INT,
            I18N.get("ui.gui.rese.tosi"),
            geva.gui.Util.Constants.TOURNAMENT_SIZE,
            I18N.get("ui.gui.rese.tosi.tip"),
            "3",
            "1,1,"
        );
        guiTournamentSize.addActionListener(this);
        tournamentGroup.add(guiTournamentSize);

	lSysGroup.add(guiEliteSize);
	lSysGroup.add(guiReplacementType);
	lSysGroup.setHiding(true);

        guiPickSize = new GEVANumberProperty
        (   this,
            container,
            GEVANumberProperty.PT_INT,
            I18N.get("ui.gui.rese.pisi"),
            geva.gui.Util.Constants.USERPICK_SIZE,
            I18N.get("ui.gui.rese.pisi.tip"),
            "10",
            "1,2,"
        );
        guiPickSize.addActionListener(this);
        userpickGroup.add(guiPickSize);

        // Container for general fitness settings
        container = new GEVAPropertyRowContainer
        (   this,
            guiPageGeneral,
            I18N.get("ui.gui.init"),
            I18N.get("ui.gui.init.tip")
        );
        new GEVAChoiceProperty
        (   this,
            container,
            GEVAChoiceProperty.PT_GROUP,
            I18N.get("ui.gui.init.init"),
            geva.gui.Util.Constants.INITIALISER,
            I18N.get("ui.gui.init.init.tip"),
            "Operator.Initialiser",
            "Operator.Initialiser,initialiserGroup,"
          + "Operator.RampedFullGrowInitialiser,rampedInitGroup"
        );
        rampedInitGroup.add
        (   new GEVANumberProperty
            (   this,
                container,
                GEVANumberProperty.PT_INT,
                I18N.get("ui.gui.init.mxdp"),
                geva.gui.Util.Constants.MAX_DEPTH,
                I18N.get("ui.gui.init.mxdp.tip"),
                "10",
                "1,0,"
            )
        );
        initialiserGroup.add
        (   new GEVANumberProperty
            (   this,
                container,
                GEVANumberProperty.PT_INT,
                I18N.get("ui.gui.init.crsi"),
                geva.gui.Util.Constants.INITIAL_CHROMOSOME_SIZE,
                I18N.get("ui.gui.init.crsi.tip"),
                "200",
                "1,0,"
            )
        );
        rampedInitGroup.add
        (   new GEVANumberProperty
            (   this,
                container,
                GEVANumberProperty.PT_FLOAT,
                I18N.get("ui.gui.init.grpr"),
                geva.gui.Util.Constants.GROW_PROBABILITY,
                I18N.get("ui.gui.init.grpr.tip"),
                "0.5",
                "0.1,0,1"
            )
        );

        // Container for everything else
        container = new GEVAPropertyRowContainer
        (   this,
            guiPageGeneral,
            I18N.get("ui.gui.othr"),
            I18N.get("ui.gui.othr.tip")
        );
        new GEVANumberProperty
        (   this,
            container,
            GEVANumberProperty.PT_FLOAT,
            I18N.get("ui.gui.othr.crpr"),
            geva.gui.Util.Constants.CROSSOVER_PROBABILITY,
            I18N.get("ui.gui.othr.crpr.tip"),
            "0.9",
            "0.1,0,1"
        );
        new GEVAChoiceProperty
        (   this,
            container,
            GEVAChoiceProperty.PT_BOOL,
            I18N.get("ui.gui.othr.crpo"),
            geva.gui.Util.Constants.FIXED_POINT_CROSSOVER,
            I18N.get("ui.gui.othr.crpo.tip"),
            I18N.get("ui.gui.othr.crpo.true"),
            I18N.get("ui.gui.othr.crpo.true") + ","
          + I18N.get("ui.gui.othr.crpo.fals")
        );
        new GEVANumberProperty
        (   this,
            container,
            GEVANumberProperty.PT_FLOAT,
            I18N.get("ui.gui.othr.mupr"),
            geva.gui.Util.Constants.MUTATION_PROBABILITY,
            I18N.get("ui.gui.othr.mupr.tip"),
            "0.01",
            "0.01,0,1"
        );

        // Container for padding (crush all other containers)
        new GEVASpringContainer
        (   this,
            guiPageGeneral
        );

    }

    /**
     * Run the selected properties file
     */
    private void runPropertiesFile()
    {   String runPath;
        File file;

        // If the file is dirty, or a new file is in the works, save the
        //  properties to a temporary file and run using that
        // The condition here has been commented out, and now always saves the
        //  properties to a temporary file before running, because, although
        //  the two choices are suppose to be the same when the file is clean,
        //  absolute paths in the saved file can be different! i.e. Saving to a
        //  new properties file resolves the absolute paths from the current
        //  directory and corrects them for the GEVA run, where the corrected
        //  paths for a previous run (saved on a previous run of the GUI) would
        //  be wrong now. I can't remember why this was the case exactly, but it
        //  did happen, and it didn't bother me enough at the time to document
        //  or care about it, but now it's driving me nuts. Update: I think I
        //  remember now why it was a problem, when running from NetBeans versus
        //  running from the compiled jar, they run from different working paths
        //  but use the same properties file, so the relative paths in the
        //  properties file needs to be resolved. Anyway, TODO - fix this!
//        if(isDirty == true || this.propertiesFile == null)
            try
            {
                file = File.createTempFile("gep", Constants.txtPropertiesExt);
                file.deleteOnExit();
                savePropertiesFile(file.getAbsolutePath());
                runPath = file.getAbsolutePath();
            }
            catch(IOException e)
            {   GEVAMessage.showMessage
                (   this,
                    I18N.get("ui.gui.prus.err", e.getMessage()),
                    GEVAMessage.ERROR_TYPE
                );
                e.printStackTrace();
                return;
            }
//        else
  //          runPath = GEVAConfig.propertiesAbsPath + this.propertiesFile;

        GEVARunning.add(new GEVARun(this, runPath, fitness));

    }

    /**
     * Set the GUI to an unloaded state, i.e. No file is loaded.
     * This state is used if loading fails, or a file to load cannot be
     *  determined. While in this state, an extra option exists in the
     *  properties file drop-down, which acts as a dummy entry to show that no
     *  file is loaded, and that a change to this is required to do a load)
     */
    private void unloadPropertiesFile()
    {

        cboPropertiesFiles.setSelectedItem(null);
        setClean(SCF_MARK);
        cmdRun.setEnabled(false);
        guiProperties.setEnabled(false);

        this.propertiesFile = null;

    }

    /**
     * Reset all values to default and select no properties file
     */
    private void newPropertiesFile()
    {   Properties cloneProperties = null;

        // Make sure we're clean - this has to be done here, and not left to be
        //  automatically done as part of changing the selected item because if
        //  the user cancels, there will be no way to know how to go back to the
        //  previous item
        if(setClean() == false)
            return;

        // Mark the file as clean; if it was dirty and setClean asked the user
        //  if they wanted to save and the user said no, it will still be dirty
        //  here. This will be a problem when the null item is selected, as it
        //  will cause setClean to be called again - so forcing it clean here
        //  will prevent this
        setClean(SCF_MARK);

        // If an item is selected, create a saved (memory) copy of the lastest
        //  properties. These are then loaded back after picking the null item
        //  so that the new file will start with the selected files properties
        //  allowing a template to be created and cloned. If no item is selected,
        //  that is, if the user has clicked new or delete, don't do the clone,
        //  which will then use the default values. So clicking new once clones,
        //  and clicking new twice resets to default
        if(cboPropertiesFiles.getSelectedItem() != null)
        {   cloneProperties = new Properties();
            guiProperties.save(cloneProperties);
        }

        // Select null, this will load all the default values and clear the
        //  selected name, but will not reset the active name, as that's only
        //  done in a save action, which may have been cancelled above, so this
        //  is followed by resetting the active name too, to ensure that
        //  clicking save will ask for a new file name and not overwrite the
        //  previously active item
        cboPropertiesFiles.setSelectedItem(null);
        this.propertiesFile = null;

        // Reload the cloned values into the controls if they've been saved
        if(cloneProperties != null)
        {   guiProperties.load(cloneProperties);
            setClean(SCF_MARK);
        }

        // Allow user to save, without actually making file dirty
        // This way, cancelling won't ask to save if no changes have been made
        //  to a new file, but will still allow the user to force a save of the
        //  new file
        cmdSave.setEnabled(true);

        // Prompt the user to save the new file immediately. This can be
        //  cancelled and will remain showing the new unsaved file rather than
        //  jump back to the previously loaded file
        savePropertiesFile();

    }

    /**
     * Load the active properties file
     */
    private boolean loadPropertiesFile()
    {   FileInputStream stream;

        // Don't load unless the current file has been saved successfully, or
        //  the user stated they don't want to save.
        if(setClean() == false)
        {   // Change the selected properties file back from the file selected
            //  to be loaded, to the file that is still loaded
            cboPropertiesFiles.setSelectedItem(this.propertiesFile);
            return false;
        }

        try
        {

            this.properties = new Properties();
            if(cboPropertiesFiles.getSelectedItem() != null)
            {   this.propertiesFile = cboPropertiesFiles
                    .getSelectedItem()
                    .toString();
                stream = new FileInputStream
                (   GEVAConfig.getPropertiesAbsPath() + this.propertiesFile
                );
                this.properties.load(stream);
            }

            if(guiProperties.load(this.properties) == false)
                GEVAMessage.showMessage
                (   this,
                    I18N.get("ui.gui.prlo.wrn"),
                    GEVAMessage.WARNING_TYPE
                );

            // force to a clean state as loading modified the fields
            setClean(SCF_MARK);

            cmdRun.setEnabled(true);
            guiProperties.setEnabled(true);

            return true;

        }
        catch(IOException e)
        {   // Put the GUI into an unloaded state, i.e. no file is loaded.
            //  This will also change the drop-down so that it no longer
            //  refers to the file that was attempted to load. It will refer
            //  to a dummy entry to say to the user, pick a file to load
            unloadPropertiesFile();
            GEVAMessage.showMessage
            (   this,
                I18N.get("ui.gui.prlo.err", e),
                GEVAMessage.ERROR_TYPE
            );
            e.printStackTrace();
            return false;
        }

    }

    private boolean savePropertiesFile()
    {   return savePropertiesFile(null);
    }

    /**
     * Save the active properties file
     * @return true if the file saved successfully, false if there was an
     *  exception
     */
    private boolean savePropertiesFile(String tempFilename)
    {   FileOutputStream stream;
        String propertiesFileEx = null;
        boolean newFile = false;
        String filename;

        try
        {

            // Before actually saving, check that all the input parameters don't
            //  have any errors. If they do, warn the user and allow them to
            //  stop the save process
            if(guiProperties.save(this.properties) == false)
                if(GEVAMessage.showMessage
                (   this,
                    I18N.get("ui.gui.prsa.wrn"),
                    GEVAMessage.WARNING_TYPE | GEVAMessage.YES_NO_OPTION
                ) == GEVAMessage.NO_OPTION)
                    // There were errors, user doesn't want to save those errors,
                    //  exit
                    return false;

            if(tempFilename == null)
            {

                // Get the active properties file
                propertiesFileEx = getActivePropertiesFile();
                // Update the active properties file to match the selected one
                //  now that the one to be saved is known
                setActivePropertiesFile();

                // If properties file is null here, it can only mean that a new
                //  file is being created, (see getActivePropertiesFile comments
                //  on why this is so), so ask for a filename to save as. Note:
                //  this code cannot be reached when no properties file is
                //  selected due to error (when an error occurs in selecting a
                //  properties file, the active file goes to null) because in
                //  that case, state is set to clean and all fields are
                //  disabled; the only way to dirty the state is to modify a
                //  field's contents - they're disabled so the file can't go
                //  dirty, so save can't be clicked, so this code can't execute,
                //  hopefully :P
                if(propertiesFileEx == null)
                {   propertiesFileEx = getNewPropertiesFile();
                    newFile = true;
                }

                // If still no properties file name is known when this code is
                //  reached, it can only mean the user cancelled the choice to
                //  pick a new filename, so exit early and don't save
                if(propertiesFileEx == null)
                    return false;

                // Use the active properties filename
                filename = GEVAConfig.getPropertiesAbsPath() + propertiesFileEx;

            }
            else
                // Using a temporary file to save
                filename = tempFilename;

            // These three lines are the only ones that do what the function's
            //  name says it does!
            stream = new FileOutputStream(filename);
            this.properties.store(stream, null);
            stream.close();

            if(tempFilename == null)
            {

                // Everything done successfully, mark as clean. Passing SCF_MARK
                //  prevents setClean from asking if a dirty file should be
                //  saved (which would end up recursively calling save())
                setClean(SCF_MARK);

                // If this was a new file, the selected properties file will be
                //  blank. Now that the new file is saved, and has a name,
                //  update the selected properties file to be that new name.
                //  Note: this cannot be done earlier, as changing the drop-down
                //  will perform a load, so the properties must be clean at this
                //  point
                if(newFile == true)
                {   // First, add the name to the properties..
                    // I cannot find a way to test if an item exists by name,
                    //  the only way I can see would be to iterate using
                    //  getItemCount() and getItemAt(index), so instead of doing
                    //  all that, just remove and add the item. If it doesn't
                    //  exist, remove will do nothing (hopefully?)
                    cboPropertiesFiles.removeItem(propertiesFileEx);
                    cboPropertiesFiles.addItem(propertiesFileEx);
                    // ..then select it; but only if nothing is selected. If
                    //  something was selected, it can only mean that the saving
                    //  of new file was in response to the user picking a
                    //  different properties file, and in that case, the picked
                    //  file should remain picked
                    if(cboPropertiesFiles.getSelectedItem() == null)
                        cboPropertiesFiles.setSelectedItem(propertiesFileEx);
                }

            }

            // Save successful. Note, if save failed or was cancelled, return
            //  statements exist within the function to handle these (this is
            //  not the only exit point!)
            return true;

        }
        catch(IOException e)
        {   GEVAMessage.showMessage
            (   this,
                I18N.get("ui.gui.prsa.err", e),
                GEVAMessage.ERROR_TYPE
            );
            e.printStackTrace();
            // Save failed. Note. if save was cancelled, false is returned too,
            //  but that happens in the code above (this is not the only false
            //  exit point!)
            return false;
        }

    }

    private String getActivePropertiesFile()
    {   String selectedPropertiesFile = null;

        // Get the selected properties file
        if(cboPropertiesFiles.getSelectedItem() != null)
            selectedPropertiesFile = cboPropertiesFiles
                .getSelectedItem()
                .toString();

        // The selected item won't equal the currently active item if this save
        //  was in response to the user changing the selected properties file,
        //  and being asked if they wanted to save changes to the old, so in
        //  that case, use the old properties file to do the save..

        // If an existing properties file is selected..
        if(selectedPropertiesFile != null
        // ..and the selected one is not the active one.. (it means the save
        //  occurred in response to the user switching to a different file)
        && selectedPropertiesFile.equals(this.propertiesFile) == false
        // ..or..
        // ..if a properties file is not picked..
        || selectedPropertiesFile == null
        // ..and there is an active properties file.. (it means the save
        //  occurred in response to the user clicking 'new')
        && this.propertiesFile != null)
            // ..then, set the to-be-saved properties file to the active one,
            //  (not the selected one)
            selectedPropertiesFile = this.propertiesFile;

        // activePropertiesFile can be null if the picked item and the active
        //  item are both null, meaning that the properties is for a new file
        return selectedPropertiesFile;

    }

    private void setActivePropertiesFile()
    {

        // Make the active properties file the same as the selected one
        if(cboPropertiesFiles.getSelectedItem() != null)
            this.propertiesFile = cboPropertiesFiles.getSelectedItem().toString();

    }

    private String getNewPropertiesFile()
    {   String newPropertiesFile = null;
        boolean tryAgain = true;
        boolean exists;

        // If the new name chosen by the user matches an existing file and the
        //  user chooses not to overwrite, loop this code to ask the user to
        //  enter a different name. Loop ends when a unique name is entered, the
        //  user chooses to overwrite a file using an existing name, or the user
        //  cancels the save. Note, if the user cancels, the loop exits by a
        //  return statement!
        while(tryAgain == true)
        {

            // Ask the user to type in a name, TODO update this to show a
            //  Save-as dialog, a save-as dialog that doesn't allow the user to
            //  switch directories, as all properties need to be in the same
            //  location
            newPropertiesFile = JOptionPane.showInputDialog
            (   this,
                I18N.get("ui.gui.prsa.msg"),
                newPropertiesFile
            );

            // User pressed cancel, don't save
            if(newPropertiesFile == null)
                return null;

            // Make sure the filename ends '.properties'
            if(newPropertiesFile.endsWith(Constants.txtPropertiesExt) == false)
                newPropertiesFile += Constants.txtPropertiesExt;

            // Check if a properties file with this specified name already
            //  exists
            exists = new File
            (   GEVAConfig.getPropertiesAbsPath() + newPropertiesFile
            ).exists();

            // Give the user the oppertunity to overwrite the file if it already
            //  exists, or to enter a different name, or cancel
            if(exists == true)
                switch(GEVAMessage.showMessage
                (   this,
                    I18N.get("ui.gui.prow.wrn"),
                    GEVAMessage.YES_NO_CANCEL_OPTION
                  | GEVAMessage.WARNING_TYPE
                )){ case GEVAMessage.YES_OPTION: tryAgain = false; break;
                    case GEVAMessage.NO_OPTION: /* tryAgain */ break;
                    case GEVAMessage.CANCEL_OPTION: return null;
                }
            else
                tryAgain = false;

        }

        return newPropertiesFile;

    }

    // TODO - this doesn't always delete the file; possibly because of file
    //  locks or something - find out what the dillio be. Currently it will show
    //  a message when it fails, but cannot say why it failed as File.Delete
    //  doesn't throw an exception, it just returns false. Doing the delete a
    //  second time, after a second or two, usually results in it then working
    public void deletePropertiesFile()
    {   File file = null;
        String propertiesFileEx = null;

        // Ask before physically deleting the properties file from disk
        if(cboPropertiesFiles.getSelectedItem() != null)
        {   propertiesFileEx = cboPropertiesFiles.getSelectedItem().toString();
            file = new File(GEVAConfig.getPropertiesAbsPath() + propertiesFileEx);
            if(GEVAMessage.showMessage
            (   this,
                I18N.get
                (   "ui.gui.prde.wrn",
                    GEVAConfig.getPropertiesAbsPath()
                  + propertiesFileEx
                ),
                GEVAMessage.OK_CANCEL_OPTION | GEVAMessage.WARNING_TYPE
            ) == GEVAMessage.CANCEL_OPTION)
                return;
        }
        else
        // If no properties file is selected, it means the user clicked new,
        //  then delete, so they are clearing the changes they made rather than
        //  actually deleting a properties file, so just warn that the changes
        //  will be lost
        if(isDirty == true)
            if(GEVAMessage.showMessage
            (   this,
                I18N.get("ui.gui.prlc.wrn"),
                GEVAMessage.OK_CANCEL_OPTION | GEVAMessage.WARNING_TYPE
            ) == GEVAMessage.CANCEL_OPTION)
                return;

        // If no file is selected, or a file is selected and deleted correctly
        if(propertiesFileEx == null || doDeletePropertiesFiles(file))
        {

            // Make sure the user isn't asked if they want to save the file
            //  before they delete it
            setClean(SCF_MARK);

            if(propertiesFileEx != null)
                cboPropertiesFiles.removeItem(propertiesFileEx);

            unloadPropertiesFile();

        }

    }

    private boolean doDeletePropertiesFiles(File file)
    {

        // Hack - attempt to unlock any open handles on the file before deleting.
        //  Some Internet people say this doesn't work, others say it does!
        //  Testing has found it doesn't work, but some Internet people said it
        //  does, and that's good enough for me
        System.gc();

        if(file != null && file.delete() == false)
        {   GEVAMessage.showMessage
            (   this,
                I18N.get("ui.gui.prde.err", file.getName()),
                GEVAMessage.ERROR_TYPE
            );
            return false;
        }

        return true;

    }

    /**
     * Mark the active properties file as modified. The inverse action is done
     *  in setClean(SCF_MARK) (under if(flag == SCF_MARK) block)
     */
    public void setDirty()
    {   cmdSave.setEnabled(true);
        this.isDirty = true;
        this.setTitle(I18N.get("ui.gui") + Constants.DIRTY_SYMBOL);
    }

    public void setDirtyable(boolean dirtyable)
    {   canDirty = dirtyable;
    }

    public boolean canDirty()
    {   return canDirty;
    }

    /**
     * Set the active properties file as clean (not modified). If this file is
     *  currently dirty (modified), the user is first prompted to save the file,
     *  to which they can respond yes (which will call save() and return true if
     *  the file saved successfully, else false), no (which will just return
     *  true), or cancel (which will just return false)
     */
    public boolean setClean()
    {   return setClean(SCF_PROMPT);
    }

    /**
     * Mark the active properties file as saved. If the properties is currently
     *  dirty, and <var>flag</var> is set to SCF_PROMPT, a dialog box will ask
     *  the user if they want to save first. If the user says yes, the file is
     *  saved, <var>isDirty</var> is set to false, and true is returned. If the
     *  user says no, the file is not saved, <var>isDirty</var> is remains true,
     *  and true is returned. If the user says cancel, the file is not saved,
     *  <var>isDirty</var> will remain at true, and false is returned. Also, if
     *  the file is saved, but fails, false is returned and <var>isDirty</var>
     *  remains at true. If <var>flag</var> is SCF_MARK, the file is NOT saved,
     *  but <var>isDirty</var> is set to false and true is returned
     * @param flag Specify how cleaning should perform. SCF_PROMPT, user should
     *  be asked if they want to save a dirty file. SCF_MARK, just mark the
     *  file as clean.
     * @return true if the properties was set to clean, or the user stated they
     *  didn't want the file to be cleaned, else false. If false is returned, it
     *  means the file was dirty, attempted to save, but failed or was cancelled,
     *  so it will remain in a dirty state. Basically, continue what you were
     *  doing if true is returned, stop what your doing if false is returned
     */
    private boolean setClean(int flag)
    {

        // If the file is clean, do nothing
        if(isDirty == false)
            return true;

        // If prompt for save
        if(flag == SCF_PROMPT)
            // Ask user if they want to save, yes/no/cancel
            switch
            (   GEVAMessage.showMessage
                (   this,
                    I18N.get("ui.gui.lodi.wrn"),
                    GEVAMessage.YES_NO_CANCEL_OPTION | GEVAMessage.WARNING_TYPE
                )
            ){  // Yes: return result of save. save calls setClean(SCF_MARK),
                //  so this will implicitly set isDirty to false if the file
                //  was saved successfully. save will return false if the file
                //  save failed and isDirty will remain true
                case GEVAMessage.YES_OPTION: return savePropertiesFile();
                // No: leave the file as is, without save and return true, save
                //  was successful, so to speak, just not done
                case GEVAMessage.NO_OPTION: return true;
                // Cancel: Leave isDirty unchanged and return false, didn't save
                case GEVAMessage.CANCEL_OPTION: return false;
            }
        else
        if(flag == SCF_MARK)
        {   // This is where the clean state is set, any actions that occur when
            //  a properties goes clean, should happen here only. The inverse
            //  action is done in setDirty()
            cmdSave.setEnabled(false);
            this.isDirty = false;
            this.setTitle(I18N.get("ui.gui"));
            return true;
        }

        // This code can only be reached if flag is an unknown value, or if
        //  prompting for save returned an unknown response; basically, it's a
        //  bug if this executes! So that all is not lost, acts as if Cancel was
        //  clicked.
        GEVAMessage.showMessage
        (   this,
            I18N.get("ui.unkn.bug"),
            GEVAMessage.ERROR_TYPE
        );
        return false;

    }

    /**
     * Display this GUI dialog
     * @return OK_OPTION if the OK button was clicked. CANCEL_OPTION if
     *  the cancel or window's X was clicked
     */
    private int showUI()
    {

        /* TODO - no dynamic GUI - discontinued
        if(GEVAControlLoader.loadUIConfig(this.configPath + "ui.config") == true)
        */
        setVisible(true);

        return resultOption;

    }

    ////////////////// Events //////////////////

    public void actionPerformed(ActionEvent event)
    {

        // ignore all events while initialising
        if(initialising == true)
            return;

        // Close the GUI, cancelling any changes (though user will be prompted
        //  to save before closing)
        if(event.getSource() == cmdClose)
            close();
        else
        // Setup a new properties file
        if(event.getSource() == cmdPropertiesFilesNew)
            newPropertiesFile();
        else
        // Delete the selected properties file
        if(event.getSource() == cmdPropertiesFilesDelete)
            deletePropertiesFile();
        else
        // Load a properties file when it's selected
        if(event.getSource() == cboPropertiesFiles)
        {   if(cboPropertiesFiles.getSelectedItem() != null)
                GEVAConfig.setSelectedPropertiesFile
                (   cboPropertiesFiles.getSelectedItem().toString()
                );
            loadPropertiesFile();
        }
        else
        // Save any changes and leave the GUI open
        if(event.getSource() == cmdSave)
            savePropertiesFile();
        else
        // Run the selected properties file
        if(event.getSource() == cmdRun)
            runPropertiesFile();
        else
        // Display a configuration dialog that is a replacement for using
        //  command-line switches
        if(event.getSource() == cmdConfig)
            configure(false);
        // If this method was called by some control that hasn't been given
        //  an action to perform, pretend that it's not because of a bug, but is
        //  instead a feature that hasn't been implemented yet
        else
            GEVAMessage.showMessage
            (   this,
                I18N.get("ui.err.main.noim", event.getActionCommand())
            );

    }

    public void close()
    {   windowClosing(null);
    }

    /**
     * Display the configuration dialog, and, if changes were made that need a
     *  reset to apply, ask the user if they want to reset now. Reset is
     *  a little hack I added because I'm too lazy to clean-up and re-initialise
     *  properly, what with all those GUI things and what-not
     * @param force If a reset is required, true will cause the reset to occur
     *  without asking the user if it's ok. This is used during startup, before
     *  the main window is displayed, if an error occurred reading the config
     * @return true if the program should continue, false if the program stop as
     *  a reset has been initiated. This is used during startup, before the main
     *  window is displayed, so that it won't be displayed as a new instance is
     *  being created
     */
    boolean configure(boolean force)
    {   GEVAConfigGUI config = new GEVAConfigGUI(this);
        if(config.forceReset() == true)
        {   reset();
            return false;
        }
        else
        if(config.needReset() == true)
            if(force == true)
            {   reset();
                return false;
            }
            else
            if(GEVAMessage.showMessage
            (   this,
                I18N.get("ui.gui.rset.wrn"),
                GEVAMessage.YES_NO_OPTION
            ) == GEVAMessage.YES_OPTION)
            {   reset();
                return false;
            }
        return true;
    }

    /**
     * Reset by creating a new instance of the GUI and disposing the old, and
     *  cleaning up any static arrays
     */
    private void reset()
    {   if(setClean() == true)
            if(GEVARunning.closeAll() == true)
            {   GEVAGraphPaneConfig.reset();
                dispose();
                new GEVAPropertiesGUI();
            }
    }

    public void windowClosing(WindowEvent event)
    {
        // Only do the actual close operation if the active properties file is
        //  clean, and if all GEVA runs have been stopped
        if(setClean() == true)
            if(GEVARunning.closeAll() == true)
            {   GEVAConfig.saveQuiet();
                System.exit(0);
            }

    }
    public void windowActivated(WindowEvent event) { }
    public void windowClosed(WindowEvent event) { }
    public void windowDeactivated(WindowEvent event) { }
    public void windowDeiconified(WindowEvent event) { }
    public void windowIconified(WindowEvent event) { }
    public void windowOpened(WindowEvent event) { }

    public void mouseEntered(MouseEvent e)
    {   if(isDirty == false)
            cmdSave.setEnabled(true);
    }
    public void mouseExited(MouseEvent e)
    {   if(isDirty == false)
            cmdSave.setEnabled(false);
    }
    public void mouseClicked(MouseEvent e) { }
    public void mousePressed(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }

    /**
     * GEVA action performed. Called when GEVAControl's issue events
     */
    public boolean actionPerformed(GEVAActionEvent event)
    {   String grammar;
        int slash;

        switch(event.getAction())
        {

            // If a field is modified..
            case GEVAActionEvent.DIRTY:

		//		System.out.println
		//    (   event.getSource().getTitle() + ", "
		//	+ event.getAction() + ", "
		//	+ event.getActionString() + ", "
		//	+ event.getActionCommand()
		//	);

                // If the modified field is the fitness function
                //  Update the grammar to match the default for that function
                if(event.getSource() == guiFitnessFunctions)
                {   fitness = this.fitnessDetails.get(event.getActionString());
                    guiGrammarFiles.setText(fitness.grammarFile);
		    //If it is 
                }
		// If field is replacement operation and steady state
		// set the elites to 0
                if(event.getSource() == guiReplacementType)
                {   
		    if(guiReplacementType.getText().equals("steady_state")) {
			guiEliteSize.setText("0");
		    }
                }
                break;

            // If a field is being loaded from the properties file..
            case GEVAActionEvent.LOAD:

                // If the field being loaded is the grammar files
                //  Strip path information, leaving just filename
                if(event.getSource() == guiGrammarFiles)
                    if((grammar = event.getActionString()) != null)
                    {   grammar = grammar.replace('\\', '/');
                        if((slash = grammar.lastIndexOf('/')) != -1
                        || (slash = grammar.lastIndexOf(':')) != -1)
                            event.setActionString(grammar.substring(slash + 1));
                    }
                return true;

            // If a field is being saved to a properties file..
            case GEVAActionEvent.SAVE:

                // If the field being saved is the grammar files
                //  Restore path information based on grammar path (not path
                //  that was stripped from grammar during load (see above))
                if(event.getSource() == guiGrammarFiles)
                    event.setActionString
                    (   GEVAConfig.getGrammarRelPath() + event.getActionString()
                    );
                return true;

            // If a field is being validated..
            case GEVAActionEvent.VALID:

                // As part of validating population size, also force a
                //  validation of elite and tournament size
                if(event.getSource() == guiPopulationSize)
                {   guiEliteSize.validate();
                    guiTournamentSize.validate();
                    guiPickSize.validate();
                }
                else
                // Custom validation for elite and tournament size
                //  Make sure they are not greater than population size
                //  This validation will also occur when population size changes
                //   because it is forced (above)
                if(event.getSource() == guiEliteSize
                || event.getSource() == guiTournamentSize
                || event.getSource() == guiPickSize)
                {   try
                    {   if(Integer.parseInt(event.getActionString())
                         > Integer.parseInt(guiPopulationSize.getText()))
                        {   event.addInvalidReason(I18N.get
                            (   "ui.ctl.main.less.err",
                                guiPopulationSize.getText()
                            ));
                            return false;
                        }
                    }
                    catch(NumberFormatException e)
                    {   event.addInvalidReason(I18N.get("ui.ctl.main.pars.err"));
                        return false;
                    }
                    return true;
                }
                break;
        }

        return true;

    }

    ////////////////// Main //////////////////

    public static void main(String[] args)
    {   // Attempt to prevent any exception messages disappearing into a console,
        //  that may not even be visible given that the UI could be started from
        //  the OS shell
        Thread.setDefaultUncaughtExceptionHandler
        (   new GEVAUncaught.GEVAGlobalUncaught()
        );
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        if(GEVAConfig.initialise(args) == true)
            new GEVAPropertiesGUI();
    }

}
