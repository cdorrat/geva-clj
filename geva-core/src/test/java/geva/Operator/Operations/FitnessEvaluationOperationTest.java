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

import geva.FitnessEvaluation.PatternMatch.WordMatch;
import geva.Helpers.GrammarCreator;
import geva.Helpers.IndividualMaker;
import geva.Individuals.GEIndividual;
import geva.Mapper.GEGrammar;
import geva.Operator.Operations.FitnessEvaluationOperation;
import geva.Util.Constants;

import java.util.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author erikhemberg
 */
public class FitnessEvaluationOperationTest {

    public FitnessEvaluationOperationTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of doOperation method, of class FitnessEvaluationOperation.
     */
    @Test
    public void testDoOperation_Individual() {
        System.out.println("* FitnessEvaluationOperation: doOperation");
        GEIndividual operand = (GEIndividual)IndividualMaker.makeIndividual();
        FitnessEvaluationOperation instance = new FitnessEvaluationOperation(new WordMatch("aa"));
        instance.setPopulationSize(10);
        instance.doOperation(operand);
        assertEquals(operand.isEvaluated(), true);
        assertEquals(operand.isValid(), true);
        assertEquals(operand.isMapped(), true);
        assertEquals(instance.getFitnessCache().containsKey(((GEGrammar)operand.getMapper()).getName()), true);
//TODO Test private class CachItem and the fitness Caching
    }

    /**
     * Test of setProperties method, of class FitnessEvaluationOperation.
     */
    @Test
    public void testSetProperties() {
        System.out.println("* FitnessEvaluationOperation: setProperties");
//POPULATION_SIZE = 10
        Properties p = GrammarCreator.getProperties();
        p.setProperty(Constants.POPULATION_SIZE, "10");
        FitnessEvaluationOperation instance = new FitnessEvaluationOperation(null);
        instance.setProperties(p);
        assertEquals(instance.getPopulationSize(), 10);
        assertEquals(instance.getOriginalPopulationSize(), 10);

    //POPULATION_SIZE = a
        p = GrammarCreator.getProperties();
        p.setProperty(Constants.POPULATION_SIZE, "a");
        instance = new FitnessEvaluationOperation(null);
        instance.setProperties(p);
        assertEquals(instance.getPopulationSize(), Integer.parseInt(Constants.DEFAULT_POPULATION_SIZE));
        assertEquals(instance.getOriginalPopulationSize(), 0);
}

}