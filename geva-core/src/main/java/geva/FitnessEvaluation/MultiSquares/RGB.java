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

package geva.FitnessEvaluation.MultiSquares;

/**
 * A static-style class, useful for transferring data between JScheme and Java
 * in the BooleanGrammarSwingPainter class.
 *
 * @author jmmcd
 */

public class RGB {
    static int [] data;
    public static int width;
    public static int height;
    public static void init(int _width, int _height) {
	width = _width;
	height = _height;
	data = new int[width * height];
    }
    public RGB(int _width, int _height) {
	width = _width;
	height = _height;
	data = new int[width * height];
    }
    public static void set(int x, int y, int val) {
	data[x + width * y] = val;
    }
    public static int[] getAllData() {
	return data;
    }
}

