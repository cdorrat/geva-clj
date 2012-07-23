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
 * StaticIntIterator.java
 *
 * Created on 20 February 2007, 22:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package geva.Util.Structures;

/**
 *
 * @author Conor
 */
public class StaticIntIterator implements IntIterator {
    private final int[] data;
    private final int size;
    private int index;

    /**
     * Create instance
     * @param data array with data
     * @param size size
     */
    public StaticIntIterator(int[] data, int size) {
        this.index = 0;
        this.size = size;
        this.data = data;
    }
    
    public boolean hasNext() {
        return this.index < this.size;
    }
    
    public int next() {
        int ret = this.data[index];
        index ++;
        return ret;
        
    }


    public static void main(String args[]) {
        StaticIntList L = new StaticIntList(100);
        for(int i = 0;i<100;i++) {
            L.set(i,i);
        }
        
        IntIterator i = L.iterator();
        while(i.hasNext()) {
            System.out.println(i.next());
        }
        StaticIntList L2 = new StaticIntList(L);
        i = L2.iterator();
        while(i.hasNext()) {
            System.out.println(i.next());
        }
    }
    
    
    
    
}
