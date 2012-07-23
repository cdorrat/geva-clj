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
import geva.Operator.Operations.ScaledRouletteWheel;

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
public class ScaledRouletteWheelTest {

    Individual ind1 = IndividualMaker.makeIndividual();
    Individual ind2 = IndividualMaker.makeIndividual();
    Individual ind3 = IndividualMaker.makeIndividual();
    List<Individual> individuals = new ArrayList();



    public ScaledRouletteWheelTest() {

    }

    @Before
    public void setUp() {
        ind1.getFitness().setDouble(5);
        individuals.add(ind1);
        ind2.getFitness().setDouble(10);
        individuals.add(ind2);
        ind3.getFitness().setDouble(15);
        individuals.add(ind3);


    }

    @After
    public void tearDown() {
    }

    /**
     * Test of calculateAccumulatedFitnessProbabilities method, of class ScaledRouletteWheel.
     */
    @Test
    public void testCalculateAccumulatedFitnessProbabilities() {
        System.out.println("calculateAccumulatedFitnessProbabilities");
        ScaledRouletteWheel instance = new ScaledRouletteWheel();
        instance.rankPopulation(individuals);
        instance.calculateFitnessSum(individuals);
        double[] expResults = new double[] {0.4166666666666667,0.75,1.0};
        assertEquals(30,instance.sumFit,9e-16);

        instance.calculateAccumulatedFitnessProbabilities(individuals);      
        JUnitHelper.checkArrays(expResults,instance.accProbs);
        assertEquals(30,instance.sumFit,9e-16);


    }

}