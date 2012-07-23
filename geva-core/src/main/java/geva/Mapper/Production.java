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
 * Production.java
 *
 * Created on 09 October 2006, 16:14
 */

package geva.Mapper;


import geva.Util.Constants;
import geva.Util.Enums;

import java.util.ArrayList;

/**
 * Production extends an ArrayList<Symbol>. 
 * @author EHemberg
 */
public class Production extends ArrayList<Symbol>{

    // Variables
    private boolean recursive; // Recursive nature of production
    private int minimumDepth; // Minimum depth of parse tree for production to map to terminal symbol(s)
    
    /** Creates a new Production with newLength elements.
     * @param newLength initial length of production
     */
    public Production(int newLength){
        super(newLength);
        setRecursive(false);
        setMinimumDepth(Integer.MAX_VALUE>>1);
    }
    
    /** Creates a new Production*/
    public Production(){
        super();
    }
    
    /**Copy constructor; copy all symbols.
     * @param copy productions to copy
     */
    public Production(Production copy){
        super(copy);
        this.recursive = copy.recursive;
        this.minimumDepth = copy.minimumDepth;
    }
    
    /** Return the recursive nature of this production.
     * @return true if the production is recursive
     */
    public boolean getRecursive() {
        return recursive;
    }
    
    /** Update the recursive nature of this production.
     * @param newRecursive recursiveness of productino
     */
    public void setRecursive(boolean newRecursive){
        recursive = newRecursive;
    }
    
    /** Return the minimum mapping depth of this production. (Number of inputs until
     * output consists of only terminals)
     * @return minimum depth
     */
    public int getMinimumDepth() {
        return minimumDepth;
    }
    
    /** Update the minimum mapping depth of this production.
     * @param newMinimumDepth minimum depth
     */
    public void setMinimumDepth(int newMinimumDepth){
        minimumDepth = newMinimumDepth;
    }
    
    /**
     * Return the number of NTSymbols in the production
     * JByrne also added GE_CODONS to the terminal list.
     * @return number of Non-Terminal symbols in the production
     */
    public int getNTSymbols() {
        int cnt = 0;
        for (Symbol o : this) {
        if(o.getType() == Enums.SymbolType.NTSymbol && !(o.getSymbolString().startsWith(Constants.GE_CODON_VALUE_PARSING))) {
                cnt++;
            }
        }
        return cnt;
    }
    
    @Override
    @SuppressWarnings({"ForLoopReplaceableByForEach"})
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(int i = 0;i<this.size();i++) {
            s.append(this.get(i).getSymbolString());
        }
        return s.toString();
    }
}
