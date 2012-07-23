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

package geva.gui.UI.Run.JSci;

import java.awt.Color;

/**
 * Some colour fluff
 * @author eliott bartley
 */
public class GEVAGraphColours
{

    private GEVAGraphColours() { }

    // Looks like something interesting, but is just colours in the format
    //  0xRRGGBB. The relation between this and colourNames (below) is bijective
    public static final int[] colourIndexes = new int[]
    {   0x000000, 0x000080, 0x00008B, 0x0000CD, 0x0000FF, 0x006400,
        0x008000, 0x008080, 0x008B8B, 0x00BFFF, 0x00CED1, 0x00FA9A,
        0x00FF00, 0x00FF7F, 0x00FFFF, 0x00FFFF, 0x191970, 0x1E90FF,
        0x20B2AA, 0x228B22, 0x2E8B57, 0x2F4F4F, 0x32CD32, 0x3CB371,
        0x40E0D0, 0x4169E1, 0x4682B4, 0x483D8B, 0x48D1CC, 0x4B0082,
        0x556B2F, 0x5F9EA0, 0x6495ED, 0x66CDAA, 0x696969, 0x6A5ACD,
        0x6B8E23, 0x708090, 0x778899, 0x7B68EE, 0x7CFC00, 0x7FFF00,
        0x7FFFD4, 0x800000, 0x800080, 0x808000, 0x808080, 0x87CEEB,
        0x87CEFA, 0x8A2BE2, 0x8B0000, 0x8B008B, 0x8B4513, 0x8FBC8F,
        0x90EE90, 0x9370DB, 0x9400D3, 0x98FB98, 0x9932CC, 0x9ACD32,
        0xA0522D, 0xA52A2A, 0xA9A9A9, 0xADD8E6, 0xADFF2F, 0xAFEEEE,
        0xB0C4DE, 0xB0E0E6, 0xB22222, 0xB8860B, 0xBA55D3, 0xBC8F8F,
        0xBDB76B, 0xC0C0C0, 0xC71585, 0xCD5C5C, 0xCD853F, 0xD2691E,
        0xD2B48C, 0xD3D3D3, 0xD8BFD8, 0xDA70D6, 0xDAA520, 0xDC143C,
        0xDCDCDC, 0xDDA0DD, 0xDEB887, 0xE0FFFF, 0xE6E6FA, 0xE9967A,
        0xEE82EE, 0xEEE8AA, 0xF08080, 0xF0E68C, 0xF0F8FF, 0xF0FFF0,
        0xF0FFFF, 0xF5DEB3, 0xF5F5DC, 0xF5F5F5, 0xF5FFFA, 0xF8F8FF,
        0xFA8072, 0xFAA460, 0xFAEBD7, 0xFAF0E6, 0xFAFAD2, 0xFDF5E6,
        0xFF0000, 0xFF00FF, 0xFF1493, 0xFF4500, 0xFF6347, 0xFF69B4,
        0xFF7F50, 0xFF8C00, 0xFFA07A, 0xFFA500, 0xFFB6C1, 0xFFC0CB,
        0xFFD700, 0xFFDAB9, 0xFFDEAD, 0xFFE4B5, 0xFFE4C4, 0xFFE4E1,
        0xFFEBCD, 0xFFEFD5, 0xFFF0F5, 0xFFF5EE, 0xFFF8DC, 0xFFFACD,
        0xFFFAF0, 0xFFFAFA, 0xFFFF00, 0xFFFFE0, 0xFFFFF0, 0xFFFFFF
    };

    public static final String[] colourNames = new String[]
    {   "black", "navy", "dark blue", "medium blue", "blue", "dark green",
        "green", "teal", "dark cyan", "deep sky-blue", "dark turquoise",
        "medium spring-green", "lime", "spring-green", "aqua", "cyan",
        "midnight-blue", "dodger-blue", "light sea-green", "forest-green",
        "sea-green", "dark slate-gray", "lime-green", "medium sea-green",
        "turquoise", "royal-blue", "steel-blue", "dark slate-blue",
        "medium turquoise", "indigo", "dark olive-green", "cadet-blue",
        "cornflower-blue", "medium aquamarine", "dim gray", "slate-blue",
        "olive-drab", "slate-gray", "light slate-gray", "medium slate-blue",
        "lawn-green", "chartreuse", "aqua-marine", "maroon", "purple", "olive",
        "gray", "sky-blue", "light sky-blue", "blue-violet", "dark red",
        "dark magenta", "saddle-brown", "dark sea-green", "light green",
        "medium purple", "dark violet", "pale-green", "dark orchid",
        "yellow-green", "sienna", "brown", "dark gray", "light blue",
        "green-yellow", "pale violet-red", "light steel-blue", "powder-blue",
        "fire-brick", "dark goldenrod", "medium orchid", "rosy-brown",
        "dark khaki", "silver", "medium violet-red", "indian-red", "peru",
        "chocolate", "tan", "light gray", "thistle", "orchid", "goldenrod",
        "crimson", "gainsboro", "plum", "burlywood", "light cyan", "lavender",
        "dark salmon", "violet", "pale goldenrod", "light coral", "khaki",
        "alice-blue", "honeydew", "azure", "wheat", "beige", "white-smoke",
        "mint-cream", "ghost-white", "salmon", "sandy-brown", "antique-white",
        "linen", "light goldenrod-yellow", "old lace", "red", "fuchsia",
        "deep pink", "orange-red", "tomato", "hot pink", "coral", "dark orange",
        "light salmon", "orange", "light pink", "pink", "gold", "peach-puff",
        "navajo-white", "moccasin", "bisque", "misty-rose", "blanched-almond",
        "papaya-whip", "lavender-blush", "seashell", "corn-silk",
        "lemon-chiffon", "floral-white", "snow", "yellow", "light yellow",
        "ivory", "white"
    };

    /**
     * Get the nearest colour that matches and return the name of that colour.
     *  This matches based on the distance between the colours in RGB space.
     * @param colour Colour to find nearest match for
     * @return Name of nearest match
     */
    public static String getColourName(Color colour)
    {   double bestDistance = Double.MAX_VALUE;
        double distance;
        String name = null;
        int nameIndex = 0;

        for(int colourIndex : colourIndexes)
        {   int x = colour.getRed() - ((colourIndex & 0xFF0000) >> 16);
            int y = colour.getGreen() - ((colourIndex & 0x00FF00) >> 8);
            int z = colour.getBlue() - (colourIndex & 0x0000FF);
            if(x == 0 && y == 0 && z == 0)
                return colourNames[nameIndex];
            distance = x * x + y * y + z * z;
            if(distance < bestDistance)
            {   bestDistance = distance;
                name = colourNames[nameIndex];
            }
            nameIndex++;
        }

        return name;

    }

    /**
     * Given a name, return the colour
     * @param name The name of the colour
     * @return The colour of the name
     */
    public static Color getNamedColour(String name)
    {   for(int i = 0; i < colourNames.length; i++)
            if(colourNames[i].equalsIgnoreCase(name) == true)
                return new Color(colourIndexes[i]);
        return null;
    }

    /**
     * Taken from http://www.w3.org/TR/AERT#color-contrast
     * @param colour
     * @return true if this is a bright colour, and should have a black
     *  contrasting foreground, false if it should have a white foreground
     */
    public static boolean isBright(Color colour)
    {   return
        ((( colour.getRed()   * 299
          + colour.getGreen() * 587
          + colour.getBlue()  * 114
        )) / 1000) >= 128;
    }

}
