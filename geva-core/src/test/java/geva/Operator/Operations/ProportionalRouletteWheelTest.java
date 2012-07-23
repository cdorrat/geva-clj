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

import geva.Helpers.IndividualMaker;
import geva.Helpers.JUnitHelper;
import geva.Individuals.Individual;
import geva.Operator.Operations.ProportionalRouletteWheel;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jbyrne
 */
public class ProportionalRouletteWheelTest {

    Individual ind1 = IndividualMaker.makeIndividual();
    Individual ind2 = IndividualMaker.makeIndividual();
    Individual ind3 = IndividualMaker.makeIndividual();
    Individual ind4 = IndividualMaker.makeIndividual();
    Individual ind5 = IndividualMaker.makeIndividual();
    Individual ind6 = IndividualMaker.makeIndividual();
    List<Individual> individuals = new ArrayList();
    List<Individual> individuals2 = new ArrayList();

    public ProportionalRouletteWheelTest() {
    }
 

    @Before
    public void setUp() {
        ind1.getFitness().setDouble(5);
        individuals.add(ind1);
        ind2.getFitness().setDouble(10);
        individuals.add(ind2);
        ind3.getFitness().setDouble(15);
        individuals.add(ind3);
        ind4.getFitness().setDouble(.2);
        individuals2.add(ind4);
        ind5.getFitness().setDouble(.3);
        individuals2.add(ind5);
        ind6.getFitness().setDouble(.5);
        individuals2.add(ind6);

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of calculateAccumulatedFitnessProbabilities method, of class ProportionalRouletteWheel.
     */
    @Test
    public void testCalculateAccumulatedFitnessProbabilities() {
        System.out.println("calculateAccumulatedFitnessProbabilities");
        ProportionalRouletteWheel instance = new ProportionalRouletteWheel();
        double[] expResults = new double[]{0.5207100591715976,0.8047337278106509,1.0};
        instance.rankPopulation(individuals);
        instance.calculateFitnessSum(individuals);
        assertEquals(false,instance.smallFit);
        assertEquals(30,instance.sumFit,9e-16);

        instance.calculateAccumulatedFitnessProbabilities(individuals);
        JUnitHelper.checkArrays(expResults,instance.accProbs);

        ProportionalRouletteWheel instance2 = new ProportionalRouletteWheel();
        instance2.rankPopulation(individuals2);
        instance2.calculateFitnessSum(individuals2);
        expResults = new double[] {0.48333189103950924,0.806088402996125,1.0};
        assertEquals(1,instance2.sumFit,9e-16);
        assertEquals(true,instance2.smallFit);
        instance2.calculateAccumulatedFitnessProbabilities(individuals2);
        JUnitHelper.checkArrays(expResults,instance2.accProbs);

    }
}