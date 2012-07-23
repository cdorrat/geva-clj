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

import java.awt.Component;
import java.util.Properties;
import java.util.ArrayList;

/**
 * Base class on which all UI controls are derived
 * @author eliottbartley
 */
public abstract class GEVAControl
{

    private static class Listeners extends ArrayList<GEVAActionListener> { }

    /**
     * All controls have a reference back to the GUI window. This is
     *  so they can call on the GUI's setDirty() when they are updated
     */
    protected GEVADirtyListener dirtyListener;
    /**
     * All controls have a parent (though a Book will have a null parent)
     */
    protected GEVAContainerControl parent;
    /**
     * Remember the type name so generic types will know their specifics
     */
    protected String type;
    /**
     * The text displayed to the user describing this controls purpose
     */
    protected String title;
    /**
     * The text input/output to the properties file to identif the property this
     *  control relates to. Container controls will leave this at null (null
     *  names will not write to the Properties file)
     */
    protected String name;
    /**
     * All controls have a comment, which is displayed as a tooltip
     */
    protected String comment;
    /**
     * Track all objects listening for events
     */
    private Listeners listeners = new Listeners();

    /**
     * All controls have to have the following information available at all
     *  times.
     * @param dirtyListener Reference to the GUI dialog window
     * @param parent Reference to the parent control
     * @param type The datatype for this control
     * @param title The text displayed to the user for this control
     * @param name The name of the property as it should be written to the
     *  properties file
     * @param comment The tooltip value to display for this control
     */
    GEVAControl
    (   GEVADirtyListener    dirtyListener,
        GEVAContainerControl parent,
        String               type,
        String               title,
        String               name,
        String               comment
    ){  this.dirtyListener = dirtyListener;
        this.parent        = parent;
        this.type          = type;
        this.title         = title;
        this.name          = name;
        this.comment       = comment;
    }

    /**
     * Get this control's parent control
     * @return this control's parent control or null if it has no parent
     */
    public GEVAContainerControl getParent()
    {   return this.parent;
    }

    /**
     * Get this control's type
     * @return this control's type
     */
    public String getType()
    {   return this.type;
    }

    /**
     * Helper - check if this is of type <var>type</var>
     * @param type The type to test against this control's type
     * @return true if this control is type <var>type</var>, else false
     */
    public boolean isType(String type)
    {   return this.type.equalsIgnoreCase(type);
    }

    /**
     * Get this control's title
     * @return this control's title
     */
    public String getTitle()
    {   return this.title;
    }

    /**
     * Get this control's name
     * @return this control's name
     */
    public String getName()
    {   return this.name;
    }

    /**
     * Get this control's comment
     * @return this control's comment
     */
    public String getComment()
    {   return this.comment;
    }

    /**
     * Make this control enabled or disabled.
     * @param enabled true to enabled, false to disable
     */
    public abstract void setEnabled(boolean enabled);

    /**
     * A control should override this to get its properties.
     * @param properties The properties of a loaded properties file
     */
    public abstract boolean load(Properties properties);

    /**
     * A control should override this to set its properties.
     * After all controls have had a chance to set the properties, the
     *  properties file will be written to by the caller
     * @param properties The properties of a loaded properties file
     */
    public abstract boolean save(Properties properties);

    /**
     * Abstract way for a container to know about its child's awt components.
     * Helper that gets the first component (all controls should have a
     *  component)
     * @return The main awt component of the control
     */
    public Component getComponent()
    {   return getComponent(0);
    }

    /**
     * Abstract way for a container to know about its child's awt components.
     * @param index The index of the component to get
     * @return The indexed awt component of the control
     */
    public abstract Component getComponent(int index);

    /**
     * Abstract way for a container to know about its child's awt components.
     * Counts how many components the child has
     * @return The number (N) components the child has.
     *  getComponent(0)..getComponent(N-1) must all return valid components
     */
    public abstract int countComponents();

    /**
     * Add a listener for events on this object
     * @param listener Listener that implements GEVAActionListener
     */
    public void addActionListener(GEVAActionListener listener)
    {   this.listeners.add(listener);
    }

    /**
     * Remove a listener for events on this object
     * @param listener Listener that implements GEVAActionListener
     */
    public void removeActionListener(GEVAActionListener listener)
    {   this.listeners.remove(listener);
    }

    /**
     * Called by GEVA* control when an event occurs and notifies all listeners
     *  of that event
     * @param event The details of the event that occurred
     * @return Return value based on event action. See GEVAActionEvent.java
     *  (LOAD/SAVE/DIRTY/VALID)
     */
    protected boolean fireActionEvent(GEVAActionEvent event)
    {   boolean result = true;
        for(GEVAActionListener listener : this.listeners)
            result &= listener.actionPerformed(event);
        return result;
    }

    /**
     * Make all the controls in the control group visible or hidden.
     * @param name The name of the control group, as set during the call to
     *  addControlGroup
     * @param show Specify true to show the group, else false
     */
    protected abstract void setVisibleControlGroup(String name, boolean show);

    /**
     * Make this control visible
     * @param show Specify true to show this control, else false to hide
     */
    public abstract void setVisible(boolean show);

    /**
     * Get the value of teh control, using a text string
     * @return The value of the control in string format
     */
    public abstract String getText();

    /**
     * Set the value of the control, using a text string
     * @param text The value to set
     */
    public abstract void setText(String text);

    /**
     * Get this control to validate itself
     */
    public abstract void validate();

}
