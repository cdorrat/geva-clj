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

package geva.Individuals.Populations;

import geva.Helpers.IndividualMaker;
import geva.Individuals.GEIndividual;
import geva.Individuals.Populations.Population;
import geva.Individuals.Populations.SimplePopulation;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * SimplePopulation is a wrapoper method for ArrayList that implements the
 * Population interface.
 * @author erikhemberg
 */
public class SimplePopulationTest {

    Population population;

    public SimplePopulationTest() {
    }

    @Before
    public void setUp() {
        population = new SimplePopulation();
        final int populationSize = 4;
        for (int i = 0; i < populationSize; i++) {
            GEIndividual individual = IndividualMaker.makeIndividual();
            individual.getFitness().setDouble(populationSize - i);
            population.add(individual);
        }
    }

    /**
     * Test of sort method, of class SimplePopulation.
     */
    @Test
    public void testSort() {
        System.out.println("*SimplePopulationTest: sort");
        double[] expected = new double[population.size()];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = i + 1;
        }
        population.sort();
        for (int i = 0; i < population.size(); i++) {
            assertEquals(population.get(i).getFitness().getDouble(), expected[i], 0.00001);
        }
    }

    /**
     * Test of toString method, of class SimplePopulation.
     */
    @Test
    public void testToString() {
        System.out.println("*SimplePopulationTest: toString");
        String expResult = "4.00,3.00,2.00,1.00";
        String result = population.toString();
        assertEquals(true, expResult.equals(result));
    }
}