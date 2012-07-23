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

package geva.Operator;

import geva.Helpers.IndividualMaker;
import geva.Individuals.Individual;
import geva.Individuals.Populations.Population;
import geva.Individuals.Populations.SimplePopulation;
import geva.Operator.CrossoverModule;
import geva.Operator.Operations.SinglePointCrossover;
import geva.Util.Random.MersenneTwisterFast;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author erikhemberg
 */
public class CrossoverModuleTest {

    MersenneTwisterFast rng = new MersenneTwisterFast(0);
    Population operands;

    public CrossoverModuleTest() {
    }

    @Before
    public void setUp() {
        List<Individual> iL = new ArrayList<Individual>();
        for (int i = 3; i > 0; i--) {
            iL.add(IndividualMaker.makeIndividual());
            iL.get(3-i).getFitness().setDouble(i);
            iL.get(3-i).setValid(true);
        }
        operands = new SimplePopulation();
        operands.addAll(iL);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of perform method, of class CrossoverModule.
     */
    @Test
    public void testPerform() {
        System.out.println("* CrossoverModule: perform");
        CrossoverModule instance = new CrossoverModule(rng, new SinglePointCrossover(rng, 0.99));
        instance.setPopulation(operands);
        instance.perform();
    }

    /**
     * Test of getRandomNotThis method, of class CrossoverModule.
     */
    @Test
    @SuppressWarnings("empty-statement")
    public void testGetRandomNotThis() {
        System.out.println("* CrossoverModule: getRandomNotThis");
        Individual me = operands.get(0);
        operands.remove(operands.get(1));
        CrossoverModule instance = new CrossoverModule(rng, new SinglePointCrossover(rng, 0.9));
        instance.setPopulation(operands);
        Individual expResult = operands.get(1);
        Individual result = instance.getRandomNotThis(me);
        assertEquals(expResult, result);
    }

}