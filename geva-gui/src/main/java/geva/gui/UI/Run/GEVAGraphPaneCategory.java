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

package geva.gui.UI.Run;

import java.awt.Container;
import java.util.HashMap;

/**
 * Store a grouping of GEVAGraphItems that relate to each other, usually in
 *  their measurements in the graph. Each GEVAGraphPaneCategory given to
 *  GEVAGraph will produce a new tab, and each GEVAGraphPaneItem in the
 *  GEAVGraphCategory will produce a statistics and configuration entry under
 *  that tab
 * @author eliott bartley
 */
public class GEVAGraphPaneCategory
{

    private static class Items extends HashMap<String, GEVAGraphPaneItem> { };

    private String name;
    private Items items = new Items();
    private Container container;

    /**
     * Create a new category (statistics tab in graph pane) and give it a user-
     *  friendly name
     * @param name Name displayed on tab
     */
    public GEVAGraphPaneCategory(String name)
    {   this.name = name;
    }

    /**
     * Get the name that is displayed on the tab for this category
     */
    public String getName()
    {   return name;
    }

    /**
     * Add an item to this category. Each added item is displayed under the tab
     *  as a statistical data entry
     * @param item
     */
    public void addItem(GEVAGraphPaneItem item)
    {   items.put(item.getName(), item);
    }

    /**
     * Get an item by its name. this name is the name used by GEVA, not the
     *  user-friendly title
     * @param itemName
     */
    public GEVAGraphPaneItem getItem(String itemName)
    {   return items.get(itemName);
    }

    /**
     * Set the container where all items under this category are to be added.
     *  This is the awt (or swing) component
     * @param container
     */
    public void setContainer(Container container)
    {   this.container = container;
    }

    /**
     * Get the container where all items under this category are to be added.
     *  This is the awt (or swing) component
     */
    public Container getContainer()
    {   return container;
    }

}
