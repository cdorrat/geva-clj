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

package geva.Operator.Operations.ContextSensitiveOperations;

import geva.Helpers.GrammarCreator;
import geva.Helpers.IndividualMaker;
import geva.Helpers.JUnitHelper;
import geva.Individuals.GEChromosome;
import geva.Individuals.GEIndividual;
import geva.Individuals.GEIndividualTest;
import geva.Mapper.ContextualDerivationTree;
import geva.Operator.Operations.ContextSensitiveOperations.StructuralMutation;
import geva.Util.Constants;
import geva.Util.GenotypeHelper;
import geva.Util.Random.MersenneTwisterFast;

import java.util.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jbyrne
 */
public class StructuralMutationTest {

    Properties p;

    public StructuralMutationTest() {
        p = GrammarCreator.getProperties();
	p.setProperty(Constants.MAX_WRAPS,"0");
        p.setProperty(Constants.DERIVATION_TREE,"geva.Mapper.ContextualDerivationTree");
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }


    /**
     * Test of doOperation method, of class StructuralMutation.
     */
    @Test
    public void testDoOperation_Individual() {
        System.out.println("Structural muation doOperation");
        //Integer.MAX_VALUE
        GEIndividual operand = IndividualMaker.makeIndividual(p);
        int[] chromosome = {0,1,2,1,2,Integer.MIN_VALUE,Integer.MAX_VALUE,0,2,1,1,1};
        int[] expected = {111352301,1,2,1,2,-2147483648,2147483647,0,2,1,1,1};
        GEChromosome geChromosome = (GEChromosome)operand.getGenotype().get(0);
        geChromosome.setAll(chromosome);

        StructuralMutation instance = new StructuralMutation(0.5, new MersenneTwisterFast(2));
        instance.doOperation(operand);

        JUnitHelper.checkArrays(expected, geChromosome.data);
        GEIndividualTest.testInvalidated(operand);

        //Null
        operand = IndividualMaker.makeIndividual(p);
        geChromosome = null;
        try {
            instance.doOperation(operand);
        } catch(NullPointerException e) {
            assertTrue(true);
        }
        GEIndividualTest.testInvalidated(operand);
    }
}