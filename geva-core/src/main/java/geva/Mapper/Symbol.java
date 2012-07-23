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
 * Symbol.java
 *
 * Created on 09 October 2006, 12:33
 *
 */

package geva.Mapper;

import geva.Util.Enums;

/**This class implements a symbol of a grammar, be it a non-terminal or
 * terminal symbol. It is used both by the Rule class, to specify its left
 * side non-terminal symbols, and by the Production class, to specify its
 * terminal and non-terminal symbols.
 * It is also used by the Phenotype class, to specify phenotypic symbols.
 */
public class Symbol {
    
    // Variables
    /**
     * Specifies a terminal or nonterminal symbol
     */
    private Enums.SymbolType type; //Symbol enums
    /**
     * The string associated with the symbol.
     */
    private String symbolString;
    
    /**
     * Default constructor, creates a symbol with an empty string.
     */
    public Symbol() {
        this.type = Enums.SymbolType.TSymbol;
        this.symbolString = "";
    }
    
    /**
     * Creates a symbol of type symbolType and with the string
     * @param newString string of symbol
     * @param newType type of symbol
     */
    public Symbol(String newString, Enums.SymbolType newType) {
        this.type = newType;
        this.symbolString = newString;
    }
    
    /** Copy constructor
     * @param copy symbol to copy
     */
    public Symbol(Symbol copy) {
        this.type = copy.type;
        this.symbolString = copy.symbolString;
    }
    
    /** Set the type of the symbol
     * @param newType symbol type
      */
    public void setType(Enums.SymbolType newType) {
        type = newType;
    }
    
    /** Return the type of the symbol
     * @return symbol type
      */
    public Enums.SymbolType getType() {
        return type;
    }
    
    /** Get the symbol string
     * @return string
     */
    public String getSymbolString() {
        return symbolString;
    }

    /**
     * Set the string of the symbol
     * @param s symbol string
     */
    public void setSymbolString(String s) {
        this.symbolString = s;
    }
    
    /**Compare type and symbol string value
     * @param newSymbol symbol to compare
     * @return if equal string and type true
     */
    public boolean equals(Symbol newSymbol)  {
        //Check the symbolString and the type
        return (getSymbolString().equals(newSymbol.getSymbolString())) && (getType() == newSymbol.getType());
    }
    
    /**
     * Compares the symbol strings of this
     * and the symbol passed as a parameter
     * @param newSymbol symbol string
     * @return if strings equal true
     */
    public boolean equals(String newSymbol)  {
        //Check the symbolString and the type
        return (getSymbolString().equals(newSymbol));
    }
    
    /**
     * Clears the string and type
     */
    public void clear() {
        this.symbolString = null;
        this.type = null;
    }

    @Override
    public String toString() {
        return this.getSymbolString();
    }
}
