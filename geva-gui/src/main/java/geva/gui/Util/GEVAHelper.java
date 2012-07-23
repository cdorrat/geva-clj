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

package geva.gui.Util;

import java.awt.Component;
import java.awt.Insets;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.text.DecimalFormatSymbols;

/**
 * Some helpful methods for repeated tasks across several classes
 * @author eliottbartley
 */
public class GEVAHelper
{

    private GEVAHelper() { }

    /**
     * When showing error messages in classes that don't have direct access to
     *  a parent window, this allows them to set the parent to be the
     *  application window. A bit of a hack, I know, but I'm a bit of a hacker!
     */
    public static Component mainWindow;

    /**
     * Given a string, wrap it in quotes (") if that string contains spaces
     * @param string The string to wrap
     * @return The string wrapped in quotes if there are spaces in the string,
     *  else the string is returned unchanged
     */
    public static String quote(String string)
    {

        if(string.indexOf(' ') != -1)
            string = "\"" + string + "\"";
        return string;

    }

    /**
     * Ruin all locale specific formatting for a parsable equivalent
     * @param value
     * @return
     */
    private static String parsePrepare(String value)
    {   DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        char group = symbols.getGroupingSeparator();
        char decimal = symbols.getDecimalSeparator();
        return value.trim()
            .replace(String.valueOf(group), "")
            .replace(decimal, '.');
    }

    /**
     * Parse a string containing an integer with locale specific formatting.
     * e.g. 1.000,00 is formatted to 1000.00 and then parsed to int
     */
    public static int parseInt(String value)
    {   return Integer.parseInt(parsePrepare(value));
    }

    /**
     * Parse a string containing a float with locale specific formatting
     * e.g. 1.000,00 is formatted to 1000.00 and then parsed to float
     */
    public static float parseFloat(String value)
    {   return Float.parseFloat(parsePrepare(value));
    }

    /**
     * Parse a string containing a double with locale specific formatting
     * e.g. 1.000,00 is formatted to 1000.00 and then parsed to double
     */
    public static double parseDouble(String value)
    {   return Double.parseDouble(parsePrepare(value));
    }

    /**
     * Trim an array of strings so that all white-space padding is removed and
     *  any strings that contained just white-space and removed completely from
     *  the array. e.g. {" a ", " ", "b"} -> {"a", "b"}
     * @param a An array of strings to prune
     * @return The array of strings with no white-space padding and no elements
     *  empty containing empty strings
     */
    public static String[] prune(String[] a)
    {   int c = 0;
        for(int i = 0; i < a.length; i++)
            a[i] = a[i].trim();
        for(String b : a)
            if(b.length() != 0)
                c++;
        String[] d = new String[c];
        c = 0;
        for(String b : a)
            if(b.length() != 0)
                d[c++] = b;
        return d;
    }

    /**
     * Trim an array of strings so that all padding white-space is removed,
     *  e.g. {" a ", " ", "b"} -> {"a", "", "b"}
     * @param a An array of strings to trim
     * @return The array of strings with no white-space padding
     */
    public static String[] trim(String[] a)
    {   for(int i = 0; i < a.length; i++)
            a[i] = a[i].trim();
        return a;
    }

    /**
     * gridAdd(Container, Container, int, int, int, int, double, double)
     *  overload. When calling on gridAdd(..), gridW and gridH are set to 1, and
     *  weightY is set to 0
     * @param container The control to add <var>control</var> to
     * @param control The control being added to <var>container</var>
     * @param gridX The grid column to add the control
     * @param gridY The grid row to add the control
     * @param weightX The amount of horizontal space this column should take
     *  relative to other columns
     */
    public static void gridAdd
    (   Container container,
        Container control,
        int       gridX,
        int       gridY,
        double    weightX
    ){  gridAdd(container, control, gridX, gridY, 1, 1, weightX, 0);
    }

    /**
     * Helper for adding a control to a GridBagLayout control
     * @param container The control to add <var>control</var> to
     * @param control The control being added to <var>container</var>
     * @param gridX The grid column to add the control
     * @param gridY The grid row to add the control
     * @param gridW The number of columns to span
     * @param gridH The number of rows to span
     * @param weightX The amount of horizontal space this column should take
     *  relative to other columns
     * @param weightY The amount of vertical space this column should take
     *  relative to other rows
     */
    public static void gridAdd
    (   Container container,
        Container control,
        int       gridX,
        int       gridY,
        int       gridW,
        int       gridH,
        double    weightX,
        double    weightY
    ){  container.add
        (   control,
            new GridBagConstraints
            (   gridX, gridY,
                gridW, gridH,
                weightX, weightY,
                GridBagConstraints.CENTER,
                GridBagConstraints.BOTH,
                new Insets(1, 1, 1, 1),
                0, 0
            )
        );
    }

}
