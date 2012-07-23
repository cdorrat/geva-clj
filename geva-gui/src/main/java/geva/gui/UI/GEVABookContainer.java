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
import javax.swing.JTabbedPane;
import java.util.Hashtable;

/**
 * The root container for all properties. Displayed as a tabbed control, allows
 *  the properties to be grouped into logical pages
 * @author eliottbartley
 */
public class GEVABookContainer extends GEVAContainerControl
{

    private static class ControlGroups
                 extends Hashtable<String, GEVAControlGroup> { }

    /**
     * The swing component used to render this control
     */
    private JTabbedPane guiBook;
    /**
     * Keeps a list of control groups. When a control group is to be modified,
     *  the control that issues the modification calls on its parent's
     *  *ControlGroup action command. This call is passed up the parent chain
     *  until it reaches the Book (root) container, which then goes through the
     *  controls in the group and calls on them to perform the action
     */
    private ControlGroups controlGroups = new ControlGroups();

    public GEVABookContainer
    (   GEVADirtyListener    dirtyListener,
        String               comment
    ){  super(dirtyListener, null, "book", null, comment);
        guiBook = new JTabbedPane();
    }

    /**
     * Helper - programmatically change the selected tabbed page to the
     *  specified page by title.
     * @param title The title of the page used for the title argument set when
     *  creating the GEVAPageControl
     */
    public void setPage(String title)
    {   int i = 0;
        for(GEVAControl control : controls)
        {   if(control.getTitle().equalsIgnoreCase(title) == true)
            {   guiBook.setSelectedIndex(i);
                break;
            }
            i++;
        }
    }

    @Override
    public void add(GEVAControl page)
    {   super.add(page);
        guiBook.addTab(page.getTitle(), page.getComponent());
    }

    public Component getComponent(int index)
    {   return guiBook;
    }

    public int countComponents() { return 1; }

    /**
     * Add a control group to an object, typically the Book object.
     * A control group is a collection of controls that all act in a similar way
     *  for a given event, such as all being shown/hidden depending on the
     *  selection of a drop-down
     * @param name The name of the control group. All controls in the group are
     *  referenced by this name
     * @param controlGroup A collection of all the controls in the group
     */
    public void addControlGroup(String name, GEVAControlGroup controlGroup)
    {   this.controlGroups.put(name, controlGroup);
    }

    @Override
    public void setVisibleControlGroup(String name, boolean show)
    {   GEVAControlGroup controlGroup = this.controlGroups.get(name);
        if(name != null && name.length() != 0) {
            if(controlGroup != null) {
		//HACK for hiding instead of showing elements
		// inverting the values
		if(controlGroup.isHiding()) {
		    show = !show;
		}
                for(GEVAControl control : controlGroup) {
                    control.setVisible(show);
		}
            } else {
                assert false : "Group " + name + " does not exist";
	    }
	}
    }

    @Override
    public void setVisible(boolean show)
    {   guiBook.setVisible(show);
    }

    @Override
    public String getText()
    {   return guiBook.getTitleAt(guiBook.getSelectedIndex());
    }

    @Override
    public void setText(String text)
    {   setPage(text);
    }

}
