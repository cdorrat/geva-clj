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
 * Rule.java
 *
 * Created on 09 October 2006, 19:25
 */

package geva.Mapper;

import geva.Util.Enums;

import java.util.ArrayList;
/**
 * Rule extends Arraylist<Production>.
 * @author EHemberg
 */
public class Rule extends ArrayList<Production> {
    
    //Variables
    private boolean recursive;  // Recursive nature of rule
    private int minimumDepth;	// Minimum depth of parse tree for production to map to terminal symbol(s)
    private Symbol lhs; //Left hand side symbol of the rule
    
    //Constructors
    /**Creates a new rule with newLength elements.
     * @param newLength initial length of rule
     */
    public Rule(int newLength){
        super(newLength);
        setRecursive(false);
        setMinimumDepth(Integer.MAX_VALUE>>1);
    }
    
    /**Creates a new rule*/
    public Rule(){
        super();
    }
    
    /** Copy constructor.
     * @param copy rule to copy
     */
    public Rule(Rule copy){
        super(copy);
        this.lhs = copy.lhs;
        this.recursive = copy.recursive;
        this.minimumDepth = copy.minimumDepth;
    }
    
    /** Return the recursive nature of this rule.
     * @return true if rule is recursive
     */
    public boolean getRecursive() {
        return recursive;
    }
    
    /** Update the recursive nature of this rule.
     * @param newRecursive set recursiveness
     */
    public void setRecursive(boolean newRecursive){
        recursive=newRecursive;
    }
    
    /** Return the minimum mapping depth of this rule.
     * @return minimum depth
     */
    public int getMinimumDepth() {
        return minimumDepth;
    }
    
    /** Update the minimum mapping depth of this Rule.
     * @param newMinimumDepth minimum depth
     */
    public void setMinimumDepth(int newMinimumDepth){
        minimumDepth=newMinimumDepth;
    }

    /**
     * Set the left hand side symbol of the rule
     * Must be a Non Terminal symbol. 
     * @param s left hand side symbol
     */
    public void setLHS(Symbol s) {
	assert (s.getType() == Enums.SymbolType.NTSymbol) : "Bad type: "+s.getType();
	if(s.getType() == Enums.SymbolType.NTSymbol) {
	    this.lhs = s;
	}
    }

    /**
     * Get the left hand symbol of the rule
     * @return left hand side symbol
     */
    public Symbol getLHS() {
        return this.lhs;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(lhs.getSymbolString());
        s.append("::=");
        for(int i=0;i<this.size();i++) {
            s.append(this.get(i));
            if(i<(this.size()-1)) {
                s.append("|");
            }
        }
        return s.toString();
    }
    
}
