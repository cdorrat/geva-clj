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
import geva.Mapper.Rule;
import geva.Mapper.Symbol;
import geva.Util.Enums;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author erikhemberg
 */
public class RuleTest {

    private Rule r;

    public RuleTest() {
    }

    @Before
    public void setUp() {
        r = new Rule();
        Production p = new Production();
        Symbol lhs = new Symbol("lhs", Enums.SymbolType.NTSymbol);
        Symbol s1 = new Symbol("testT", Enums.SymbolType.TSymbol);
        Symbol s2 = new Symbol("testNT", Enums.SymbolType.NTSymbol);
        Production p2 = new Production();
        //This should be an empty symbol according to the constructor
        Symbol s3 = new Symbol();
        r.setLHS(lhs);
        p.add(s1);
        p.add(s2);
        p2.add(s3);
        r.add(p);
        r.add(p2);
        r.setRecursive(true);
        r.setMinimumDepth(2);
    }

    /**
     * Test CopyConstructor
     **/
    @Test
    public void testTestCopyConstructor() {
        System.out.println("* RuleTest: testTestCopyConstructor");
        Rule copy = new Rule(r);
        assertEquals(copy.getRecursive(), r.getRecursive());
        assertEquals(copy.getMinimumDepth(), r.getMinimumDepth());
        assertEquals(copy.getLHS(), r.getLHS());
        assertSame(copy.getLHS(), r.getLHS());
        for (int i = 0; i < r.size(); i++) {
            assertEquals(copy.get(i), r.get(i));
            assertSame(copy.get(i), r.get(i));
        }
    }

    /**
     * Test of setLHS method, of class Rule.
     */
    @Test
    public void testSetLHS() {
        //Test TSymbol
        System.out.println("* RuleTest: setLHS");
        Symbol s = new Symbol("testT", Enums.SymbolType.TSymbol);
        Rule instance = new Rule();
        try {
            instance.setLHS(s);
        } catch (AssertionError e) {
            assertTrue(true);
        }
        assertEquals(null, instance.getLHS());

        //Test NTSymbol
        s = new Symbol("testT", Enums.SymbolType.NTSymbol);
        instance.setLHS(s);
        assertEquals(true, s == instance.getLHS());

        Symbol s1 = new Symbol("testT", Enums.SymbolType.NTSymbol);
        assertEquals(true, s1.equals(instance.getLHS()));
    }

    /**
     * Test of toString method, of class Rule.
     */
    @Test
    public void testToString() {
        System.out.println("* RuleTest: toString");
        Rule instance = r;
        String expResult = "lhs::=testTtestNT|";
        String result = instance.toString();
        assertEquals(result, true, expResult.equals(result));
    }
}