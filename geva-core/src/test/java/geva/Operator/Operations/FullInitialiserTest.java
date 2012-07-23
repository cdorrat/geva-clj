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
import geva.Individuals.GEChromosome;
import geva.Individuals.Genotype;
import geva.Mapper.GEGrammar;
import geva.Mapper.Symbol;
import geva.Operator.Operations.FullInitialiser;
import geva.Util.Constants;
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
public class FullInitialiserTest {

    FullInitialiser fi1;
    RandomNumberGenerator rng;
    GEGrammar geg;
    Properties p;

    public FullInitialiserTest() {
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
        rng.setSeed(10);
        p = GrammarCreator.getProperties();   
        p.setProperty(Constants.DERIVATION_TREE, "geva.Mapper.DerivationTree");
        p.setProperty(Constants.MAX_DEPTH, "8");
        geg = new GEGrammar(p);
        fi1 = new FullInitialiser(rng, geg, p);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of grow method, of class FullInitialiser.
     */
    @Test
    public void testGrow() {
        NimbleTree<Symbol> dt = new NimbleTree<Symbol>();
        TreeNode<Symbol> tn = new TreeNode<Symbol>();
        tn.setData(geg.getStartSymbol());
        dt.populateStack();
        dt.setRoot(tn);
        dt.setCurrentNode(dt.getRoot());
        int expResult = dt.getDepth();
        fi1.genotype = new Genotype();
        fi1.chromosome = new GEChromosome(100);
        fi1.chromosome.setMaxChromosomeLength(1000);
        fi1.genotype.add(fi1.chromosome);
        boolean result = fi1.grow(dt);
        assertEquals(true, result);
        assertEquals(true, expResult < dt.getDepth());
    }
}