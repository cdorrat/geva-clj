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
import geva.Individuals.FitnessPackage.Fitness;
import geva.Individuals.Populations.SimplePopulation;
import geva.Operator.Operations.EliteOperationSelection;
import geva.Util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author erikhemberg
 */
public class EliteOperationSelectionTest {

    public EliteOperationSelectionTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setProperties method, of class EliteOperationSelection.
     */
    @Test
    public void testSetProperties() {
        System.out.println("* EliteOperationSelection: setProperties");
        Properties p = GrammarCreator.getProperties();
        p.setProperty(Constants.ELITE_SIZE, "1");
        p.setProperty(Constants.EVALUATE_ELITES, "true");
        EliteOperationSelection instance = new EliteOperationSelection(0);
        instance.setProperties(p);
        assertEquals(instance.size, Integer.parseInt(p.getProperty(Constants.ELITE_SIZE)));
        assertEquals(instance.isEvaluateElites(), Boolean.parseBoolean(p.getProperty(Constants.EVALUATE_ELITES)));
        assertEquals(instance.getSelectedPopulation().size(), 0);

        //ELITE_SIZE -1
        instance = new EliteOperationSelection(0);
        p.setProperty(Constants.ELITE_SIZE, "-1");
        p.setProperty(Constants.EVALUATE_ELITES, "");
        instance.setProperties(p);
        assertEquals(instance.size, 0);
        assertEquals(instance.isEvaluateElites(), false);
        assertEquals(instance.getSelectedPopulation().size(), 0);
    }

    /**
     * Test of doOperation method, of class EliteOperationSelection.
     */
    @Test
    public void testDoOperation_List() {
        System.out.println("* EliteOperationSelection: doOperation");
        List<Individual> operands = new ArrayList<Individual>();
        operands.add(IndividualMaker.makeIndividual());
        operands.get(0).getFitness().setDouble(2.0);
        operands.get(0).setValid(true);
        operands.get(0).setEvaluated(true);
        operands.add(IndividualMaker.makeIndividual());
        operands.get(1).getFitness().setDouble(1.0);
        operands.get(1).setValid(true);
        operands.get(1).setEvaluated(true);
        EliteOperationSelection instance = new EliteOperationSelection(1);
        instance.setEvaluate_elites(false);
        instance.doOperation(operands);
        assertEquals(instance.selectedPopulation.size(),1);
        assertEquals(instance.selectedPopulation.get(0).getFitness().getDouble(),operands.get(1).getFitness().getDouble(),0.001);
        assertNotSame(instance.selectedPopulation.get(0),operands.get(1));
        assertEquals(instance.selectedPopulation.get(0).isEvaluated(),true);
        assertEquals(instance.selectedPopulation.get(0).isValid(), true);
    }

    /**
     * Test of rankPopulation method, of class EliteOperationSelection.
     */
    @Test
    public void testRankPopulation() {
        System.out.println("* EliteOperationSelection: rankPopulation");
        List<Individual> operands = new ArrayList<Individual>();
        operands.add(IndividualMaker.makeIndividual());
        operands.get(0).getFitness().setDouble(2.0);
        operands.add(IndividualMaker.makeIndividual());
        operands.get(1).getFitness().setDouble(1.0);
        EliteOperationSelection instance = new EliteOperationSelection(1);
        instance.setEvaluate_elites(false);
        instance.doOperation(operands);
        Fitness[] expResult = {operands.get(1).getFitness(), operands.get(0).getFitness()};
        Fitness[] res = instance.rankPopulation(operands);
        assertEquals(expResult.length, res.length);
        assertArrayEquals(expResult, res);
        
    }

}