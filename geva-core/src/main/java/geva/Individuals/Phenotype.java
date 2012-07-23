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
 * Phenotype.java
 *
 * Created on 09 October 2006, 16:51
 *
 */

package geva.Individuals;


import geva.Mapper.Symbol;

import java.util.ArrayList;

/**
 * Phenotype extends ArrayList<Symbol>
 *
 * Phenotype holds the result of mapping from a genotype through a mapper.
 * The representation is a list of symbols, which must be all terminal if 
 * the individuals mapping was valid. There are two methods that are of particular
 * interest.
 *
 * getString() and getStringNoSpace()
 *
 *  get string will be used if you are evolving a program in a highlevel syntax
 *  where the language symbols must be space seperated. 
 *  The no space variant would be useful in other situations such as evolving non
 *  program code structures. Machine code, or solutions that have to undergo further 
 *  interpretation.   
 *
 */

public class Phenotype extends ArrayList<Symbol> {

    public Phenotype()
    {
        super();
    }

    /**
     * Copy constructor
     * @param p phenotype to copy
     */
    public Phenotype(Phenotype p) {
        super(p);
    }

    /**
     * Get a string reperesentation of the output(Phenotype)
     * @return string representation
     */
    public String getString() {
        Symbol currentSymbol;
        StringBuilder S = new StringBuilder();
        for (Object o : this) {
            currentSymbol = (Symbol)o;
            S.append(currentSymbol.getSymbolString());
            S.append(" ");//Add space
        }
        return S.toString();
    }

    /**
     * Get string representaiton without added whitespace
     * @return string representation
     */
    public String getStringNoSpace() {
        Symbol currentSymbol;
        StringBuilder S = new StringBuilder();
        for (Object o : this) {
            currentSymbol = (Symbol)o;
            S.append(currentSymbol.getSymbolString());
        }
        return S.toString();
    }

    @Override
    public String toString() {
        return this.getString();
    }

}
