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

import geva.gui.Util.FileNameExtensionFilter;
import geva.gui.Util.GEVAHelper;
import geva.gui.Util.I18N;
import geva.gui.Util.PathTools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Input that allows files or directories to be chosen
 * @author eliott bartley
 */
public class GEVAFileProperty
     extends GEVAPropertyControl
  implements DocumentListener
{

    private static class Filters extends ArrayList<FileNameExtensionFilter>
    {   public Filters(int size) { super(size); }
    }

    /**
     * Constructor <var>type</var>. Accepts files only
     */
    public static String PT_FILE = "";
    /**
     * Constructor <var>type</var>. Accepts folders only
     */
    public static String PT_FOLDER = "folder";
    /**
     * Constructor <var>type</var>. Accepts files or folders
     */
    public static String PT_FILE_OR_FOLDER = "any";

    /**
     * Constructor <var>params</var>. Path names are to be output as relative to
     *  working path (see setWorkingPath). This must be given as the second
     *  value in params (this is the default)
     */
    public static String PP_RELATIVE = "";
    /**
     * Constructor <var>params</var>. Path names are to be output as absolute.
     *  This must be given as the second value in params
     */
    public static String PP_ABSOLUTE = "absolute";
    /**
     * Constructor <var>params</var>. No validation is done to see if the file
     *  or folder specified actually exists. This must be given as the third
     *  value in params (this is the default)
     */
    public static String PP_MUST_EXIST = "";
    /**
     * Constructor <var>params</var>. Validation is done to see if the file or
     *  folder specified actually exists. This must be given as the third value
     *  in params
     */
    public static String PP_IGNORE_EXIST = "ignorexist";

    private JPanel guiPane;
    private JTextField txtValue;
    private JButton cmdBrowse;
    // When converting absolute into relative, relative will be from this path
    private String workingPath;
    // Flag whether control should output absolute or relative paths
    private boolean absolute;
    // Flag whether control will validate that the file or path exists
    private boolean mustExist;
    // When clicking browse, keeps the filters used in file types
    private Filters filters;
    // If an all files filter exists, flag it so the dialogs internal all files
    //  can be used
    private boolean allFiles;
    // What type of files can be picked, file, folders, or files-and-folders
    private int pick;

    /**
     * Create a text input and file browser button with file exists validation
     * @param dirtyListener GUI that listens to dirty events
     * @param parent Container for this control
     * @param type Can be PP_FILE, PP_FOLDER or PP_FILE_OR_FOLDER and states
     *  what the
     * @param title The title to show in the GUI of this control
     * @param name The name used when saving this to the properties file
     * @param comment A tooltip
     * @param initial The initial value
     * @param params "working_folder, absolute, mustExist, filter" where
     *  working_folder is a string of the root directory from which relative
     *  paths are resolved and defaults to the current directory if not
     *  speicified. absolute is a boolean which states whether the pathname will
     *  be output as a relative or absolute path (false or true respectively)
     *  (default false (relative)), mustExist is a boolean which states whether
     *  the result must be to an existing file or directory (default true (must
     *  exist)), and filter states the file types that are filtered, in the
     *  format "desc|ex1[;ex2;...][|desc2|ex21[;ex22;...][|...|...]]",
     *  e.g. "Pictures|jpeg;jpg;png;bmp|Videos|mpeg;mpg;avi". If no extension is
     *  given, e.g. "All files|", then all files are output for that filter
     */
    public GEVAFileProperty
    (   GEVADirtyListener     dirtyListener,
        GEVAPropertyContainer parent,
        String                type,
        String                title,
        String                name,
        String                comment,
        String                initial,
        String                params
    ){  super(dirtyListener, parent, type, title, name, comment, initial, params);
        guiPane = new JPanel();
        guiPane.setLayout(new BorderLayout());
        txtValue = new JTextField();
        txtValue.getDocument().addDocumentListener(this);
        txtValue.addFocusListener(new FocusListener()
        {   public void focusGained(FocusEvent e) { }
            public void focusLost(FocusEvent e)
            {   txtValue_onUpdate();
            }
        });
        cmdBrowse = new JButton(I18N.get("ui.more"));
        cmdBrowse.addActionListener(new ActionListener()
        {   public void actionPerformed(ActionEvent e)
            {   cmdBrowse_onClick();
            }
        });
        guiPane.add(txtValue, BorderLayout.CENTER);
        guiPane.add(cmdBrowse, BorderLayout.EAST);
        if(isType(PT_FOLDER) == true)
            pick = JFileChooser.DIRECTORIES_ONLY;
        else
        if(isType(PT_FILE_OR_FOLDER) == true)
            pick = JFileChooser.FILES_AND_DIRECTORIES;
        else
            pick = JFileChooser.FILES_ONLY;
        workingPath = super.getParam(0);
        absolute = super.getParamEqual(1, PP_ABSOLUTE);
        mustExist = !super.getParamEqual(2, PP_IGNORE_EXIST);
        parseExtensions(super.getParam(3));
        parent.add(this);
        super.dirtyListener.setDirtyable(false);
        setText(initial);
        validate();
        super.dirtyListener.setDirtyable(true);
    }

    /**
     * Given a list of file types in the format..
     *
     *  <filterlist> ::= <filter>
     *                 | <filter> "|" <filterlist>
     *  <filter> ::= <description> "|" <extensionlist>
     *             | <description> "|" <allfiles>
     *  <description> ::= <!--Freetext to display to user, e.g. 'Image files'-->
     *  <extensionlist> ::= <extension>
     *                    | <extension>;<extensionlist>
     *  <extension> ::= <!--Filename extension without period, e.g. 'jpg'-->
     *  <allfiles> ::= "" <!--Empty string-->
     *
     *  ..populate the <var>filters</var> array with all file types and their
     *  extensions
     * @param filterList List of file types, e.g.
     *  "All files||Image files|jpg;jpeg;gif;png"
     */
    private void parseExtensions(String filterList)
    {   String[] parseFilters;
        String[] extensions = null;

        if(filterList == null)
        {   allFiles = true;
            return;
        }

        parseFilters = filterList.split("\\|");
        if(parseFilters.length == 0)
        {   allFiles = true;
            return;
        }

        filters = new Filters(parseFilters.length / 2);
        for(int i = 0; i < parseFilters.length; i += 2)
        {

            if(i + 1 < parseFilters.length)
            {   extensions = GEVAHelper.prune(parseFilters[i + 1].split(";"));
                if(extensions.length == 0)
                    extensions = null;
            }
            else
                extensions = null;

            if(extensions != null)
                filters.add(new FileNameExtensionFilter
                (   parseFilters[i],
                    extensions
                ));
            else
                allFiles = true;

        }

    }

    @Override
    public String getText()
    {   if(mustExist == false
        && txtValue.getText().trim().length() == 0)
            return "";
        else
        if(absolute == true)
            return PathTools.getAbsolutePath(txtValue.getText(), workingPath);
        else
            return PathTools.getRelativePath(txtValue.getText(), workingPath);
    }

    /**
     * Get the absolute path value regardless to whether this control displays
     *  in absolute or relative mode (which getText() would be affected by)
     */
    public String getAbsoluteText()
    {   return PathTools.getAbsolutePath(txtValue.getText(), workingPath);
    }

    @Override
    public void setText(String text)
    {   if(mustExist == false
        && text.trim().length() == 0)
            txtValue.setText("");
        else
        if(absolute == true
        || new File(PathTools.getAbsolutePath
        (   text,
            workingPath
        )).exists() == false)
            txtValue.setText(PathTools.getAbsolutePath(text, workingPath));
        else
            txtValue.setText(PathTools.getRelativePath(text, workingPath));
    }

    @Override
    public boolean load(Properties properties)
    {   if(name == null)
            return  true;
        String value = properties.getProperty(name, initial);
        GEVAActionEvent event = new GEVAActionEvent
        (   this,
            GEVAActionEvent.LOAD,
            value,
            -1
        );
        if(fireActionEvent(event) == false)
            value = initial;
        else
        if(event.isDirty() == true)
            value = event.getActionString();
        setText(value);
        // If control is invisible, ignore its validness
        return valid() || txtValue.isVisible() == false;
    }

    @Override
    public boolean save(Properties properties)
    {   if(name == null)
            return true;
        String value;
        value = getText();
        GEVAActionEvent event = new GEVAActionEvent
        (   this,
            GEVAActionEvent.SAVE,
            value,
            -1
        );
        boolean write = fireActionEvent(event);
        if(event.isDirty() == true)
            value = event.getActionString();
        if(write == true)
            properties.setProperty(name, value);
        // If control is invisible, ignore its validness;
        return valid() || txtValue.isVisible() == false;
    }

    @Override
    public void setEnabled(boolean enabled)
    {   txtValue.setEnabled(enabled);
        cmdBrowse.setEnabled(enabled);
    }

    @Override
    public Component getComponent(int index)
    {   if(index == 1) return guiPane;
        return super.getComponent(index);
    }

    public boolean valid()
    {   boolean valid = true;
        File file = null;
        resetInvalidReason();

        // If the file doesn't need to exist, having no pathname specified is ok
        if(mustExist == false
        && getText().length() == 0)
            return true;

        // If the file does need to exist, or must be a file/folder, prepare to
        //  do the validation
        if(mustExist == true
        || pick == JFileChooser.FILES_ONLY
        || pick == JFileChooser.DIRECTORIES_ONLY)
            file = new File(getAbsoluteText());

        // If the file must exist, and it does not, it's an error
        if(mustExist == true && file.exists() == false)
            valid = addInvalidReason(I18N.get("ui.ctl.file.path.err"));
        else
        // If the file needs to be an actual file, and it's not, and it must
        //  exist or it does exist, it's an error. A break-down of this
        //  condition is..
        //  Must this be a file? (pick == JFileChooser.FILES_ONLY)
        //   Yes: it must be, check if it is, continue
        //   No: it doesn't need to be a file, don't output an error
        //  Is it a file? (file.isFile() == false)
        //   No: it's suppose to be a file, it's not, continue
        //   Yes: it's suppose to be a file, it is, don't output an error
        //  It must exist | does exist? (mustExist==true || file.exists()==true)
        //   Yes | Yes: It does exist, but it's not a file : error!
        //   Yes | No: This state is impossible here, as it's already been
        //              handled in the else condition before this one
        //   No | Yes: It does exist, but it's not a file : error!
        //   No | No: It doesn't exist, so the reason it's not a file is because
        //             of just that, it doesn't exist. It doesn't have to exist
        //             either, so no error message is shown
        if(pick == JFileChooser.FILES_ONLY && file.isFile() == false
        && (mustExist == true || file.exists() == true))
            valid = addInvalidReason(I18N.get("ui.ctl.file.file.err"));
        else
        // Same a the file check above, except for directories - If it must be a
        //  directory and it's not and it must exist and it does, it's an error
        if(pick == JFileChooser.DIRECTORIES_ONLY && file.isDirectory() == false
        && (mustExist == true || file.exists() == true))
            valid = addInvalidReason(I18N.get("ui.ctl.file.fold.err"));

        return eventFireAndSet(GEVAActionEvent.VALID) & valid;

    }

    @Override
    public void validate()
    {   if(valid() == true)
            txtValue.setBackground(Color.white);
        else
            txtValue.setBackground(Color.red);
    }

    public void changedUpdate(DocumentEvent e)
    {   if(super.dirtyListener.canDirty() == true
        && eventFireAndSet(GEVAActionEvent.DIRTY) == true)
            super.dirtyListener.setDirty();
        validate();
    }

    public void insertUpdate(DocumentEvent e)
    {   if(super.dirtyListener.canDirty() == true
        && eventFireAndSet(GEVAActionEvent.DIRTY) == true)
            super.dirtyListener.setDirty();
        validate();
    }

    public void removeUpdate(DocumentEvent e)
    {   if(super.dirtyListener.canDirty() == true
        && eventFireAndSet(GEVAActionEvent.DIRTY) == true)
            super.dirtyListener.setDirty();
        validate();
    }

    private boolean eventFireAndSet(int action)
    {   String invalidReason;
        GEVAActionEvent event = new GEVAActionEvent
        (   this,
            action,
            getText(),
            -1
        );
        boolean result = fireActionEvent(event);
        if(event.isDirty() == true)
            setText(event.getActionString());
        if(action == GEVAActionEvent.VALID
        && (invalidReason = event.getInvalidReason()) != null)
            addInvalidReason(invalidReason);
        return result;
    }

    /**
     * Make the path listed relative to a particular working path. If working
     *  path is set to null, the path will be relative to the current directory.
     * @param workingPath
     */
    public void setWorkingPath(String workingPath)
    {   String absolutePath;
        // Make sure it's absolute before chaning the working path, so that it
        //  can be resolved using the new workingPath
        absolutePath = getAbsoluteText();
        this.workingPath = workingPath;
        setText(absolutePath);
        validate();
    }

    public String getWorkingPath()
    {   return workingPath;
    }

    /**
     * When focus leaves the text input, reformat it
     */
    private void txtValue_onUpdate()
    {   // This is a helper that automatically converts from relative to
        //  absolute or whatever, but bascially doesn't change the meaning of
        //  the value when it does this, so it's exempt from making things dirty
        super.dirtyListener.setDirtyable(false);
        setText(getText());
        validate();
        super.dirtyListener.setDirtyable(true);
    }

    /**
     * If browse button clicked, browse
     */
    private void cmdBrowse_onClick()
    {   JFileChooser fileBrowser = new JFileChooser();
        fileBrowser.setAcceptAllFileFilterUsed(allFiles);
        fileBrowser.setCurrentDirectory(new File(getAbsoluteText()));
        fileBrowser.setFileSelectionMode(pick);
        if(filters != null)
            for(FileNameExtensionFilter filter : filters)
                fileBrowser.addChoosableFileFilter(filter);
        if(fileBrowser.showOpenDialog(guiPane) == JFileChooser.APPROVE_OPTION)
            setText(fileBrowser.getSelectedFile().getAbsolutePath());
    }

}
