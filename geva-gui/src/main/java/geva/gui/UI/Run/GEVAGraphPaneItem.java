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

import java.awt.Color;

/**
 * Store the configuration properties of an item in the graph. GEVAGraphItems
 *  are stored in a GEVAGraphCategory
 * @author eliott bartley
 */
public class GEVAGraphPaneItem
{

    /**
     * Hide this item on the graph, but still visible on the options
     */
    public static int VISIBLE_HIDE = 0;
    /**
     * Show this item on the graph
     */
    public static int VISIBLE_SHOW = 1;
    /**
     * Show this item on the graph, plus mark it as highlighted
     */
    public static int VISIBLE_BOLD = 2;

    private String name;
    private int visible;
    private Color colour;
    private String title;
    private String measure;
    private String errorName;

    public GEVAGraphPaneItem
    (   String      name,
        int      visible,
        Color     colour,
        String     title,
        String   measure,
        String errorName
    ){  assert clean(name) != null : "name required";
        this.visible = visible;
        this.colour = colour;
        this.name = clean(name);
        this.title = clean(title);
        this.measure = clean(measure);
        this.errorName = clean(errorName);
    }

    public int getVisible()
    {   return visible;
    }

    public Color getColour()
    {   return colour;
    }

    public String getName()
    {   return name;
    }

    public String getTitle()
    {   if(title == null)
            return name;
        return title;
    }

    public String getMeasure()
    {   return measure;
    }

    public String getErrorName()
    {   return errorName;
    }

    /**
     * Helper to ensure all read values are clean. No white-space padding, no
     *  empty strings (uses null instead)
     * @param value
     * @return
     */
    private String clean(String value)
    {   value = value.trim();
        if(value.length() == 0)
            return null;
        return value;
    }

}
