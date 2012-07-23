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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package geva.Individuals;

import geva.Individuals.Phenotype;
import geva.Mapper.Symbol;
import geva.Util.Enums;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author erikhemberg
 */
public class PhenotypeTest {

    public PhenotypeTest() {
    }

/**
 * Test of constructor, of calss Phenotype
 */
    @Test
    public void testPhenotype_Phenotype() {
        System.out.println("PhenotypeTest: Phenotype(Phenoytpe p)");
        Phenotype p = new Phenotype();
        p.add(new Symbol("a",Enums.SymbolType.TSymbol));
        p.add(new Symbol("b", Enums.SymbolType.TSymbol));
        Phenotype p2 = new Phenotype(p);
        assertEquals(p2, p);
        assertNotSame(p,p2);
    }

    /**
     * Test of getString method, of class Phenotype.
     */
    @Test
    public void testGetString() {
        System.out.println("PhenotypeTest: getString");
        
        //Empty
        Phenotype instance = new Phenotype();
        String expResult = "";
        String result = instance.getString();
        assertEquals(expResult, result);

        //Filled
        instance = new Phenotype();
        instance.add(new Symbol("a",Enums.SymbolType.TSymbol));
        instance.add(new Symbol("b", Enums.SymbolType.TSymbol));
        result = "a b ";
        expResult = instance.getString();
        assertEquals(true, result.equals(expResult));

                //Filled
        instance = new Phenotype();
        instance.add(new Symbol("a",Enums.SymbolType.TSymbol));
        instance.add(new Symbol("", Enums.SymbolType.TSymbol));
        instance.add(new Symbol("b", Enums.SymbolType.TSymbol));
        result = "a  b ";
        assertEquals(true, instance.getString().equals(result));
}

    /**
     * Test of getStringNoSpace method, of class Phenotype.
     */
    @Test
    public void testGetStringNoSpace() {
        System.out.println("PhenotypeTest: getStringNoSpace");
        Phenotype instance = new Phenotype();
        String result = instance.getStringNoSpace();
        //Filled
        instance = new Phenotype();
        instance.add(new Symbol("a",Enums.SymbolType.TSymbol));
        instance.add(new Symbol("b", Enums.SymbolType.TSymbol));
        result = "ab";
        String expResult = instance.getString();
        assertEquals(true, instance.getStringNoSpace().equals(result));
    }

}