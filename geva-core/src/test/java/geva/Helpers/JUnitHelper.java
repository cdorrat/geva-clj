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

package geva.Helpers;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class JUnitHelper {

    public static void checkArrays(int[] expResult, int[] result) {
   assertEquals(expResult.length, result.length);
        for (int i = 0; i < expResult.length; i++) {
            assertEquals(expResult[i], result[i]);
        }
    }

    public static void checkArrays(ArrayList expResult, ArrayList result) {
   assertEquals(expResult.size(), result.size());
        for (int i = 0; i < expResult.size(); i++) {
            assertEquals(expResult.get(i),result.get(i));
        }
    }

    private JUnitHelper() {
    }

    final public static void checkArrays(final double[] expResult, final double[] result) {
        assertEquals(expResult.length, result.length);
        for (int i = 0; i < expResult.length; i++) {
            assertEquals(expResult[i], result[i], 0.0000001);
        }
    }

    final public static void checkMatrix(final double[][] expResult, final double[][] result) {
        assertEquals(expResult.length, result.length);
        for (int i = 0; i < expResult.length; i++) {
            JUnitHelper.checkArrays(expResult[i], result[i]);
        }
    }

    final public static void checkList(final List<?> expected, final List<?> actual) {
        assertEquals(expected.size(), actual.size());
        for(int i=0; i<expected.size();i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }
}