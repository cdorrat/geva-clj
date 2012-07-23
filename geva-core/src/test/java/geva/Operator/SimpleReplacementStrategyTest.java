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
import geva.Operator.SimpleReplacementStrategy;
import geva.Operator.Operations.EliteReplacementOperation;
import geva.Operator.Operations.Operation;
import geva.Operator.Operations.ReplacementOperation;
import geva.Operator.Operations.TournamentSelect;
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
public class SimpleReplacementStrategyTest {

    public SimpleReplacementStrategyTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of perform method, of class SimpleReplacementStrategy.
     */
    @Test
    public void testPerform() {
        System.out.println("* SimpleReplacementStrategy: perform");
        //population < incomingPopulation
        int populationSize = 10;
        int incomingpopulationSize = 15;
        List<Individual> operands = new ArrayList<Individual>();
        for (int i = incomingpopulationSize; i > 0; i--) {
            operands.add(IndividualMaker.makeIndividual());
            operands.get(incomingpopulationSize-i).getFitness().setDouble(i+populationSize/2);
        }
        Population incommingPopulation = new SimplePopulation();
        incommingPopulation.addAll(operands);
        List<Individual> operands2 = new ArrayList<Individual>();
        for (int i = populationSize; i > 0; i--) {
            operands2.add(IndividualMaker.makeIndividual());
            operands2.get(populationSize-i).getFitness().setDouble(i);
        }
        Population population = new SimplePopulation();
        population.addAll(operands2);
        SimpleReplacementStrategy instance = new SimpleReplacementStrategy();
        instance.setOperation(new ReplacementOperation(populationSize));
        instance.setPopulation(population);
        instance.setIncomingPopulation(incommingPopulation);
        instance.perform();
        assertEquals(instance.population.size(), populationSize);
        assertEquals(instance.incomingPopulation.size(), 0);
        assertEquals(instance.population.get(0).getAge(),1);
        Individual[] pA = instance.population.getAll().toArray(new Individual[0]);
        Individual[] ipA = operands.subList(incomingpopulationSize-populationSize,incomingpopulationSize).toArray(new Individual[0]);
        assertArrayEquals(pA, ipA);

        //population = incommingPopulation
        populationSize = 10;
        incomingpopulationSize = 10;
        operands = new ArrayList<Individual>();
        for (int i = incomingpopulationSize; i > 0; i--) {
            operands.add(IndividualMaker.makeIndividual());
            operands.get(incomingpopulationSize-i).getFitness().setDouble(i+populationSize/2);
        }
        incommingPopulation = new SimplePopulation();
        incommingPopulation.addAll(operands);
        operands2 = new ArrayList<Individual>();
        for (int i = populationSize; i > 0; i--) {
            operands2.add(IndividualMaker.makeIndividual());
            operands2.get(populationSize-i).getFitness().setDouble(i);
        }
        population = new SimplePopulation();
        population.addAll(operands2);
        instance = new SimpleReplacementStrategy();
        instance.setOperation(new ReplacementOperation(populationSize));
        instance.setPopulation(population);
        instance.setIncomingPopulation(incommingPopulation);
        instance.perform();
        assertEquals(instance.population.size(), populationSize);
        assertEquals(instance.incomingPopulation.size(), 0);
        assertEquals(instance.population.get(0).getAge(),1);
        pA = instance.population.getAll().toArray(new Individual[0]);
        ipA = operands.subList(incomingpopulationSize-populationSize,incomingpopulationSize).toArray(new Individual[0]);
        assertArrayEquals(pA, ipA);

        //population > incommingPopulation
        populationSize = 15;
        incomingpopulationSize = 10;
        operands = new ArrayList<Individual>();
        for (int i = incomingpopulationSize; i > 0; i--) {
            operands.add(IndividualMaker.makeIndividual());
            operands.get(incomingpopulationSize-i).getFitness().setDouble(i+populationSize/2);
        }
        incommingPopulation = new SimplePopulation();
        incommingPopulation.addAll(operands);
        operands2 = new ArrayList<Individual>();
        for (int i = populationSize; i > 0; i--) {
            operands2.add(IndividualMaker.makeIndividual());
            operands2.get(populationSize-i).getFitness().setDouble(i);
        }
        population = new SimplePopulation();
        population.addAll(operands2);
        instance = new SimpleReplacementStrategy();
        instance.setOperation(new ReplacementOperation(populationSize));
        instance.setPopulation(population);
        instance.setIncomingPopulation(incommingPopulation);
        instance.perform();
        assertEquals(instance.population.size(), incomingpopulationSize);
        assertEquals(instance.incomingPopulation.size(), 0);
        assertEquals(instance.population.get(0).getAge(),1);
        pA = instance.population.getAll().toArray(new Individual[0]);
        ipA = operands.subList(0,incomingpopulationSize).toArray(new Individual[0]);
        assertArrayEquals(pA, ipA);
    }

    /**
     * Test of setOperation method, of class SimpleReplacementStrategy.
     */
    @Test
    public void testSetOperation() {
        System.out.println("* SimpleReplacementStrategy: setOperation");
        Operation op = new ReplacementOperation(1);
        SimpleReplacementStrategy instance = new SimpleReplacementStrategy();
        instance.setOperation(op);
        assertEquals(true, instance.replacementOperation instanceof ReplacementOperation);
    }

}