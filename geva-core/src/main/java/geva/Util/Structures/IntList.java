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

/*
 * FastIntegerList.java
 *
 * Created on February 20, 2007, 6:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package geva.Util.Structures;

/**
 * Interface for a list structure that stores ints
 * @author Blip
 */
public interface IntList {

    /**
     * Get int at index position
     * @param index position to get element form
     * @return element at index
     */
    public int  get(int index);

    /**
     * Set item at index
     * @param index position to set
     * @param item item to set
     */
    public void set(int index, int item);

    /**
     * Clear structure
     */
    public void clear();

    /**
     * Make an array view
     * @return array view
     */
    public int[] toArray();

    /**
     * Get an iterator
     * @return iterator
     */
    public IntIterator iterator();

    /**
     * Add an int
     * @param item int to add
     * @throws IndexOutOfBoundsException list to small
     */
    public void add(int item) throws IndexOutOfBoundsException;

    /**
     * Size of structure
     * @return size
     */
    public int size();
}
