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

import geva.Mapper.Symbol;
import geva.Util.Enums;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author erikhemberg
 */
public class SymbolTest {

    public SymbolTest() {
    }

    /**
     * Test constructors
     **/
    @Test
    public void testTestConstructors() {
        System.out.println("* SymbolTest: TestConstructors");

        //Symbol()
        Symbol s = new Symbol();
        assertEquals(Enums.SymbolType.TSymbol, s.getType());
        assertEquals("", s.getSymbolString());

        //Symbol(string,type)
        s = new Symbol("test", Enums.SymbolType.TSymbol);
        assertEquals(Enums.SymbolType.TSymbol, s.getType());
        assertEquals(true, s.getSymbolString().equals("test"));

    }

    /**
     * Test copy constructors
     **/
    @Test
    public void testTestCopyConstructor() {
        System.out.println("* SymbolTest: TestCopyConstructor");
        Symbol s = new Symbol("test", Enums.SymbolType.TSymbol);
        Symbol copy = new Symbol(s);
        assertEquals(copy.getType(), s.getType());
        assertEquals(copy.getSymbolString(), s.getSymbolString());
        assertNotSame(copy, s);
    }

    /**
     * Test of equals method, of class Symbol.
     */
    @Test
    public void testEquals_Symbol() {
        System.out.println("* SymbolTest: equals");
        //Unequal strings
        Symbol newSymbol = new Symbol("test2", Enums.SymbolType.NTSymbol);
        Symbol instance = new Symbol("test", Enums.SymbolType.NTSymbol);
        boolean expResult = false;
        boolean result = instance.equals(newSymbol);
        assertEquals(expResult, result);

        //Unequal type && string
        newSymbol = new Symbol("test", Enums.SymbolType.TSymbol);
        expResult = false;
        assertEquals(expResult, instance.equals(newSymbol));

        //Unequal type
        newSymbol = new Symbol("test2", Enums.SymbolType.TSymbol);
        expResult = false;
        assertEquals(expResult, instance.equals(newSymbol));

        //Equal
        newSymbol = new Symbol("test", Enums.SymbolType.NTSymbol);
        expResult = true;
        assertEquals(expResult, instance.equals(newSymbol));

        //Null
        newSymbol = null;
        expResult = false;
        try {
            result = instance.equals(newSymbol);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class Symbol.
     */
    @Test
    public void testEquals_String() {
        System.out.println("* SymbolTest: equals");
        Symbol instance = new Symbol("test2", Enums.SymbolType.TSymbol);
        assertEquals(false, instance.equals("test"));
        assertEquals(true, instance.equals("test2"));
    }

    /**
     * Test of clear method, of class Symbol.
     */
    @Test
    public void testClear() {
        System.out.println("* SymbolTest: clear");
        Symbol instance = new Symbol("test", Enums.SymbolType.TSymbol);
        instance.clear();
        assertEquals(null, instance.getSymbolString());
        assertEquals(null, instance.getType());
    }

    /**
     * Test of toString method, of class Symbol.
     */
    @Test
    public void testToString() {
        System.out.println("* SymbolTest: toString");
        Symbol instance = new Symbol("test", Enums.SymbolType.TSymbol);
        assertEquals("test", instance.toString());
    }
}