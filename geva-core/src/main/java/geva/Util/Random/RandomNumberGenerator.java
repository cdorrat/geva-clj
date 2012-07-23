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

package geva.Util.Random;

/**
 * Interface for number generators
 */
public interface RandomNumberGenerator {

    /**
     * Get the next int, where n is max
     * @param n max in value
     * @return next random int
     */
    public int nextInt(int n);

    /**
     * Get the next int
     * @return next int
     */
    public int nextInt();

    /**
     * Get the next double 0<=x<1
     * @return next double
     */
    public double nextDouble();

    /**
     * Get the next short
     * @return next short
     */
    public short nextShort();

    /**
     * Get the next char
     * @return next char
     */
    public char nextChar();

    /**
     * Get the next boolean
     * @return next boolean
     */
    public boolean nextBoolean();

    /**
     * Get the next boolean with probability of true defined by d
     * 0<=d<=1
     * @param d probablity of true
     * @return next boolean
     */
    public boolean nextBoolean(double d);

    /**
     * Set the seed
     * @param l seed
     */
    public void setSeed(long l);

    /**
     * Set the seed
     * @param aI seed
     */
    public void setSeed(int[] aI);
}