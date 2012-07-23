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

/**
 * Store details of an event being passed on to a listener
 * @author eliottbartley
 */
public class GEVAActionEvent
{

    private GEVAControl source;
    private int action;
    private String string;
    private int command;
    private boolean isDirty;
    private StringBuilder invalidReason;

    /**
     * Event is fired when the control is called on to load. getActionString()
     *  will return loaded value (it can be modified and will appear as modified)
     *  returning false will load the default value rather than the loaded value
     */
    public static final int LOAD = 0;
    /**
     * Event is fired when the control is called on to save. getActionString()
     *  will return value about to be saved (it can be modified and will save as
     *  modified) returning false will prevent the value from being modified
     *  (the original value loaded will be saved)
     */
    public static final int SAVE = 1;
    /**
     * Event is fired when the control's value is modified either by user input
     * or loading. getActionString() will return the new value of the control
     * returning false will cancel the set dirty action
     */
    public static final int DIRTY = 2;
    /**
     * Event is fired when the control's value is validated. This occurs during
     *  load, save, and dirty actions. getActionString() will return the value
     *  to test returning false will mark the field as invalid. Returning false
     *  will highlight the field in red. It will also cause a dialog to display,
     *  notifying the user of invalid data during load/save
     */
    public static final int VALID = 3;

    /**
     * Create a new event
     * @param source The control that fired the event
     * @param action The event's action (LOAD/SAVE/DIRTY/VALID)
     * @param string The string text associated with the event
     */
    public GEVAActionEvent
    (   GEVAControl source,
        int         action,
        String      string,
        int        command
    ){  this.source  = source;
        this.action  = action;
        this.string  = string;
        this.command = command;
        this.isDirty = false;
    }

    /**
     * Get the control that fired the event
     */
    public GEVAControl getSource()
    {   return this.source;
    }

    /**
     * Get the event that was fired LOAD/SAVE/DIRTY/VALID
     */
    public int getAction()
    {   return this.action;
    }

    /**
     * Get the text associated with the event
     */
    public String getActionString()
    {   return this.string;
    }

    /**
     * Get the index associated with the event
     */
    public int getActionCommand()
    {   return this.command;
    }

    /**
     * Set the text associated with the event (this will allow to modify the
     *  result of the event) e.g. During save, modifying this will write the
     *  modified value to the output file (not the actual value)
     */
    public void setActionString(String string)
    {   this.string = string;
        this.isDirty = true;
    }

    /**
     * Set to true automatically when setActionString is called
     */
    public boolean isDirty()
    {   return this.isDirty;
    }

    /**
     * While validating, add an error that will be displayed to the user
     * @param reason The error caused when validating
     */
    public void addInvalidReason(String reason)
    {   if(invalidReason == null)
        {   invalidReason = new StringBuilder();
            invalidReason.append(reason);
        }
        else
            invalidReason.append("<br>" + reason);
    }

    /**
     * Get all the accumulated invalid reasons added and resets, ie calling a
     *  second time will return null. Returned string will be HTML formatted
     */
    public String getInvalidReason()
    {   if(invalidReason != null)
            return invalidReason.toString();
        return null;
    }

}
