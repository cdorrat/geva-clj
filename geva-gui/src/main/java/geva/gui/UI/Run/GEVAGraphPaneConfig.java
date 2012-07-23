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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Store all configuration details by name. Graphs that want to use these
 *  configuration details must specify it by this name. Currently (2008y08M21d)
 *  only one name is used, 'GEVA'. I figured someday, someone might add another
 *  graph pane for more output, and reuse this class to configure how its data
 *  is displayed, and could do so by adding more names
 * @author eliott bartley
 */
public class GEVAGraphPaneConfig
{

    private static class Configurations extends HashMap<String, Categories> { }
    public static class Categories extends ArrayList<GEVAGraphPaneCategory> { }

    private GEVAGraphPaneConfig() { }

    private static Configurations configurations = new Configurations();

    /**
     * When the system resets, the graph configuration also needs to be reset
     */
    public static void reset()
    {   configurations = new Configurations();
    }

    /**
     * Add a new category to the named configuration. When getCategories is
     *  called on the same named item, all categories added through this method
     *  are returend
     * @param name An identifier for the configuration this category will belong
     *  to
     * @param category The category to add
     */
    public static void addCategory(String name, GEVAGraphPaneCategory category)
    {   Categories categories;
        if(configurations.containsKey(name) == true)
        {   categories = configurations.get(name);
            categories.add(category);
        }
        else
        {   categories = new Categories();
            categories.add(category);
            configurations.put(name, categories);
        }
    }

    /**
     * Get all the categories that were added to the named configuration in a
     *  list
     * @param name An identifier for the configuration this category will belong
     *  to
     * @return A list of all categories that were added to the named
     *  configuration through the addCategory method. If the named category does
     *  not exist, this returns an empty list rather than null
     */
    public static Categories getCategories(String name)
    {   Categories categories = configurations.get(name);
        // Don't allow null to be returned as this is always used as an iterator,
        //  so I'd perfer to exit from an empty list than have to check for a
        //  null
        if(categories == null)
            categories = new Categories();
        return categories;
    }

    /**
     * Get the first category in a named configuration that contains the named
     *  item
     * @param name An identifier for the configuration this category will belong
     *  to
     * @param itemName The identifier name of the graph item
     * @return The category in which the named item belongs or null if no item
     *  was found
     */
    public static GEVAGraphPaneCategory getCategoryWithItem
    (   String name,
        String itemName
    ){  for(GEVAGraphPaneCategory category : getCategories(name))
            if(category.getItem(itemName) != null)
                return category;
        return null;
    }

    /**
     * Get the named item in the named configuration. This searchs all
     *  categories added to the named configuration and returns the first item
     *  found that matches the itemName
     * @param name An identifier for the configuration this category will belong
     *  to
     * @param itemName The identifier name of the graph item
     * @return The named item or null if it was not found
     */
    public static GEVAGraphPaneItem getItem(String name, String itemName)
    {   GEVAGraphPaneItem item;
        for(GEVAGraphPaneCategory category : getCategories(name))
            if((item = category.getItem(itemName)) != null)
                return item;
        return null;
    }

    /**
     * Get the named item in the named configuration. This searchs all
     *  categories added to the named configuration, searching the named
     *  category first, and returns the first item found that matches the
     *  itemName.
     * @param name An identifier for the configuration this category will belong
     *  to
     * @param categoryName The category to search first. If the named item is
     *  not found in this category, all other categories are searched
     * @param itemName The identifier name of the graph item
     * @return The named item or null if it was not found
     */
    public static GEVAGraphPaneItem getItem
    (   String name,
        String categoryName,
        String itemName
    ){  Categories primaryCategories = getCategories(name);
        GEVAGraphPaneItem item;
        for(GEVAGraphPaneCategory category : primaryCategories)
            if((item = category.getItem(itemName)) != null)
                return item;
        for(Categories categories : configurations.values())
            if(categories != primaryCategories)
                for(GEVAGraphPaneCategory category : categories)
                    if((item = category.getItem(itemName)) != null)
                        return item;
        return null;
    }

}
