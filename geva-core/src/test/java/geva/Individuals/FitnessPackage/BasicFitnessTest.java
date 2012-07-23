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

package geva.Individuals.FitnessPackage;

import geva.Individuals.FitnessPackage.BasicFitness;
import geva.Individuals.FitnessPackage.DoubleFitness;
import geva.Individuals.FitnessPackage.Fitness;

import java.util.ArrayList;
import java.util.Collections;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author erikhemberg
 */
public class BasicFitnessTest {

    public BasicFitnessTest() {
    }

    /**
     * Test of compareTo method, of class BasicFitness.
     */
    @Test
    public void testCompareTo() {
        System.out.println("*BasicFitnessTest: compareTo");
        
        //Null
        Fitness o = null;
        BasicFitness instance = new BasicFitness();
        try {
            instance.compareTo(o);
        } catch(NullPointerException e) {
            assertTrue(true);
        }

        //NaN and ==
        o = new BasicFitness();
        o.setDouble(Double.NaN);
        instance.setDouble(Double.NaN);
        assertEquals(0, instance.compareTo(o));

        //Infinite and ==
        o.setDouble(Double.NEGATIVE_INFINITY);
        instance.setDouble(Double.POSITIVE_INFINITY);
        assertEquals(0, instance.compareTo(o));

        //<
        o.setDouble(0.0);
        instance.setDouble(-0.1);
        assertEquals(-1, instance.compareTo(o));

        //>
        o.setDouble(0.0);
        instance.setDouble(0.1);
        assertEquals(1, instance.compareTo(o));

        //sort asending
        ArrayList<BasicFitness> alb = new ArrayList<BasicFitness>(4);
        double[] da = {3.0,2.0,1.0,4.0};
        double[] expected = {1.0,2.0,3.0,4.0};
        for(double d: da) {
            alb.add(new BasicFitness(d, null));
        }
        Collections.sort(alb);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], alb.get(i).getDouble(), 0.0000001);
        }

        //sort descending
        double[] expected2 = {4.0,3.0,2.0,1.0};
        Collections.reverse(alb);
        for (int i = 0; i < expected2.length; i++) {
            assertEquals(expected2[i], alb.get(i).getDouble(), 0.0000001);
        }

    }

    /**
     * Test of getMaxDoubleFitness method, of class BasicFitness.
     */
    @Test
    public void testGetMaxDoubleFitness() {
        System.out.println("*BaicFitnessTest: getMaxDoubleFitness");
        BasicFitness instance = new BasicFitness();
        double expResult = BasicFitness.DEFAULT_FITNESS;
        double result = instance.getMaxDoubleFitness();
        assertEquals(expResult, result, 0.00001);
        }

    /**
     * Test of getMinDoubleFitness method, of class BasicFitness.
     */
    @Test
    public void testGetMinDoubleFitness() {
        System.out.println("*BaicFitnessTest: getMinDoubleFitness");
        BasicFitness instance = new BasicFitness();
        double expResult = 0.0;
        double result = instance.getMinDoubleFitness();
        assertEquals(expResult, result, 0.00001);
    }

   /**
     * Test of getMaxIntFitness method, of class BasicFitness.
     */
    @Test
    public void testGetMaxIntFitness() {
        System.out.println("*BaicFitnessTest: getMaxIntFitness");
        BasicFitness instance = new BasicFitness();
        int expResult = (int)BasicFitness.DEFAULT_FITNESS;
        int result = instance.getMaxIntFitness();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMinIntFitness method, of class BasicFitness.
     */
    @Test
    public void testGetMinIntFitness() {
        System.out.println("*BaicFitnessTest: getMinIntFitness");
        BasicFitness instance = new BasicFitness();
        int expResult = 0;
        int result = instance.getMinIntFitness();
        assertEquals(expResult, result);
    }

   /**
     * Test of getDistance method, of class BasicFitness.
     */
    @Test
    public void testGetDistance() {
        System.out.println("*BaicFitnessTest: getDistance");
        DoubleFitness f = new DoubleFitnessMock(1.0);
        BasicFitness instance = new BasicFitness();
        double expResult = -1.0;
        double result = instance.getDistance(f);
        assertEquals(expResult, result,0.0001);
    }

    /**
     * Test of setDouble method, of class BasicFitness.
     */
    @Test
    public void testSetDouble() {
        System.out.println("*BaicFitnessTest: setDouble");
        //NaN
        double f = Double.NaN;
        BasicFitness instance = new BasicFitness();
        instance.setDouble(f);
        assertEquals(true, instance.getDouble()==instance.getMaxDoubleFitness());

        //Inf
        f = Double.NEGATIVE_INFINITY;
        instance = new BasicFitness();
        instance.setDouble(f);
        assertEquals(true, instance.getDouble() == instance.getMaxDoubleFitness());
    }

    /**
     * Test of setInt method, of class BasicFitness.
     */
    @Test
    public void testSetInt() {
        System.out.println("*BaicFitnessTest: setInt");
        int f = Integer.MAX_VALUE;
        BasicFitness instance = new BasicFitness();
        instance.setInt(f);
        assertEquals(instance.getDouble(), (float)Integer.MAX_VALUE,0.1);
    }
    
}