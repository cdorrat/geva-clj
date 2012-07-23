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
package geva.Operator.Operations;

import geva.Helpers.GrammarCreator;
import geva.Helpers.IndividualMaker;
import geva.Individuals.Individual;
import geva.Operator.Operations.RouletteWheel;
import geva.Util.Constants;
import geva.Util.Random.MersenneTwisterFast;
import geva.Util.Random.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jbyrne
 */
public class RouletteWheelTest {

    Individual ind1 = IndividualMaker.makeIndividual();
    Individual ind2 = IndividualMaker.makeIndividual();
    Individual ind3 = IndividualMaker.makeIndividual();
    List<Individual> individuals = new ArrayList();
    List<Individual> individuals2 = new ArrayList();
    Properties p = new Properties();
    RandomNumberGenerator rng = new MersenneTwisterFast();


    public RouletteWheelTest() {
    }

    @Before
    public void setUp() {
        p = GrammarCreator.getProperties();
        p.setProperty(Constants.POPULATION_SIZE, "15");
        rng.setSeed(0);
        ind3.getFitness().setDouble(15);
        ind3.setPhenotype(IndividualMaker.getPhenotype("individual3"));
        ind2.getFitness().setDouble(10);
        ind2.setPhenotype(IndividualMaker.getPhenotype("individual2"));
        ind1.getFitness().setDouble(5);
        ind1.setPhenotype(IndividualMaker.getPhenotype("individual1"));

        individuals.add(ind1);
        individuals.add(ind2);
        individuals.add(ind3);
        individuals2.add(ind3);
        individuals2.add(ind2);
        individuals2.add(ind1);
    }




    /**
     * Test of doOperation method, of class RouletteWheel.
     */
    @Test
    public void testDoOperation_List() {
        System.out.println("doOperation");
        RouletteWheelMock instance = new RouletteWheelMock(15,rng);
        instance.doOperation(individuals);
    }

    /**
     * Test of rankPopulation method, of class RouletteWheel.
     */
    @Test
    public void testRankPopulation() {
        System.out.println("rankPopulation");
        RouletteWheel instance = new RouletteWheelMock();
        System.out.println("before: " + individuals2.toString());
        instance.rankPopulation(individuals2);
        System.out.println("after :" + individuals2.toString());
        // TODO review the generated test code and remove the default call to fail.
        assertEquals(individuals, individuals2);
    }

    /**
     * Test of spinRoulette method, of class RouletteWheel.
     */
    @Test
    public void testSpinRoulette() {
        System.out.println("spinRoulette");
        RouletteWheel instance = new RouletteWheelMock(15,rng);
        instance.calculateAccumulatedFitnessProbabilities(individuals);
        instance.spinRoulette(individuals);
        int result = instance.selectedPopulation.size();
        assertEquals(15, result);
    }

    /**
     * Test of calculateFitnessSum method, of class RouletteWheel.
     */
    @Test
    public void testCalculateFitnessSum() {
        System.out.println("calculateFitnessSum");
        RouletteWheel instance = new RouletteWheelMock();
        instance.calculateFitnessSum(individuals);
        assertEquals(instance.sumFit, 30, 9e-16);
    }

    /**
     * Test of getRNG method, of class RouletteWheel.
     */
    @Test
    public void testGetRNG() {
        System.out.println("getRNG");
        RouletteWheel instance = new RouletteWheelMock(5, new MersenneTwisterFast());
        RandomNumberGenerator result = instance.getRNG();
        assertEquals(true, result instanceof MersenneTwisterFast);

    }

    /**
     * Test of setRNG method, of class RouletteWheel.
     */
    @Test
    public void testSetRNG() {
        System.out.println("setRNG");
        RandomNumberGenerator m = new MersenneTwisterFast();
        RouletteWheel instance = new RouletteWheelMock();
        instance.setRNG(m);
        RandomNumberGenerator result = instance.getRNG();
        assertEquals(true, result instanceof MersenneTwisterFast);
    }
}