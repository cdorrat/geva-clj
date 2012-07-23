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

import geva.gui.Util.Constants;
import geva.gui.Util.GEVAHelper;
import geva.gui.Util.I18N;
import geva.gui.Util.PathTools;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * Allow the config to be set up by a GUI, rather than just command line
 * @author eliott bartley
 */
public class GEVAConfigGUI
     extends JDialog
  implements GEVADirtyListener,
             WindowListener,
             MouseListener,
             GEVAActionListener
{

    /**
     * During setClean(), clean by saving (after prompting user if they want to)
     */
    private static final int SCF_PROMPT = 0;
    /**
     * During setClean(), clean by just marking as such and disabling apply
     */
    private static final int SCF_MARK = 1;

    private static final int NRF_LOCK = -1;
    private static final int NRF_NO = 0;
    private static final int NRF_WARN = 1;
    private static final int NRF_YES = 2;
    private static final int NRF_FORCE = 3;

    private GEVABookContainer guiProperties;
    private JButton cmdReset = new JButton(I18N.get("ui.cfg.ctrl.rset"));
    private JButton cmdOk = new JButton(I18N.get("ui.ctrl.okay"));
    private JButton cmdApply = new JButton(I18N.get("ui.ctrl.aply"));
    private JButton cmdCancel = new JButton(I18N.get("ui.ctrl.exit"));
    private GEVAFileProperty guiPropertiesPath;
    private GEVAFileProperty guiGrammarPath;
    private GEVAFileProperty guiConfigPath;
    private GEVAFileProperty guiClassPath;
    private GEVAFileProperty guiWorkingAbsPath;
    private boolean isDirty = false;
    private boolean canDirty = true;
    private int needRestart = NRF_NO;

    public GEVAConfigGUI(Dialog owner)
    {   super(owner, I18N.get("ui.cfg"));
        init();
    }

    public GEVAConfigGUI(Frame owner)
    {   super(owner, I18N.get("ui.cfg"));
        init();
    }

    private void init()
    {   super.setModal(true);
        super.setBounds
        (   super.getOwner().getX() + super.getOwner().getWidth() / 2 - 200,
            super.getOwner().getY() + super.getOwner().getHeight() / 2 - 250,
            400,
            500
        );
        super.getContentPane().setLayout(new BorderLayout());
        super.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        super.addWindowListener(this);

        cmdReset.addActionListener(new ActionListener()
        {   public void actionPerformed(ActionEvent e)
            {   cmdReset_onClick();
            }
        });
        cmdOk.addActionListener(new ActionListener()
        {   public void actionPerformed(ActionEvent e)
            {   cmdOk_onClick();
            }
        });
        cmdApply.addActionListener(new ActionListener()
        {   public void actionPerformed(ActionEvent e)
            {   cmdApply_onClick();
            }
        });
        cmdApply.addMouseListener(this);
        cmdCancel.addActionListener(new ActionListener()
        {   public void actionPerformed(ActionEvent e)
            {   cmdCancel_onClick();
            }
        });

        cmdOk.setEnabled(false);
        cmdApply.setEnabled(false);

        JPanel guiCommand = new JPanel();
        JPanel guiSeparator = new JPanel();
        guiCommand.setLayout(new GridBagLayout());

        guiCommand.add(cmdReset);
        guiCommand.add(cmdOk);
        guiCommand.add(cmdApply);
        guiCommand.add(cmdCancel);

        GEVAHelper.gridAdd(guiCommand, cmdReset,     0, 0, 0);
        GEVAHelper.gridAdd(guiCommand, guiSeparator, 1, 0, 1);
        GEVAHelper.gridAdd(guiCommand, cmdOk,        2, 0, 0);
        GEVAHelper.gridAdd(guiCommand, cmdApply,     3, 0, 0);
        GEVAHelper.gridAdd(guiCommand, cmdCancel,    4, 0, 0);

        initialiseProperties();

        super.add(guiProperties.getComponent(), BorderLayout.CENTER);
        super.add(guiCommand, BorderLayout.SOUTH);

        super.setVisible(true);

    }

    private void initialiseProperties()
    {

        guiProperties = new GEVABookContainer(this, I18N.get("ui.cfg.tip"));

        GEVAPageContainer guiPageGeva = new GEVAPageContainer
        (   this,
            guiProperties,
            I18N.get("ui.cfg.geva"),
            I18N.get("ui.cfg.geva.tip")
        );

        GEVAPropertyRowContainer container = new GEVAPropertyRowContainer
        (   this,
            guiPageGeva,
            I18N.get("ui.cfg.geva.path"),
            I18N.get("ui.cfg.geva.path.tip")
        );

        guiPropertiesPath = new GEVAFileProperty
        (   this,
            container,
            GEVAFileProperty.PT_FOLDER,
            I18N.get("ui.cfg.geva.path.prop"),
            Constants.cfgPropertiesPath,
            I18N.get("ui.cfg.geva.path.prop.tip"),
            GEVAConfig.getPropertiesRelPath(),
            GEVAConfig.getWorkingAbsPath()
        );
        guiPropertiesPath.addActionListener(this);
        guiGrammarPath = new GEVAFileProperty
        (   this,
            container,
            GEVAFileProperty.PT_FOLDER,
            I18N.get("ui.cfg.geva.path.gram"),
            Constants.cfgGrammarPath,
            I18N.get("ui.cfg.geva.path.gram.tip"),
            GEVAConfig.getGrammarRelPath(),
            GEVAConfig.getWorkingAbsPath()
        );
        guiGrammarPath.addActionListener(this);

        container = new GEVAPropertyRowContainer
        (   this,
            guiPageGeva,
            I18N.get("ui.cfg.geva.uipa"),
            I18N.get("ui.cfg.geva.uipa.tip")
        );

        guiConfigPath = new GEVAFileProperty
        (   this,
            container,
            GEVAFileProperty.PT_FOLDER,
            I18N.get("ui.cfg.geva.uipa.conf"),
            Constants.cfgConfigPath,
            I18N.get("ui.cfg.geva.uipa.conf.tip"),
            GEVAConfig.getConfigRelPath(),
            PathTools.getAbsolutePath(".")
        );
        guiConfigPath.addActionListener(this);

        container = new GEVAPropertyRowContainer
        (   this,
            guiPageGeva,
            I18N.get("ui.cfg.advn"),
            I18N.get("ui.cfg.advn.tip")
        );

        new GEVAStringProperty
        (   this,
            container,
            GEVAStringProperty.PT_READWRITE,
            I18N.get("ui.cfg.advn.main"),
            Constants.cfgClassName,
            I18N.get("ui.cfg.advn.main.tip"),
            GEVAConfig.getClassName(),
            GEVAStringProperty.PP_ACCEPT_ALL
        );
        guiClassPath = new GEVAFileProperty
        (   this,
            container,
            GEVAFileProperty.PT_FILE_OR_FOLDER,
            I18N.get("ui.cfg.advn.clpa"),
            Constants.cfgClassPath,
            I18N.get("ui.cfg.advn.clpa.tip"),
            GEVAConfig.getClassRelPath(),
            GEVAConfig.getWorkingAbsPath() + ",,,All Files||JAR files|jar"
        );
        guiWorkingAbsPath = new GEVAFileProperty
        (   this,
            container,
            GEVAFileProperty.PT_FOLDER,
            I18N.get("ui.cfg.advn.wkpa"),
            Constants.cfgWorkingAbsPath,
            I18N.get("ui.cfg.advn.wkpa.tip"),
            GEVAConfig.getWorkingAbsPath(),
            "," + GEVAFileProperty.PP_ABSOLUTE
        );
        guiWorkingAbsPath.addActionListener(this);

        new GEVAStringProperty
        (   this,
            container,
            GEVAStringProperty.PT_READWRITE,
            I18N.get("ui.cfg.advn.java"),
            Constants.cfgJavaName,
            I18N.get("ui.cfg.advn.java.tip"),
            GEVAConfig.getJavaName(),
            GEVAStringProperty.PP_ACCEPT_ALL
        );

        new GEVASpringContainer(this, guiPageGeva);

    }

    public void setDirty()
    {   if(canDirty == false)
            return;
        cmdOk.setEnabled(true);
        cmdApply.setEnabled(true);
        cmdCancel.setText(I18N.get("ui.ctrl.canl"));
        isDirty = true;
        this.setTitle
        (   I18N.get("ui.cfg") + Constants.DIRTY_SYMBOL
        );
    }

    public void setDirtyable(boolean dirtyable)
    {   canDirty = dirtyable;
    }

    public boolean canDirty()
    {   return canDirty;
    }

    /**
     * Save the configuration details after prompting the user if they want to
     *  save. If the user cancels, or the save fails, false is returned and
     *  isDirty remains true. If the user says no, true is returned and isDirty
     *  remains true. If the user says yes, and save doesn't fail, true is
     *  returned and isDirty is set to false
     */
    public boolean setClean()
    {   return setClean(SCF_PROMPT);
    }

    /**
     * Put the configuration dialog into a clean state by either saving any
     *  changes, or by simply flagging the changes as clean. Note. this does not
     *  actually do a save, it mearly attempts to put the state into a clean one,
     *  so basically, it either marks the state as clean, or asks the user what
     *  it should do--clean by saving, or ignore. To just do a save and clean
     *  the state, save should be called and if it succeedes, setClean(SCF_MARK)
     *  should then be called
     * @param flag SCF_MARK to just mark it as clean, SCF_PROMPT to promot the
     *  user if it should save (if it's actually dirty at the time)
     * @return true if it's ok to continue doing the action that required the
     *  configuration to be clean, false if the action should cancel. e.g. The
     *  user could say no to cleaning, but this will still return true, even
     *  though the config is still dirty, as the user said it was ok, so the
     *  calling code should continue as if it was clean. If the user said cancel
     *  however, it would mean it's not ok, and false is returned in this case
     */
    public boolean setClean(int flag)
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
                    I18N.get("ui.cfg.lodi.wrn"),
                    GEVAMessage.YES_NO_CANCEL_OPTION | GEVAMessage.WARNING_TYPE
                )
            ){  // Yes: return result of save. save calls setClean(SCF_FORCE),
                //  so this will implicitly set isDirty to false if the file
                //  was saved successfully. save will return false if the file
                //  save failed and isDirty will remain true
                case GEVAMessage.YES_OPTION: return saveConfigFile();
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
            cmdOk.setEnabled(false);
            cmdApply.setEnabled(false);
            cmdCancel.setText(I18N.get("ui.ctrl.exit"));
            this.isDirty = false;
            this.setTitle(I18N.get("ui.cfg"));
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

    private void cmdReset_onClick()
    {   needRestart = NRF_FORCE;
        close();
    }

    private void cmdOk_onClick()
    {   if(saveConfigFile() == true)
            close();
    }

    private void cmdApply_onClick()
    {   saveConfigFile();
    }

    private void cmdCancel_onClick()
    {   close();
    }

    private boolean saveConfigFile()
    {

        try
        {

            // Before actually saving, check that all the input parameters don't
            //  have any errors. If they do, warn the user and allow them to
            //  stop the save process - taken out because save is done anyway
            //  to save selected properties file, so there's no real cancelling
            if(guiProperties.save(GEVAConfig.getProperties()) == false)
                if(GEVAMessage.showMessage
                (   this,
                    I18N.get("ui.cfg.prsa.wrn"),
                    GEVAMessage.WARNING_TYPE | GEVAMessage.YES_NO_OPTION
                ) == GEVAMessage.NO_OPTION)
                {   // There were errors, user doesn't want to save those errors,
                    //  exit
                    return false;
                }

            GEVAConfig.save();
            // The changes that needed a restart are now set in stone, restart
            //  must occur
            if(needRestart == NRF_WARN)
                needRestart = NRF_YES;
            // save will throw if it fails, but if it succeeds, set it to clean
            setClean(SCF_MARK);
            return true;

        }
        catch(IOException e)
        {   GEVAMessage.showMessage
            (   this,
                I18N.get("ui.cfg.cosa.err", e),
                GEVAMessage.ERROR_TYPE
            );
            e.printStackTrace();
            return false;
        }

    }

    private void close()
    {   windowClosing(null);
    }

    public boolean actionPerformed(GEVAActionEvent e)
    {

        switch(e.getAction())
        {

            case GEVAActionEvent.VALID:
                if(e.getSource() == guiWorkingAbsPath)
                {   if(needRestart == NRF_NO)
                        needRestart = NRF_LOCK;
                    guiPropertiesPath.setWorkingPath(e.getActionString());
                    guiGrammarPath.setWorkingPath(e.getActionString());
                    guiClassPath.setWorkingPath(e.getActionString());
                    if(needRestart == NRF_LOCK)
                        needRestart = NRF_NO;
                    return true;
                }
                break;

            case GEVAActionEvent.DIRTY:
                if(needRestart == NRF_NO)
                    if(e.getSource() == guiPropertiesPath
                    || e.getSource() == guiGrammarPath
                    || e.getSource() == guiConfigPath)
                    {   needRestart = NRF_WARN;
                        return true;
                    }
                break;

        }

        return true;
    }

    /**
     * Return whether a configuration change requires a restart of the GUI for
     *  the changes to take place
     */
    public boolean needReset()
    {   return needRestart == NRF_YES || needRestart == NRF_FORCE;
    }

    public boolean forceReset()
    {   return needRestart == NRF_FORCE;
    }

    public void windowClosing(WindowEvent event)
    {   // Only do the actual close operation if the configuration file is clean
        if(setClean() == true)
            dispose();
    }
    public void windowActivated(WindowEvent event) { }
    public void windowClosed(WindowEvent event) { }
    public void windowDeactivated(WindowEvent event) { }
    public void windowDeiconified(WindowEvent event) { }
    public void windowIconified(WindowEvent event) { }
    public void windowOpened(WindowEvent event) { }

    public void mouseEntered(MouseEvent e)
    {   if(isDirty == false)
            cmdApply.setEnabled(true);
    }
    public void mouseExited(MouseEvent e)
    {   if(isDirty == false)
            cmdApply.setEnabled(false);
    }
    public void mouseClicked(MouseEvent e) { }
    public void mousePressed(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }

}
