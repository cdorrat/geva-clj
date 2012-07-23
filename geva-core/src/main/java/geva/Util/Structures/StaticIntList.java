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
 * StaticIntList.java
 *
 * Created on February 20, 2007, 6:21 PM
 *
 * This is a simple implementation of a wrapper for a dynamically sized static array
 * to hold ints. Does not support insertion of elements between a particular element
 * or removal of elemnts but allow elements to change. 
 *
 */

package geva.Util.Structures;

/**
 * List of integers. When current size is reached the array size is doubled untill maxSize is reached
 * @author Blip
 */
public class StaticIntList implements IntList {

    public int[] data;//Declared public to give direct access to the array
    public int currentSize;
    public final int initialSize;
    public static int maxSize = Integer.MAX_VALUE;//128000;

    /**
     * Creates a new instance of StaticIntList
     * @param size start size of int list
     */
    public StaticIntList(int size) {
        this.data = new int[size];
        this.currentSize = 0;
        this.initialSize = size;
        for(int i=0;i<this.data.length;i++) {
            this.data[i] = 0;
        }
    }

    /**
     * Copy constructor
     * @param list object to copy
     */
    public StaticIntList(StaticIntList list) {
        this.data = new int[list.data.length];
        if(list.data!=null){
            System.arraycopy(list.data,0,this.data,0,list.data.length);
        }
        this.currentSize = list.currentSize;
        this.initialSize = list.initialSize;
    }

    /**
     * Set max size
     * @param i max size
     */
    public void setMaxSize(int i) {
        StaticIntList.maxSize = i;
    }

    /**
     * Get max size
     * @return max size
     */
    public int getMaxSize() {
        return StaticIntList.maxSize;
    }

    /**
     * Get element at index
     * @param index element index
     * @return element at index
     */
    public int get(int index) {
        return this.data[index];

    }

    /**
     * Set element at index
     * @param index index to set element
     * @param item element to set
     */
    public void set(int index, int item) {
        this.data[index] = item;
    }

    /**
     * Set all the elements to data
     * @param data the set elements
     */
    public void setAll(int[] data) {
        this.data = data;
        this.currentSize = this.data.length;
    }

    /**
     * Clear list by setting current size to 0
     */
    public void clear() {
        this.currentSize = 0;
    }

    /**
     * To array
     * @return array of list
     */
    public int[] toArray() {
        int[] arr = new int[this.currentSize];
        if(data!=null) {
            System.arraycopy(this.data,0,arr,0,this.currentSize);
        }
        return arr;
    }

    /**
     * Create iterator over list
     * @return iterator
     */
    public IntIterator iterator() {
        return new StaticIntIterator(this.data,this.currentSize);
    }

    /**
     * Add int to list. If current size equals data length a new data structure
     * of double size is created.
     * @param item item to add
     * @throws IndexOutOfBoundsException
     */
    public void add(int item) throws IndexOutOfBoundsException {
        if(this.currentSize < StaticIntList.maxSize){
            if(this.currentSize == this.data.length) {
                int[] newData = new int[this.data.length*2];
                System.arraycopy(this.data, 0, newData, 0, this.data.length);
                this.data = newData;
            }

            this.data[currentSize] = item;
            this.currentSize++;
        } else {
            throw new IndexOutOfBoundsException("No adding, too long, length:"+this.data.length);
        }
    }

    public static void main(String args[]) {
        StaticIntList l = new StaticIntList(10);
        for(int i=0;i<=8;i++) {
            l.add(i);
        }
        IntIterator itr = l.iterator();
        while(itr.hasNext()) {
            System.out.println(itr.next());
        }
        System.out.println(l.size());

    }

    /**
     * Get current size of the list
     * @return list size
     */
    public int size() {
        return this.currentSize;
    }

    /**
     * Get the total
     * @return
     */
    public int allocationSize(){
        return data.length;
    }

}
