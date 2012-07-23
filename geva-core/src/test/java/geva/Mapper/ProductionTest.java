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

package geva.Mapper;

import geva.Mapper.Production;
import geva.Mapper.Symbol;
import geva.Util.Constants;
import geva.Util.Enums;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author erikhemberg
 */
public class ProductionTest {

    public ProductionTest() {
    }

    /**
     * Test constructors
     **/
    @Test
    public void testTestConstructors() {
        System.out.println("* ProductionTest: TestConstructors");
        //UNINITIALISED
        Production p = new Production();
        assertEquals(false, p.getRecursive());
        assertEquals(0, p.getMinimumDepth());

        //Size of production
        p = new Production(10);
        assertEquals(false, p.getRecursive());
        assertEquals((Integer.MAX_VALUE >> 1), p.getMinimumDepth());
        assertEquals(0, p.size());
    }

    /**
     * Test copy constructors
     **/
    @Test
    public void testTestCopyConstructor() {
        System.out.println("* ProductionTest: TestCopyConstructor");
        Production p = new Production();
        p = new Production(10);
        p.add(new Symbol("test", Enums.SymbolType.NTSymbol));
        Production copy = new Production(p);
        assertEquals(copy.getRecursive(), p.getRecursive());
        assertEquals(copy.getMinimumDepth(), p.getMinimumDepth());
        assertEquals(copy.size(), p.size());
        assertNotSame(copy, p);
        assertEquals(copy.get(0).getType(), p.get(0).getType());
        assertEquals(copy.get(0).getSymbolString(), p.get(0).getSymbolString());
        assertSame(copy.get(0), p.get(0));
    }

    /**
     * Test of getNTSymbols method, of class Production.
     */
    @Test
    public void testGetNTSymbols() {
        System.out.println("* ProductionTest: getNTSymbols");
//Default test
        Production instance = new Production();
        instance.add(new Symbol("test", Enums.SymbolType.NTSymbol));
        instance.add(new Symbol("test", Enums.SymbolType.TSymbol));
        instance.add(new Symbol("test", Enums.SymbolType.NTSymbol));
        instance.add(new Symbol(Constants.GE_CODON_VALUE_PARSING,Enums.SymbolType.NTSymbol));
        int expResult = 2;
        int result = instance.getNTSymbols();
        assertEquals(expResult, result);

        //Test clear
        instance.clear();
        assertEquals(0, instance.getNTSymbols());
    }

    /**
     * Test of toString method, of class Production.
     */
    @Test
    public void testToString() {
        System.out.println("* ProductionTest: toString");
        Production instance = new Production();
        instance.add(new Symbol("test", Enums.SymbolType.NTSymbol));
        instance.add(new Symbol("test", Enums.SymbolType.TSymbol));
        String expResult = "testtest";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
}