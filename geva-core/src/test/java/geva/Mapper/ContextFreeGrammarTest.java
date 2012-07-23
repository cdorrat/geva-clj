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

import geva.Exceptions.MalformedGrammarException;
import geva.Helpers.GrammarCreator;
import geva.Mapper.ContextFreeGrammar;
import geva.Mapper.Rule;
import geva.Mapper.Symbol;
import geva.Util.Enums;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author erikhemberg
 */
public class ContextFreeGrammarTest {

    ContextFreeGrammarMock g;
    String file_name;

    public ContextFreeGrammarTest() {
    }

    @Before
    public void setUp() throws MalformedGrammarException {
        g = new ContextFreeGrammarMock();
        file_name = GrammarCreator.getGrammarFile();
        g.readBNFFileFromFilesystem(file_name);
    }

    /**
     * Test of readBNFFile method, of class ContextFreeGrammar.
     */
    @Test
    public void testReadBNFFile() {
        System.out.println("* ContextFreeGrammarTest: readBNFFile");
        String fileName = "";
        ContextFreeGrammar instance = new ContextFreeGrammarMock();
        boolean result = false;
        try {
            result = instance.readBNFFile(fileName);
            fail("Expected a Malformed Grammar Exception");
        } catch (MalformedGrammarException ex) {
        } catch (AssertionError e) {
            assertTrue(true);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
        assertEquals(false, result);

        //Null
        instance = new ContextFreeGrammarMock();
        fileName = null;
        result = true;
        try {
            result = instance.readBNFFile(fileName);
            fail("Expected a Malformed Grammar Exception");
        } catch (MalformedGrammarException ex) {
        } catch (AssertionError e) {
            assertTrue(true);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
        assertEquals(false, result);
    // There are no files in the jar file any more!!?? REMOVE??!! 20082011
    }

    /**
     * Test of readBNFFileFromFilesystem method, of class ContextFreeGrammar.
     */
    @Test
    public void testReadBNFFileFromFilesystem() throws MalformedGrammarException {
        System.out.println("* ContextFreeGrammarTest: readBNFFileFromFilesystem");
        String fileName = "";
        ContextFreeGrammar instance = new ContextFreeGrammarMock();
        boolean result = true;
        try {
            result = instance.readBNFFileFromFilesystem(fileName);
            fail("Expected a Malformed Grammar Exception");
        } catch (MalformedGrammarException ex) {
        } catch (AssertionError e) {
            assertTrue(true);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
        assertEquals(false, result);

        //False
        instance = new ContextFreeGrammarMock();
        result = false;
        assertEquals(true, instance.readBNFFileFromFilesystem(this.file_name));
    }

    /**
     * Test of readBNFString method, of class ContextFreeGrammar.
     */
    @Test
    public void testReadBNFString() throws MalformedGrammarException {
        System.out.println("* ContextFreeGrammarTest: readBNFString");
        String bnfString = "";
        //empty
        ContextFreeGrammarMock instance = new ContextFreeGrammarMock();
        boolean result = false;
        try {
            result = instance.readBNFString(bnfString);
            fail("Expected a Malformed Grammar Exception");
        } catch (MalformedGrammarException ex) {
        }
        assertEquals(false, result);

        //proper
        bnfString = instance.readBNFFileToString(this.file_name);
        instance = new ContextFreeGrammarMock();
        assertEquals(true, instance.readBNFString(bnfString));

        //null
        instance = new ContextFreeGrammarMock();
        result = true;
        try {
            result = instance.readBNFString(null);
        } catch (MalformedGrammarException ex) {
        } catch (NullPointerException e) {
            assertTrue(true);
        }
        assertEquals(false, result);

    }

    /**
     * Test of findRule method, of class ContextFreeGrammar.
     */
    @Test
    public void testFindRule_Symbol() {
        System.out.println("* ContextFreeGrammarTest: findRule");
        //Null
        Symbol s = null;
        Rule result = new Rule();
        try {
            result = g.findRule(s);
        } catch (AssertionError e) {
            assertTrue(true);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
        assertEquals(new Rule(), result);

        //Non-Terminal
        s = new Symbol("<string>", Enums.SymbolType.NTSymbol);
        Rule r = ContextFreeGrammarMock.getTestRule();
        assertTrue(r.toString().equals(g.findRule(s).toString()));
        assertEquals(true, r!=g.findRule(s));
        
        //Non-terminal Can only test object similarity on the same object
        assertEquals(g.getRules().get(0), g.findRule(s));
        
        //Terimnal
        s = new Symbol("_", Enums.SymbolType.TSymbol);
        assertEquals(null, g.findRule(s));

        //Invalid symbol
        s = new Symbol("<test>", Enums.SymbolType.NTSymbol);
        assertEquals(null, g.findRule(s));
    }

    /**
     * Test of findRule method, of class ContextFreeGrammar.
     */
    @Test
    public void testFindRule_String() {
        System.out.println("* ContextFreeGrammarTest: findRule");

        //Empty
        String s = "";
        Rule result = new Rule();
        try {
            result = g.findRule(s);
        } catch (AssertionError e) {
            assertTrue(true);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
        assertEquals(null, result);

        //Null
        s = null;
        result = ContextFreeGrammarMock.getTestRule();
        try {
            result = g.findRule(s);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
        
        //Non-terminal
        s = "<string>";
        Rule r = ContextFreeGrammarMock.getTestRule();
        assertTrue(r.toString().equals(g.findRule(s).toString()));
        assertEquals(true, r!=g.findRule(s));

        //Non-terminal Can only test object similarity on the same object
        assertEquals(g.getRules().get(0), g.findRule(s));

        //Terminal
        s = "_";
        assertEquals(null, g.findRule(s));

        //Nonexisting rule
        s = "<test>";
        assertEquals(null, g.findRule(s));
    }

    /**
     * Test of setProductionMinimumDepth method, of class ContextFreeGrammar.
     */
    @Test
    public void testSetProductionMinimumDepth() {
        System.out.println("* ContextFreeGrammarTest: setProductionMinimumDepth");
        //Initial
        Rule r = g.getRules().get(0);
        g.setProductionMinimumDepth(r);
        assertEquals(2, r.get(0).getMinimumDepth());
        assertEquals(3, r.get(1).getMinimumDepth());

        //set
        r = g.getRules().get(2);
        g.setProductionMinimumDepth(r);
        assertEquals(0, r.get(0).getMinimumDepth());

        //Null
        try {
            g.setProductionMinimumDepth(null);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
        //Not a good test since the setProductionMinimumDepth() has been already called when g was initialised. Does only confirm that function, but not test this
        assertEquals(1, g.getRules().get(1).get(0).getMinimumDepth());
    }

    /**
     * Test of calculateMinimumDepthRecursive method, of class ContextFreeGrammar.
     */
    @Test
    public void testCalculateMinimumDepthRecursive() {
        System.out.println("* ContextFreeGrammarTest: calculateMinimumDepthRecursive");
        //Null
        Rule startRule = null;
        ArrayList<Rule> visitedRules = null;
        ContextFreeGrammar instance = new ContextFreeGrammarMock();
        try {
            instance.calculateMinimumDepthRecursive(startRule, visitedRules);
        } catch (NullPointerException e) {
            assertTrue(true);
        }

        //Empty list
        visitedRules = new ArrayList<Rule>();
        instance = new ContextFreeGrammarMock();
        try {
            instance.calculateMinimumDepthRecursive(startRule, visitedRules);
        } catch (NullPointerException e) {
            assertTrue(true);
        }

        //Initial
        startRule = g.getStartRule();
        visitedRules = new ArrayList<Rule>();
        g.calculateMinimumDepthRecursive(startRule, visitedRules);
        assertEquals(3, startRule.getMinimumDepth());

        //Test any rule
        startRule = g.getRules().get(1);
        visitedRules = new ArrayList<Rule>();
        g.calculateMinimumDepthRecursive(startRule, visitedRules);
        assertEquals(2, startRule.getMinimumDepth());
    }

    /**
     * Test of isRecursive method, of class ContextFreeGrammar.
     */
    @Test
    public void testIsRecursive() {
        System.out.println("* ContextFreeGrammarTest: isRecursive");
        //Null
        ArrayList<Rule> visitedRules = null;
        Rule startRule = null;
        ContextFreeGrammar instance = new ContextFreeGrammarMock();
        boolean result = false;
        try {
            result = instance.isRecursive(visitedRules, startRule);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
        assertEquals(false, result);

        //Empty list
        visitedRules = new ArrayList<Rule>();
        instance = new ContextFreeGrammarMock();
        result = false;
        try {
            result = instance.isRecursive(visitedRules, startRule);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
        assertEquals(false, result);

        //Initial
        startRule = g.getStartRule();
        visitedRules = new ArrayList<Rule>();
        result = g.isRecursive(visitedRules, startRule);
        assertEquals(true, result);

        //Any rule
        startRule = g.getRules().get(1);
        visitedRules = new ArrayList<Rule>();
        result = g.isRecursive(visitedRules, startRule);
        assertEquals(false, result);
    }

    /**
     * Test isInfinitelyRecursive, of class ContextFreeGrammar
     * Hmm, I should test the private methods
     */
    @Test
    public void testIsInfinitelyRecursive() throws MalformedGrammarException {
        System.out.println("* ContextFreeGrammarTest: isInfinitelyRecursive");
        //Infinitely recursive
        g = new ContextFreeGrammarMock();
        String file_name2 = file_name.replace("test_grammar", "test_infiniteRecursive.bnf");
        
        boolean result = g.readBNFFileFromFilesystem(file_name2);
        assertEquals(false, result);

        //GE codon value
        g = new ContextFreeGrammarMock();
        file_name2 = file_name.replace("test_grammar", "test_infiniteRecursive_gec.bnf");
        result = false;
        result = g.readBNFFileFromFilesystem(file_name);
        assertEquals(true, result);

    }
}