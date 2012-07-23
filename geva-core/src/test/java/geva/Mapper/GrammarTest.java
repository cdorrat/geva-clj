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
package geva.Mapper;

import geva.Mapper.Grammar;
import geva.Mapper.Production;
import geva.Mapper.Rule;
import geva.Mapper.Symbol;
import geva.Util.Enums;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author erikhemberg
 */
public class GrammarTest {

    private Grammar g;
    private Symbol startSymbol;

    public GrammarTest() {
    }

    @Before
    public void setUp() {
        g = new GrammarMock();
        Rule r1 = new Rule();
        Production p = new Production();
        Symbol lhs = new Symbol("lhs", Enums.SymbolType.NTSymbol);
        Symbol s1 = new Symbol("testT", Enums.SymbolType.TSymbol);
        Symbol s2 = new Symbol("testNT", Enums.SymbolType.NTSymbol);
        Production p2 = new Production();
        //This should be an empty symbol according to the constructor
        Symbol s3 = new Symbol();
        r1.setLHS(lhs);
        p.add(s1);
        p.add(s2);
        p2.add(s3);
        r1.add(p);
        r1.add(p2);
        r1.setRecursive(true);
        r1.setMinimumDepth(2);
        //Set startSymbol
        startSymbol = lhs;

        Rule r2 = new Rule();
        p = new Production();
        lhs = new Symbol("testNT", Enums.SymbolType.NTSymbol);
        s1 = new Symbol("testT2", Enums.SymbolType.TSymbol);
        s2 = new Symbol("testNT2", Enums.SymbolType.NTSymbol);
        p2 = new Production();
        s3 = new Symbol("testNT3", Enums.SymbolType.NTSymbol);
        r2.setLHS(lhs);
        p.add(s1);
        p.add(s2);
        p2.add(s3);
        r2.add(p);
        r2.add(p2);
        r2.setRecursive(true);
        r2.setMinimumDepth(2);

        g.getRules().add(r1);
        g.getRules().add(r1);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test Constructor
     **/
    @Test
    public void testTestConstructors() {
        System.out.println("* GrammarTest: testTestConstructors");
        Grammar g2 = new GrammarMock();
        assertEquals(false, g2.getValidGrammar());
        Symbol s = startSymbol;
        s = g2.getStartSymbol();
        assertEquals(s, s);
        assertEquals(0, g2.getRules().size());
    }

    /**
     * Test Copy Constructor
     **/
    @Test
    public void testTestCopyConstructors() {
        System.out.println("* GrammarTest: testTestCopyConstructors");
        Grammar copy = new GrammarMock(g);
        assertEquals(copy.getValidGrammar(), g.getValidGrammar());
        assertEquals(copy.getStartSymbol(), g.getStartSymbol());
        assertNotSame(copy.getRules(), g.getRules());
        assertEquals(copy.getRules(), g.getRules());
        for (int i = 0; i < g.getRules().size(); i++) {
            assertNotSame(copy.getRules().get(i), g.getRules().get(i));
            assertEquals(true, copy.getRules().get(i).toString().equals(g.getRules().get(i).toString()));
        }
    }

    /**
     * Test of getStartSymbol method, of class Grammar.
     */
    @Test
    public void testGetStartSymbol() {
        System.out.println("* GrammarTest: getStartSymbol");
        Symbol s = new Symbol(startSymbol);
        assertEquals(true, g.getStartSymbol().equals(s));
    }

    /**
     * Test of setStartSymbol method, of class Grammar.
     */
    @Test
    public void testSetStartSymbol_int() {
        System.out.println("* GrammarTest: setStartSymbol_int");
        //Default
        assertEquals(true, g.setStartSymbol(0));

        //Start symbol -1
        boolean result = true;
        try {
            result = g.setStartSymbol(-1);
            assertEquals(false, result);
        } catch (AssertionError e) {
            assertTrue(true);
            assertEquals(true, result);
        }

        //Start symbol > rules
        result = true;
        try {
            result = g.setStartSymbol(g.getRules().size() + 1);
            assertEquals(false, result);
        } catch (AssertionError e) {
            assertTrue(true);
            assertEquals(true, result);
        }

    }

    /**
     * Test of setStartSymbol method, of class Grammar.
     */
    @Test
    public void testSetStartSymbol_Symbol() {
        System.out.println("* GrammarTest: setStartSymbol_Symbol");
        //Default
        assertEquals(true, g.setStartSymbol(startSymbol));

        //Bad start symbol
        boolean result = true;
        result = g.setStartSymbol(new Symbol("fake", Enums.SymbolType.NTSymbol));
        assertEquals(false, result);

        //New symbol
        result = true;
        result = g.setStartSymbol(new Symbol("lhs", Enums.SymbolType.TSymbol));
        assertTrue(true);
        assertEquals(false, result);
    }

    /**
     * Test of setStartSymbol method, of class Grammar.
     */
    @Test
    public void testSetStartSymbol_String() {
        System.out.println("* GrammarTest: setStartSymbol_String");
        //Default
        assertEquals(true, g.setStartSymbol("lhs"));

        //Bad start symbol
        boolean result = true;
        result = g.setStartSymbol("testT");
        assertEquals(false, result);
    }
}