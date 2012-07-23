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
import geva.Helpers.JUnitHelper;
import geva.Individuals.Individual;
import geva.Operator.Operations.ReplacementOperation;
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
public class ReplacementOperationTest {

    List<Individual> operands;

    public ReplacementOperationTest() {
    }

    @Before
    public void setUp() {
        operands = new ArrayList<Individual>();
        for (int i = 10; i > 0; i--) {
            operands.add(IndividualMaker.makeIndividual());
            operands.get(10-i).getFitness().setDouble(i);
        }
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setProperties method, of class ReplacementOperation.
     */
    @Test
    public void testSetProperties() {
        System.out.println("* ReplacementOperation: setProperties");
//STEADY_STATE
        Properties p = GrammarCreator.getProperties();
        p.setProperty(Constants.POPULATION_SIZE, "10");
        p.setProperty(Constants.REPLACEMENT_TYPE, Constants.STEADY_STATE);
        ReplacementOperation instance = new ReplacementOperation(0);
        instance.setProperties(p);
        assertEquals(instance.replacementSize, 1);

        //GENERATIONAL
        p = GrammarCreator.getProperties();
        p.setProperty(Constants.POPULATION_SIZE, "10");
        p.setProperty(Constants.REPLACEMENT_TYPE, Constants.GENERATIONAL);
        instance = new ReplacementOperation(0);
        instance.setProperties(p);
        assertEquals(instance.replacementSize, Integer.parseInt(p.getProperty(Constants.POPULATION_SIZE)));

        //GENERATION GAP = 0.5
        p = GrammarCreator.getProperties();
        p.setProperty(Constants.POPULATION_SIZE, "10");
        p.setProperty(Constants.GENERATION_GAP, "0.5");
        p.remove(Constants.REPLACEMENT_TYPE);
        instance = new ReplacementOperation(0);
        instance.setProperties(p);
        assertEquals(instance.replacementSize, 5);

         //GENERATION GAP = -0.5
        p = GrammarCreator.getProperties();
        p.setProperty(Constants.POPULATION_SIZE, "10");
        p.setProperty(Constants.GENERATION_GAP, "-0.5");
        p.remove(Constants.REPLACEMENT_TYPE);
        instance = new ReplacementOperation(0);
        instance.setProperties(p);
        assertEquals(instance.replacementSize, 10);
    }

    /**
     * Test of doOperation method, of class ReplacementOperation.
     */
    @Test
    public void testDoOperation_List() {
        System.out.println("* ReplacementOperation: doOperation");
        ReplacementOperation instance = new ReplacementOperation(2);
        List<Individual> reference = operands;
        instance.doOperation(operands);
        assertEquals(operands.size(), 8);
        assertSame(operands,reference);
        List<Double> dL = new ArrayList<Double>();
        List<Double> fL = new ArrayList<Double>();
        for (int i = operands.size(); i > 0; i--) {
            dL.add(new Double(i));
            fL.add(operands.get(operands.size()-i).getFitness().getDouble());
        }
        JUnitHelper.checkList(fL, dL);
    }

    /**
     * Test of doOperation method, of class ReplacementOperation.
     */
    @Test
    public void testDoOperation_List_int() {
        System.out.println("* ReplacementOperation: doOperation");
        ReplacementOperation instance = new ReplacementOperation(2);
        List<Individual> reference = operands;
        instance.doOperation(operands,4);
        assertEquals(operands.size(), 6);
        assertSame(operands,reference);
        List<Double> dL = new ArrayList<Double>();
        List<Double> fL = new ArrayList<Double>();
        for (int i = operands.size(); i > 0; i--) {
            dL.add(new Double(i));
            fL.add(operands.get(operands.size()-i).getFitness().getDouble());
        }
        JUnitHelper.checkList(fL, dL);
    }
    
}