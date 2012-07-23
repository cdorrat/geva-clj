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

import geva.gui.Util.I18N;

import java.util.Properties;
import java.awt.Component;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.util.regex.Pattern;

/**
 * Input control that takes any value.
 * Type can be "rout" which creates a read-only text area for piped output of
 *  external program - hacked in, should probably make a generic 'area' type
 *  with read-only flag or something. TODO - Currently (2008y08M23d) doesn't
 *  support events
 * @author eliottbartley
 */
public class      GEVAStringProperty
       extends    GEVAPropertyControl
       implements DocumentListener
{

    /**
     * Constructor <var>type</var>. Read and write to control
     */
    public static String PT_READWRITE = "";

    /**
     * Constructor <var>params</var>. No validation
     */
    public static String PP_ACCEPT_ALL = "";

    private JTextField txtValue;
    private JTextArea strValue;

    /**
     * Create a text input with regular express validation
     * @param dirtyListener GUI that listens to dirty events
     * @param parent Container for this control
     * @param type Can be "rout" or "". "rout" is a quick hack which was added
     *  to allow it to show read-only console output and has since been replaced
     *  by other means (so "rout" is currently unused (2008r07M11d))
     * @param title The title to show in the GUI of this control
     * @param name The name used when saving this to the properties file
     * @param comment A tooltip
     * @param initial The initial value
     * @param params Regular expression used to validate, and a mismatch error
     *  message in the format "regex,error". During validation, the input string
     *  is matched against the regular expression. If it mismatches, 'error' is
     *  output as the error reason. If 'error' is not specified, the message,
     *  "Value mismatched pattern 'regex'" is output (where 'regex' is the regex
     *  expression entered). If 'regex' is not specified, no regular expression
     *  validation is done
     */
    public GEVAStringProperty
    (   GEVADirtyListener     dirtyListener,
        GEVAPropertyContainer parent,
        String                type,
        String                title,
        String                name,
        String                comment,
        String                initial,
        String                params
    ){  super(dirtyListener, parent, type, title, name, comment, initial, params);
        if(isType("rout") == true)
        {   strValue = new JTextArea();
            strValue.setEditable(false);
        }
        else
        {   txtValue = new JTextField();
            txtValue.getDocument().addDocumentListener(this);
        }
        parent.add(this);
        super.dirtyListener.setDirtyable(false);
        setText(initial);
        validate();
        super.dirtyListener.setDirtyable(true);
    }

    /**
     * Get the text displayed in the text box
     */
    public String getText()
    {   return (txtValue != null ? txtValue : strValue).getText();
    }

    /**
     * Set the text displayed in the text box
     */
    public void setText(String text)
    {   (txtValue != null ? txtValue : strValue).setText(text);
    }

    /**
     * Append to the text displayed in the text box
     */
    public void addText(String text)
    {   if(txtValue != null)
            txtValue.setText(txtValue.getText() + text);
        else
            strValue.append(text);
    }

    public void setEnabled(boolean enabled)
    {   (txtValue != null ? txtValue : strValue).setEnabled(enabled);
    }

    public boolean load(Properties properties)
    {   if(name == null)
            return true;
        (txtValue != null ? txtValue : strValue).setText
        (   properties.getProperty(name, initial)
        );
        return valid();
    }

    public boolean save(Properties properties)
    {   if(name == null)
            return true;
        properties.setProperty
        (   name,
            (txtValue != null ? txtValue : strValue).getText().toString()
        );
        return valid();
    }

    @Override
    public Component getComponent(int index)
    {   if(index == 1) return (txtValue != null ? txtValue : strValue);
        return super.getComponent(index);
    }

    public void insertUpdate(DocumentEvent event)
    {   this.dirtyListener.setDirty();
        validate();
    }
    public void removeUpdate(DocumentEvent event)
    {   this.dirtyListener.setDirty();
        validate();
    }
    public void changedUpdate(DocumentEvent event)
    {   this.dirtyListener.setDirty();
        validate();
    }

    @Override
    public void validate()
    {   if(valid() == true)
            (txtValue != null ? txtValue : strValue).setBackground(Color.white);
        else
            (txtValue != null ? txtValue : strValue).setBackground(Color.red);
    }

    public boolean valid()
    {   resetInvalidReason();
        if(super.getParam(0, null) != null)
            if(Pattern.matches(super.getParam(0, ".*"), getText()) == false)
            {   addInvalidReason(super.getParam
                (   1,
                    I18N.get("ui.ctl.strg.miss.err", super.getParam(0, ""))
                ));
                return false;
            }
        return true;
    }

}
