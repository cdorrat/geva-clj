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
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.JFormattedTextField;
import java.awt.Color;
import java.text.DecimalFormatSymbols;

/**
 * A name value control that handles numeric inputs.
 * Type can be PT_INT or PT_FLOAT and validates that the value input can be
 *  converted to an int or double respectively. Also supports params of
 *  "increment,minimum,maximum", where fields can be left blank if not needed
 *  e.g. (",,10"). Default for unspecified fields are, increment: 1 for int, 0.1
 *  for float, minimum: -max value for type,  maximum: +max value for type.
 * @author eliottbartley
 */
public class      GEVANumberProperty
       extends    GEVAPropertyControl
       implements ChangeListener,
                  DocumentListener
{

    /**
     * Constructor <var>type</var>. Number must validate to type int
     */
    public static String PT_INT = "int";
    /**
     * Constructor <var>type</var>. Number must validate to type float
     */
    public static String PT_FLOAT = "float";

    //    private JTextField numValue;
    private JSpinner numValue;

    /**
     * Create a numeric input with spinner controls for mouse entry
     * @param dirtyListener GUI that listens to dirty events
     * @param parent Container for this control
     * @param type Can be PT_INT or PT_FLOAT. Used when validating the control,
     *  it is parsed to the specified type
     * @param title The title to show in the GUI of this control
     * @param name The name used when saving this to the properties file
     * @param comment A tooltip
     * @param initial The initial value
     * @param params Range and step amounts in the format "step,minimum,maximum".
     *  If minimum/maximum are not specified, the min/max of the type are used.
     *  If step is not speicified +1 is used for int and +0.1 is used for float.
     *  When the control's spinners are used, the number increments by 'step'
     *  amount and will not be allowed to go outside the range [minimum..maximum]
     *  inclusive. If the user types a value that is outside the minimum/maximum
     *  range, an error message is displayed
     */
    public GEVANumberProperty
    (   GEVADirtyListener     dirtyListener,
        GEVAPropertyContainer parent,
        String                type,
        String                title,
        String                name,
        String                comment,
        String                initial,
        String                params
    ){  super(dirtyListener, parent, type, title, name, comment, initial, params);
        if(isType(PT_INT) == true)
            initialiseInt(initial);
        else
        if(isType(PT_FLOAT) == true)
            initialiseFloat(initial);
        numValue.addChangeListener(this);
        JFormattedTextField textField = ((JSpinner.DefaultEditor)numValue
            .getEditor())
            .getTextField();
        textField.setHorizontalAlignment(JFormattedTextField.LEFT);
        textField.getDocument().addDocumentListener(this);
        parent.add(this);
    }

    private void initialiseInt(String initial)
    {   int value = Integer.parseInt(initial);
        int min = getParamInt(1, Integer.MIN_VALUE);
        int max = getParamInt(2, Integer.MAX_VALUE);
        int step = getParamInt(0, 1);
        numValue = new JSpinner(new SpinnerNumberModel(value, min, max, step));
        numValue.setEditor(new JSpinner.NumberEditor(numValue));
    }

    private void initialiseFloat(String initial)
    {   double value = Double.parseDouble(initial);
        double min = getParamDouble(1, Double.NEGATIVE_INFINITY);
        double max = getParamDouble(2, Double.POSITIVE_INFINITY);
        double step = getParamDouble(0, 0.1);
        numValue = new JSpinner(new SpinnerNumberModel(value, min, max, step));
        numValue.setEditor(new JSpinner.NumberEditor(numValue));
    }

    public void setEnabled(boolean enabled)
    {   numValue.setEnabled(enabled);
    }

    public boolean load(Properties properties)
    {   String value = properties.getProperty(name, initial).replace(",", "");
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
        return valid() || numValue.isVisible() == false;
    }

    public boolean save(Properties properties)
    {   String value;
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
        return valid() || numValue.isVisible() == false;
    }

    @Override
    public Component getComponent(int index)
    {   if(index == 1)
            return numValue;
        return super.getComponent(index);
    }

    public void stateChanged(ChangeEvent event)
    {   if(eventFireAndSet(GEVAActionEvent.DIRTY) == true)
            this.dirtyListener.setDirty();
        validate();
    }

    public void insertUpdate(DocumentEvent event)
    {   validate();
    }
    public void removeUpdate(DocumentEvent event)
    {   validate();
    }
    public void changedUpdate(DocumentEvent event)
    {   validate();
    }

    public boolean valid()
    {   boolean valid = true;
        resetInvalidReason();
        if(this.isType(PT_INT) == true)
        {   int value;
            try
            {   value = Integer.parseInt(getText());
                // validate the range, param 1 = min, param 2 = max
                if(this.getParam(1) != null && value < this.getParamInt(1))
                    valid = addInvalidReason(I18N.get
                    (   "ui.ctl.numb.less.err",
                        this.getParamInt(1)
                    ));
                if(this.getParam(2) != null && value > this.getParamInt(2))
                    valid = addInvalidReason(I18N.get
                    (   "ui.ctl.numb.more.err",
                        this.getParamInt(2)
                    ));
            }
            catch(NumberFormatException e)
            {   valid = addInvalidReason(I18N.get("ui.ctl.numb.bint.err"));
            }
        }
        else
        if(this.isType(PT_FLOAT) == true)
        {   double value;
            try
            {   // validate the content
                value = Double.parseDouble(getText());
                // validate the range, param 1 = min, param 2 = max
                if(this.getParam(1) != null && value < this.getParamDouble(1))
                    valid = addInvalidReason(I18N.get
                    (   "ui.ctl.numb.less.err",
                        this.getParamInt(1)
                    ));
                if(this.getParam(2) != null && value > this.getParamDouble(2))
                    valid = addInvalidReason(I18N.get
                    (   "ui.ctl.numb.more.err",
                        this.getParamInt(2)
                    ));
            }
            catch(NumberFormatException e)
            {   valid = addInvalidReason(I18N.get("ui.ctl.numb.bflt.err"));
            }
        }
        else
            valid = false;

        return eventFireAndSet(GEVAActionEvent.VALID) & valid;

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

    public void validate()
    {   if(valid() == true)
           ((JSpinner.DefaultEditor)numValue.getEditor())
               .getTextField()
               .setBackground(Color.white);
        else
           ((JSpinner.DefaultEditor)numValue.getEditor())
               .getTextField()
               .setBackground(Color.red);
    }

    // Get text gets the real-time modified version of the text, not the
    //  spinner's stored value - this is so that the validation can check
    //  against a value as it's being modified, for real-time feedback of errors
    public String getText()
    {   String text =
            ((JSpinner.DefaultEditor)numValue.getEditor())
            .getTextField()
            .getText();
        DecimalFormatSymbols symbols =
            ((JSpinner.NumberEditor)numValue.getEditor())
            .getFormat()
            .getDecimalFormatSymbols();
        // Get the symbols for the current locale
        char groupSep = symbols.getGroupingSeparator();
        char floatSep = symbols.getDecimalSeparator();
        if(text == null || text.length() == 0)
            text = String.valueOf(numValue.getValue());
        // Replace locale specific characters with ones that can be parsed
        return text.replace(String.valueOf(groupSep), "").replace(floatSep, '.');
        // Return what the spinner has stored as the value (which is only
        //  updated when the control looses focus (and may not be updated if
        //  the value was invalid))
        //return String.valueOf(numValue.getValue());
    }

    public void setText(String text)
    {   if(isType(PT_INT) == true)
            numValue.setValue(Integer.parseInt(text));
        else
            numValue.setValue(Double.parseDouble(text));
    }

}
