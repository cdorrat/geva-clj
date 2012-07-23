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

package geva.Operator.Operations;

import geva.Helpers.GrammarCreator;
import geva.Individuals.Individual;
import geva.Mapper.GEGrammar;
import geva.Operator.Operations.RandomInitialiser;
import geva.Util.Constants;
import geva.Util.Random.MersenneTwisterFast;
import geva.Util.Random.RandomNumberGenerator;

import java.util.Properties;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 *
 * @author jbyrne
 */
public class RandomInitialiserTest {

    RandomInitialiser ri1;
    RandomInitialiser ri2;
    RandomNumberGenerator rng;
    GEGrammar cgeg;
    GEGrammar geg;
    Properties p;

    public RandomInitialiserTest() {
    }

    @Before
    public void setUp() {
        rng = new MersenneTwisterFast();
        p = GrammarCreator.getProperties();
	p.setProperty(Constants.MAX_WRAPS,"0");
        p.setProperty(Constants.DERIVATION_TREE, "geva.Mapper.ContextualDerivationTree");
        cgeg = new GEGrammar(p);
        ri1 = new RandomInitialiser(rng, cgeg, p);
        p.setProperty(Constants.DERIVATION_TREE, "geva.Mapper.DerivationTree");
        geg = new GEGrammar(p);
        ri2 = new RandomInitialiser(rng, geg, p);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getRNG method, of class RandomInitialiser.
     */
    @Test
    public void testGetRNG() {
        System.out.println("getRNG");
        RandomNumberGenerator result = ri1.getRNG();
        assertEquals(true, result instanceof MersenneTwisterFast);
    }

    /**
     * Test of createIndividual method, of class RandomInitialiser.
     */
    @Test
    public void testCreateIndividual() {
        System.out.println("createIndividual");

        Individual result = ri1.createIndividual();
        GEGrammar grammar = (GEGrammar) result.getMapper();
        System.out.println("the grammar is:" + grammar.getDerivationString());
        assertEquals(true, grammar.getDerivationString().equals("geva.Mapper.ContextualDerivationTree"));

        result = ri2.createIndividual();
        grammar = (GEGrammar) result.getMapper();
        System.out.println("the grammar is:" + grammar.getDerivationString());
        assertEquals(true, grammar.getDerivationString().equals("geva.Mapper.DerivationTree"));

    }
}