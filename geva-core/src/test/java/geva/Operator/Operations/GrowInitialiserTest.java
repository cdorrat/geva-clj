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
import geva.Individuals.GEChromosome;
import geva.Individuals.GEIndividual;
import geva.Individuals.Genotype;
import geva.Individuals.Individual;
import geva.Mapper.GEGrammar;
import geva.Mapper.Symbol;
import geva.Operator.Operations.GrowInitialiser;
import geva.Util.Constants;
import geva.Util.Enums;
import geva.Util.Random.MersenneTwisterFast;
import geva.Util.Random.RandomNumberGenerator;
import geva.Util.Structures.NimbleTree;
import geva.Util.Structures.TreeNode;

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
public class GrowInitialiserTest {

    GrowInitialiser gi1;
    GrowInitialiser gi2;
    GrowInitialiser gi3;
    RandomNumberGenerator rng;
    GEGrammar cgeg;
    GEGrammar codongeg;
    GEGrammar geg;
    Properties p;

    public GrowInitialiserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        rng = new MersenneTwisterFast();
        rng.setSeed(5);
        p = GrammarCreator.getProperties();
	p.setProperty(Constants.MAX_WRAPS,"0");
        p.setProperty(Constants.DERIVATION_TREE, "geva.Mapper.ContextualDerivationTree");
        cgeg = new GEGrammar(p);
        gi1 = new GrowInitialiser(rng, cgeg, p);
        p.setProperty(Constants.DERIVATION_TREE, "geva.Mapper.DerivationTree");
        p.setProperty(Constants.MAX_DEPTH, "8");
        geg = new GEGrammar(p);
        gi2 = new GrowInitialiser(rng, geg, p);
        p.setProperty(Constants.GRAMMAR_FILE, "src/test/resources/test_infiniteRecursive_gec.bnf");
        codongeg = new GEGrammar(p);
        gi3 = new GrowInitialiser(rng, codongeg, p);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setRNG method, of class GrowInitialiser.
     */
    @Test
    public void testSetRNG() {
        System.out.println("setRNG");
        GrowInitialiser giTest = new GrowInitialiser(rng, cgeg, p);
        giTest.setRNG(rng);
        RandomNumberGenerator result = giTest.getRNG();
        assertEquals(true, result instanceof MersenneTwisterFast);
    }

    /**
     * Test of getRNG method, of class GrowInitialiser.
     */
    @Test
    public void testGetRNG() {
        System.out.println("getRNG");
        RandomNumberGenerator result = gi1.getRNG();
        assertEquals(true, result instanceof MersenneTwisterFast);
    }

    /**
     * Test of createIndividual method, of class GrowInitialiser.
     */
    @Test
    public void testCreateIndividual() {
        System.out.println("createIndividual");
        Individual result = gi1.createIndividual();
        GEGrammar grammar = (GEGrammar) result.getMapper();
        assertEquals(true, grammar.getDerivationString().equals("geva.Mapper.ContextualDerivationTree"));

        result = gi2.createIndividual();
        grammar = (GEGrammar) result.getMapper();
        assertEquals(true, grammar.getDerivationString().equals("geva.Mapper.DerivationTree"));
    }

    /**
     * Test of getMinDepth method, of class GrowInitialiser.
     */
    @Test
    public void testGetMinDepth() {
        System.out.println("getMinDepth");
        int minResult = gi1.getMinDepth();

        assertEquals(minResult, 0);
    }

    /**
     * Test of setMinDepth method, of class GrowInitialiser.
     */
    @Test
    public void testSetMinDepth() {
        System.out.println("setMinDepth");
        int minDepth = 1;
        gi1.setMinDepth(minDepth);
        assertEquals(gi1.getMinDepth(), minDepth);
    }

    /**
     * Test of getMaxDepth method, of class GrowInitialiser.
     */
    @Test
    public void testGetMaxDepth() {
        System.out.println("getMaxDepth");
        int maxResult = gi1.getMaxDepth();
        assertEquals(maxResult, 10);
        maxResult = gi2.getMaxDepth();
        assertEquals(maxResult, 8);
    }

    /**
     * Test of setMaxDepth method, of class GrowInitialiser.
     */
    @Test
    public void testSetMaxDepth() {
        System.out.println("setMaxDepth");
        gi1.setMaxDepth(7);
        assertEquals(gi1.getMaxDepth(), 7);
    }

    /**
     * Test of doOperation method, of class GrowInitialiser.
     */
    @Test
    public void testDoOperation_Individual() {
        System.out.println("doOperation");
        GEIndividual operand = IndividualMaker.makeIndividual();
        gi1.doOperation(operand);
    }

    /**
     * Test of getGenotype method, of class GrowInitialiser.
     */
    @Test
    public void testGetGenotype() {
        System.out.println("getGenotype");
        int maxLength = 10;
        Genotype result = gi1.getGenotype(maxLength);
        assertEquals(true, result.size() < 10);
        assertEquals(true, result.size() > 0);
    }

    /**
     * Test of grow method, of class GrowInitialiser.
     */
    @Test
    public void testGrow() {
        System.out.println("grow");
        NimbleTree<Symbol> dt = new NimbleTree<Symbol>();
        TreeNode<Symbol> tn = new TreeNode<Symbol>();
        tn.setData(geg.getStartSymbol());
        dt.populateStack();
        dt.setRoot(tn);
        dt.setCurrentNode(dt.getRoot());
        int expResult = dt.getDepth();
        gi1.genotype = new Genotype();
        gi1.chromosome = new GEChromosome(100);
        gi1.chromosome.setMaxChromosomeLength(1000);
        gi1.genotype.add(gi1.chromosome);
        boolean result = gi1.grow(dt);
        assertEquals(true, result);
        assertEquals(true, expResult < dt.getDepth());

    }

    /**
     * Test of checkGECodonValue method, of class GrowInitialiser.
     */
    @Test
    public void testCheckGECodonValue() {
        System.out.println("checkGECodonValue");
        NimbleTree<Symbol> dt = new NimbleTree<Symbol>();
        TreeNode<Symbol> tn = new TreeNode<Symbol>();
        tn.setData(new Symbol((String) Constants.GE_CODON_VALUE,Enums.SymbolType.NTSymbol));
        dt.populateStack();
        dt.setRoot(tn);
        dt.setCurrentNode(dt.getRoot());
        boolean expResult = true;
        gi3.genotype = new Genotype();
        gi3.chromosome = new GEChromosome(10);
        gi3.chromosome.setMaxChromosomeLength(100);
        gi3.genotype.add(gi3.chromosome);
        boolean result = gi3.checkGECodonValue(dt);
        System.out.println("the tree is: " + dt.toString());
        assertEquals(expResult, result);
    }
}