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

package geva.FitnessEvaluation.SymbolicRegression;

import geva.FitnessEvaluation.SymbolicRegression.Range;
import geva.Helpers.JUnitHelper;
import geva.Util.Random.MersenneTwisterFast;
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
public class RangeTest {

    public RangeTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of stringToRange method, of class Range.
     */
    @Test
    public void testStringToRange() {
        System.out.println("stringToRange");
//Fixed range
        String s = "x0 eq [1:2:7]; x1 eq [2:1:5]";
        Range r = new Range("");
        r.stringToRange(s);
        assertEquals(r.getStart(0), 1, 0.01);
        assertEquals(r.getStart(1), 2, 0.01);
        assertEquals(r.getStep(0), 2, 0.01);
        assertEquals(r.getStep(1), 1, 0.01);
        assertEquals(r.getStop(0), 7, 0.01);
        assertEquals(r.getStop(1), 5, 0.01);
        assertEquals(r.isRandom(0), false);
        assertEquals(r.isRandom(1), false);
        assertEquals(r.getTotalSamples(0), 3);
        assertEquals(r.getTotalSamples(1), 3);

    //Random range
        s = "x0 eq rnd(0,3,1); x1 eq rnd(1,3,2)";
        r = new Range("");
        r.stringToRange(s);
        assertEquals(r.getStart(0), 0, 0.01);
        assertEquals(r.getStart(1), 1, 0.01);
        assertEquals(r.getStep(0), 3, 0.01);
        assertEquals(r.getStep(1), 3, 0.01);
        assertEquals(r.getStop(0), 1, 0.01);
        assertEquals(r.getStop(1), 2, 0.01);
        assertEquals(r.isRandom(0), true);
        assertEquals(r.isRandom(1), true);
        assertEquals(r.getTotalSamples(0), 3);
        assertEquals(r.getTotalSamples(1), 3);

//Bad parse
        s = "x0 eq rnd(0,3:1)";
        r = new Range("");
        try {
            r.stringToRange(s);
        } catch (NumberFormatException e) {
            assertTrue(true);
        }

    //Bad parse
        s = "x0 eq rnd(0,3,1";
        r = new Range("");
        try {
            r.stringToRange(s);
        } catch (NumberFormatException e) {
            //assertTrue(true);
        }
    }

    /**
     * Test of getSamples method, of class Range.
     */
    @Test
    public void testGetSamples() {
        System.out.println("getSamples");
        //Fixed
        String s = "x0 eq [1:2:7]; x1 eq [2:1:5]";
        Range r = new Range(s);
        double[][] res = r.getSamples();
        double[][] exp = {{1,2},{3,3},{5,4}};
        JUnitHelper.checkMatrix(exp, res);

        //Random
        s = "x0 eq rnd(0,2,1); x1 eq rnd(1,2,2)";
        r = new Range(s, new MersenneTwisterFast(0));
        MersenneTwisterFast rng2 = new MersenneTwisterFast(0);
        res = r.getSamples();
        double d1 = rng2.nextDouble();
        double d2 = rng2.nextDouble();
        double d3 = rng2.nextDouble()+1;
        double d4 = rng2.nextDouble()+1;
        double[][] exp2 = {{d1,d3},{d2,d4}};
        JUnitHelper.checkMatrix(exp2, res);
    }

}