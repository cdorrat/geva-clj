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

package geva.Mapper;

import geva.Helpers.GrammarCreator;
import geva.Helpers.IndividualMaker;
import geva.Individuals.GEChromosome;
import geva.Individuals.GEIndividual;
import geva.Individuals.Phenotype;
import geva.Mapper.ContextualDerivationTree;
import geva.Mapper.DerivationNode;
import geva.Mapper.GEGrammar;
import geva.Mapper.Symbol;
import geva.Util.Constants;
import geva.Util.Enums;
import geva.Util.GenotypeHelper;

import java.util.ArrayList;
import java.util.Properties;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jbyrne
 */
public class ContextualDerivationTreeTest {

    GEGrammar geg;
    GEChromosome chrom;
    ContextualDerivationTree dt;
    TreeCreator tc;
    Properties p;

    public ContextualDerivationTreeTest() {
        p = GrammarCreator.getProperties();
        p.setProperty(Constants.DERIVATION_TREE,"geva.Mapper.ContextualDerivationTree");
        p.setProperty(Constants.MAX_WRAPS,"0");
    }

    class TreeCreator {

        public void growTree() {
            
            geg = GrammarCreator.getGEGrammar(p);
            geg.setPhenotype(new Phenotype());
            chrom = GrammarCreator.getGEChromosome();
            System.out.println("chromosome:"+chrom);
            geg.setGenotype(chrom);
            dt = (ContextualDerivationTree)GrammarCreator.getContextualDerivationTree(geg, chrom);
        }
    }

    @Before
    public void setUp() {
        tc = new TreeCreator();
        tc.growTree();
    }

    /**
     * Test of growNode method, of class ContextualDerivationTree.
     */
    @Test
    public void testGrowNode() {
        System.out.println("* DerivationTreeTest: DerivationTree");
        ContextualDerivationTree dt2 = new ContextualDerivationTree(geg, chrom);
        DerivationNode dn = new DerivationNode();
        dn.setData(new Symbol("<string>", Enums.SymbolType.NTSymbol));
        String msg = dt2.getRoot().toString() + " " + dn.toString();
        assertEquals(
                msg,
                true,
                dn.toString().equals(dt2.getRoot().toString()));
        assertEquals(1, dt2.getWrapCount());
        assertEquals(0, dt2.getGeneCnt());
    }

    /**
     * Test of getStructCodonList method, of class ContextualDerivationTree.
     */
    @Test
    public void testGetStructCodonList() {
        System.out.println("getStructCodonList");

        GEIndividual operand = IndividualMaker.makeIndividual(p);
        int[] chromosome = {1,2,1,2,Integer.MIN_VALUE,Integer.MAX_VALUE,0,2};
        GEChromosome geChromosome = (GEChromosome)operand.getGenotype().get(0);
        Integer[] expected = {0,1,2,3,4,5};

        geChromosome.setAll(chromosome);
        ContextualDerivationTree tree = (ContextualDerivationTree) GenotypeHelper.buildDerivationTree(operand);

        ArrayList<Integer> arrayResult = tree.getStructCodonList();
        Integer[] result = arrayResult.toArray(new Integer[0]);
        assertArrayEquals(expected, result);
    }

    /**
     * Test of getNodeCodonList method, of class ContextualDerivationTree.
     */
    @Test
    public void testGetNodeCodonList() {
        System.out.println("getNodeCodonList");
        GEIndividual operand = IndividualMaker.makeIndividual(p);
        int[] chromosome = {1,2,1,2,Integer.MIN_VALUE,Integer.MAX_VALUE,0,2};
        GEChromosome geChromosome = (GEChromosome)operand.getGenotype().get(0);
        Integer[] expected = {6};
        geChromosome.setAll(chromosome);
        ContextualDerivationTree tree = (ContextualDerivationTree) GenotypeHelper.buildDerivationTree(operand);
        System.out.println(tree.toString());

        ArrayList<Integer> arrayResult = tree.getNodeCodonList();
        Integer[] result = arrayResult.toArray(new Integer[0]);
        assertArrayEquals(expected, result);
    }

}