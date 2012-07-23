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

import java.util.Properties;
import java.awt.Component;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import java.util.ArrayList;

/**
 * A name value control whose value is a drop down list. Supports special types
 *  for boolean values, group values, and enumerations.
 * Boolean values always write true or false to the file, but can display using
 *  more suitable names. Alternative values are set in the params ("hi,bye" - if
 *  hi is selected, true is output, bye=false)
 * Group values are enumerations that also show a named control group
 *  Enumerated values are set in params, interleaved with the control group
 *  ("name1,controlGroup1,name2,controlGroup2...")
 * Enumerations are values that are written to the file as they appear in the
 *  params ("name1,name2...")
 */
public class      GEVAChoiceProperty
       extends    GEVAPropertyControl
       implements ActionListener
{

    private static class ControlGroups extends ArrayList<String> { }

    /**
     * Constructor <var>type</var>. Accepts true/false (or their equivelants)
     */
    public static final String PT_BOOL = "bool";
    /**
     * Constructor <var>type</var>. Accepts name,group list pairs
     */
    public static final String PT_GROUP = "group";
    /**
     * Constructor <var>type</var>. Accepts comma separated lists
     */
    public static final String PT_ENUM = "";

    private static final String[] boolValues = {"true", "false"};
    private static final int BV_TRUE = 0;
    private static final int BV_FALSE = 1;

    private JComboBox cboValue;
    private ControlGroups controlGroups;

    /**
     * Create a drop-down control
     * @param dirtyListener GUI that listens to dirty events
     * @param parent Container for this control
     * @param type Can be PT_BOOL, PT_GROUP, or PT_ENUM. If PT_BOOL, saved
     *  value will be either true or false (regardless of params value (see
     *  params)). If PT_GROUP, params must include a GEVAControlGroup name after
     *  each entry. This named group will be shown when the entry is selected,
     *  else hidden. PT_GROUP also saves the value as it appears in params
     *  If this is PT_ENUM, the drop-down displays and saves the values
     *  exactly as they appear in params
     * @param title The title to show in the GUI of this control
     * @param name The name used when saving this to the properties file
     * @param comment A tooltip
     * @param initial The initially selected entry
     * @param params Comma separated list of entries to add to the drop-down.
     *  If type is PT_BOOL params is a comma separated pair of names that relate
     *  to a true/false output. If not specified, it default to "true,false". If
     *  given, only the first two entries are used. e.g. "a,b,c" will show the
     *  drop-donw with just entries a and b. If fewer than two entries is given,
     *  the remaining are padded with the "true,false" values. e.g. "a" shows as
     *  a and false. When loading or saving, the values are output/input as
     *  true and false regardless of what they appear as to the user. e.g.
     *  "yes, no" shows yes and no to the user, but saves/loads as true and
     *  false respectively. If type is PT_GROUP params is a a comma separated
     *  list of "entry,group" pairs (e.g. "fit,fitopts,fat,fatopts"), where
     *  'entry' is the text that is displayed in the drop-down, and 'group' is
     *  the name of a group of controls. The named group must be added to the
     *  book container that this control is a [grand-]child of (see
     *  GEVABookContainer.addControlGroup). When 'entry' is selected from the
     *  drop-down, its related 'group' is shown and all others (in this drop-
     *  down) are hidden. When loading/saving, the entry selected, as it appears,
     *  is used for the input/output value. If type is PT_ENUM params is a
     *  comma separated list of entries that are displayed and loaded/saved as
     *  is
     */
    public GEVAChoiceProperty
    (   GEVADirtyListener     dirtyListener,
        GEVAPropertyContainer parent,
        String                type,
        String                title,
        String                name,
        String                comment,
        String                initial,
        String                params
    ){  super(dirtyListener, parent, type, title, name, comment, initial, params);
        int                   i;
        cboValue = new JComboBox();
        // For boolean type, only add maximum two elements. If params are given,
        //  their names will overwrite the default 'true, false' values. Blank
        //  params will be given their 'true, false' equivelant "x" becomes "x,
        //  false" ",x" becomes "true, x"
        if(this.isType(PT_BOOL) == true)
        {   for(i = 0; i < this.params.length && i < 2; i++)
                if(this.params[i].trim().length() == 0)
                    cboValue.addItem(boolValues[i]);
                else
                    cboValue.addItem(this.params[i]);
            for(; i < 2; i++)
                cboValue.addItem(boolValues[i]);
        }
        else
        // For group type, every even value is the name that is shown on the
        //  drop-down (and output to the properties file) and every odd value
        //  is the name of the controlGroup (that is added to Book) that
        //  selecting the named drop-down value specified before it will cause
        //  to be visible "name1,cg1,name2,cg2" (selecting "name1" shows control
        //  group "cg1")
        if(this.isType(PT_GROUP) == true)
        {   controlGroups = new ControlGroups();
            for(i = 0; i < this.params.length; i++)
                if((i % 2) == 0)
                    cboValue.addItem(this.params[i]);
                else
                    controlGroups.add(this.params[i]);
        }
        else
        // Normal behaviour, just display list of names, output as seen
        if(this.params != null)
            for(i = 0; i < this.params.length; i++)
                cboValue.addItem(this.params[i]);
        cboValue.setSelectedItem(initial);
        cboValue.addActionListener(this);
        cboValue.setBackground(Color.white);
        parent.add(this);
    }

    public void setEnabled(boolean enabled)
    {   cboValue.setEnabled(enabled);
    }

    public boolean load(Properties properties)
    {   String value = properties.getProperty(name, initial);
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

        // Map true/false to named value
        if(isType(PT_BOOL) == true)
            if(value.equalsIgnoreCase(boolValues[BV_TRUE]) == true)
                cboValue.setSelectedIndex(BV_TRUE);
            else
            if(value.equalsIgnoreCase(boolValues[BV_FALSE]) == true)
                cboValue.setSelectedIndex(BV_FALSE);
            else
                cboValue.setSelectedItem(value);
        else
            cboValue.setSelectedItem(value);

        return valid();

    }

    public boolean save(Properties properties)
    {   String value;

        // Map named value to true/false
        if(isType(PT_BOOL) == true)
            if(cboValue.getSelectedIndex() == BV_TRUE)
                value = boolValues[BV_TRUE];
            else
                value = boolValues[BV_FALSE];
        else
            value = String.valueOf(cboValue.getSelectedItem());

        GEVAActionEvent event = new GEVAActionEvent
        (   this,
            GEVAActionEvent.SAVE,
            value,
            cboValue.getSelectedIndex()
        );
        boolean write = fireActionEvent(event);
        if(event.isDirty() == true)
            value = event.getActionString();

        if(write == true)
            properties.setProperty(name, value);

        return valid();

    }

    @Override
    public Component getComponent(int index)
    {   if(index == 1)
            return cboValue;
        return super.getComponent(index);
    }

    public void actionPerformed(ActionEvent event)
    {   int visibleIndex = -1;

        // If this is a group control, make visible all controls in the selected
        //  group
        if(isType(PT_GROUP) == true)
        {

            for(int groupIndex = 0;
                groupIndex < controlGroups.size();
                groupIndex++
            )   if(cboValue.getSelectedIndex() == groupIndex)
                    visibleIndex = groupIndex;
                else
                    parent.setVisibleControlGroup
                    (   controlGroups.get(groupIndex),
                        false
                    );

            // Set visible after setting all others hidden, instead of inline
            //  with them, so that if a control has been added to two groups,
            //  it won't be displayed by one group and then hidden by another
            if(visibleIndex != -1)
                parent.setVisibleControlGroup
                (   controlGroups.get(visibleIndex),
                    true
                );

        }

        if(eventFireAndSet(GEVAActionEvent.DIRTY) == true)
            this.dirtyListener.setDirty();

        validate();

    }

    public boolean valid()
    {   resetInvalidReason();
        // TODONT - replace true with internal validation; currently is none as
        //  the combobox can never be generically in an invalid state. (It can
        //  be specifically in an invalid state, which is what eventFireAndSet
        //  accounts for)
        return eventFireAndSet(GEVAActionEvent.VALID) & true;
    }

    /**
     * Setup all the fluff for calling a listener and record the results the
     *  listener returns
     * @param action
     * @return
     */
    private boolean eventFireAndSet(int action)
    {   String invalidReason;
        GEVAActionEvent event = new GEVAActionEvent
        (   this,
            action,
            String.valueOf(cboValue.getSelectedItem()),
            cboValue.getSelectedIndex()
        );
        boolean result = fireActionEvent(event);
        if(event.isDirty() == true)
            cboValue.setSelectedItem(event.getActionString());
        if(action == GEVAActionEvent.VALID
        && (invalidReason = event.getInvalidReason()) != null)
            addInvalidReason(invalidReason);
        return result;
    }

    public void validate()
    {   if(valid() == true)
            cboValue.setBackground(Color.white);
        else
            cboValue.setBackground(Color.red);
    }

    public String getText()
    {   return String.valueOf(cboValue.getSelectedItem());
    }

    public void setText(String text)
    {   cboValue.setSelectedItem(text);
    }

}
