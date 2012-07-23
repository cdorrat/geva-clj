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
import geva.Operator.Operations.ContextSensitiveOperations.NodalMutation;
import geva.Util.Constants;
import geva.Util.GenotypeHelper;
import geva.Util.Random.MersenneTwisterFast;

import java.util.ArrayList;
import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jbyrne
 */
public class NodalMutationTest {

    Properties p;
    Properties p2;

    public NodalMutationTest() {
        p = GrammarCreator.getProperties();
        p2 = GrammarCreator.getProperties();
	p.setProperty(Constants.MAX_WRAPS,"0");
        p.setProperty(Constants.DERIVATION_TREE,"geva.Mapper.ContextualDerivationTree");
	p2.setProperty(Constants.MAX_WRAPS,"0");
        p2.setProperty(Constants.DERIVATION_TREE,"geva.Mapper.ContextualDerivationTree");
        String file_name = GrammarCreator.getGrammarFile("test_gec.bnf");
        p2.setProperty(Constants.GRAMMAR_FILE, file_name);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of doOperation method, of class NodalMutation.
     * create an instance, mutate it, see if its okay
     */
    @Test
    public void testDoOperation_Individual() {
        System.out.println("Nodal muation doOperation");
        //Integer.MAX_VALUE
        GEIndividual operand = IndividualMaker.makeIndividual(p);
        int[] chromosome = {0,1,2};
        int[] expected = {0,1,111352301};
        GEChromosome geChromosome = (GEChromosome)operand.getGenotype().get(0);
        geChromosome.setAll(chromosome);

        NodalMutation instance = new NodalMutation(0.5, new MersenneTwisterFast(2));
        instance.doOperation(operand);
        JUnitHelper.checkArrays(expected, geChromosome.data);
        GEIndividualTest.testInvalidated(operand);
        
        //test to make sure its invalidated
        geChromosome = null;
        try {
            instance.doOperation(operand);
        } catch(NullPointerException e) {
            assertTrue(true);
        }
        GEIndividualTest.testInvalidated(operand);
    }

    /**
     * Test of doOperation method, of class NodalMutation.
     * create an instance, mutate it, see if its okay
     */
    @Test
    public void testDoOperation_codonList() {
        System.out.println("Nodal mutation codonlist");
        //Integer.MAX_VALUE
        GEIndividual operand = IndividualMaker.makeIndividual(p);
        int[] chromosome = {1,1,2,1,1,2,4,6,7,8,9,9,9,0,5,4,3};
        GEChromosome geChromosome = (GEChromosome)operand.getGenotype().get(0);
        geChromosome.setAll(chromosome);
        ContextualDerivationTree tree = (ContextualDerivationTree) GenotypeHelper.buildDerivationTree(operand);
        System.out.println(tree.toString());
        ArrayList<Integer> expected = tree.getNodeCodonList();
        NodalMutation instance = new NodalMutation(0.5, new MersenneTwisterFast(2));
        instance.doOperation(operand);


        tree = (ContextualDerivationTree) GenotypeHelper.buildDerivationTree(operand);
        ArrayList<Integer> result = tree.getNodeCodonList();;
        System.out.println("expected"+expected.toString());
        System.out.println("result"+result.toString());
        System.out.println(tree.toString());
        JUnitHelper.checkArrays(expected, result);
        GEIndividualTest.testInvalidated(operand);
    }



        //this tests that it will mutate gecodonvalues
        @Test
	public void testDoOperation_GECodonValue() {
	    GEIndividual operand = IndividualMaker.makeIndividual(p2);
	    int[] chromosome = {1,2,1,1,2,2,0,0};
	    int[] expected = {1,2,1937831252,1,2,1748719057,0,0,};
	    GEChromosome geChromosome = (GEChromosome)operand.getGenotype().get(0);
	    geChromosome.setAll(chromosome);

        System.out.println("Operand:"+operand);
	    ContextualDerivationTree tree = (ContextualDerivationTree) GenotypeHelper.buildDerivationTree(operand);
	    System.out.println("BEFORE "+operand.getGenotype());
	    //FIXME Erik Commenting out string because it threw null pointer and I did not know why. And it did not seem to matter to the test what was printed??
	    //System.out.println(tree.toString());

	    NodalMutation instance = new NodalMutation(1.0, new MersenneTwisterFast(0));
	    instance.doOperation(operand);

	    tree = (ContextualDerivationTree) GenotypeHelper.buildDerivationTree(operand);
	    System.out.println("AFTER "+operand.getGenotype());
	    //	    System.out.println(tree.toString());

	    //JUnitHelper.checkArrays(expected, geChromosome.data);
	    //GEIndividualTest.testInvalidated(operand);

        }

}