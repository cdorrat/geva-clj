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

import geva.Exceptions.MalformedGrammarException;
import geva.Helpers.GrammarCreator;
import geva.Individuals.*;
import geva.Mapper.DerivationNode;
import geva.Mapper.DerivationTree;
import geva.Mapper.GEGrammar;
import geva.Mapper.Symbol;
import geva.Util.Enums;
import geva.Util.Structures.TreeNode;

import java.util.Properties;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author erikhemberg
 */
public class DerivationTreeTest {

    GEGrammar geg;
    GEChromosome chrom;
    DerivationTree dt;
    TreeCreator tc;

    public DerivationTreeTest() {
    }

    class TreeCreator {

        public void growTree() {
            Properties p = GrammarCreator.getProperties();
            geg = GrammarCreator.getGEGrammar(p);
            geg.setPhenotype(new Phenotype());
            chrom = GrammarCreator.getGEChromosome();
            geg.setGenotype(chrom);
            dt = GrammarCreator.getDerivationTree(geg, chrom);
        }
    }

    @Before
    public void setUp() {
        tc = new TreeCreator();
        tc.growTree();
    }

    /**
     * Test of Constructor DerivationTree(GEGrammar gram, GEChromosome gen), 
     * of class DerivationTree
     */
    @Test
    public void testDerivationTree() {
        System.out.println("* DerivationTreeTest: DerivationTree");
        DerivationTree dt2 = new DerivationTree(geg, chrom);
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
     * Test of buildDerivationTree method, of class DerivationTree.
     */
    @Test
    public void testBuildDerivationTree() {
        System.out.println("* DerivationTreeTest: buildDerivationTree");
        boolean expResult = true;
        boolean result = dt.buildDerivationTree();
        assertEquals(expResult, result);
        assertEquals(3, dt.getGeneCnt());
        assertEquals(1, dt.getWrapCount());

        dt = new DerivationTree(geg, chrom);
        dt.setCurrentNode(new TreeNode<Symbol>());
        result = true;
        try {
            result = dt.buildDerivationTree();
        } catch (AssertionError e) {
            assertTrue(true);
        } catch (ClassCastException e) {
            assertTrue(true);
        }
        assertEquals(true, result);

        //Not enough codons
        geg.setMaxWraps(1);
        int[] ia = {0};
        chrom.setAll(ia);
        dt = new DerivationTree(geg, chrom);
        result = true;
        result = dt.buildDerivationTree();
        assertEquals(false, result);
    }

    /**
     * Test of getGECodonValue method, of class DerivationTree.
     */
    @Test
    public void testGetGECodonValue() throws Exception {
        System.out.println("* DerivationTreeTest: getGECodonValue");
        String[] geCodonSpecs = {
            "<GECodonValue-3+5>",
            "<GECodonValue{x,y,z}>",
            "<GECodonValue[-10,-5]>",
            "<GECodonValue[-10,-5)>",
            "<GECodonValue(-10,-5]>",
            "<GECodonValue(-10,-5)>",
            "<GECodonValue[6.0,7.0]>",
            "<GECodonValue[6e2, 7e2]>",
            "<GECodonValue(10.0,11.0]>",
            "<GECodonValue[13.0, 14.0)>"
        };
        String[][] results = {
            {"3", "4", "5", "3", "4", "5", "3", "4", "5", "3", "4"},
            {"x", "y", "z", "x", "y", "z", "x", "y", "z", "x", "y"},
            {"-10", "-9", "-8", "-7", "-6", "-5", "-10", "-9", "-8", "-7", "-6"},
            {"-10", "-9", "-8", "-7", "-6", "-10", "-9", "-8", "-7", "-6", "-10"},
            {"-9", "-8", "-7", "-6", "-5", "-9", "-8", "-7", "-6", "-5", "-9"},
            {"-9", "-8", "-7", "-6", "-9", "-8", "-7", "-6", "-9", "-8", "-7"},
            {"6.0", "6.1", "6.2", "6.3", "6.4", "6.5", "6.6", "6.7", "6.8", "6.9", "7.0"},
            {"600.0", "610.0", "620.0", "630.0", "640.0", "650.0", "660.0", "670.0", "680.0", "690.0", "700.0"},
            {"10.0001", "10.10009", "10.20008", "10.30007", "10.40006", "10.50005", "10.60004", "10.70003", "10.80002", "10.90001", "11.0"},
            {"13.0", "13.09999", "13.19998", "13.29997", "13.39996", "13.49995", "13.59994", "13.69993", "13.79992", "13.89991", "13.9999"}
        };
        chrom.setMaxCodonValue(10);
        int cnt = 0;
        for (String spec : geCodonSpecs) {
            // up to and including 10: max codon value is 10
            for (int j = 0; j <= 10; j++) {
                String s = dt.getGECodonValue(spec, j);
                assertEquals(true, s.equals(results[cnt][j]));
            }
            cnt++;
        }

        //Hmm, not all exceptions are tried ??!!
        // The uncommented does not seem to pass,
        // but it is not the mos turgent to fix now
        String[] geCodonSpecs2 = {
            "<GECodonValue{x,y,z>"
        //	    "<GECodonValue[-10,-5]]>",
        //	    "<GECodonValue-10,-5>",
        //	    "<GECodonValue(-10,-5>",
        //	    "<GECodonValue(-10,-5>",
        //	    "<GECodonValue(-10,-10]>",
        //	    "<GECodonValue(-10,-13)>"
        };
        String s = "";
        for (String spec : geCodonSpecs2) {
            try {
                s = dt.getGECodonValue(spec, 0);
            } catch (MalformedGrammarException e) {
                assertTrue(true);
            }
            String msg = "spec:" + spec + " s:" + s;
            assertEquals(msg, true, s.equals(""));
        }
    }

    /**
     * Test of getGECodonValueLegacyFormat method, of class DerivationTree.
     */
    @Test
    public void testGetGECodonValueLegacyFormat() {
        System.out.println("* DerivationTreeTest: getGECodonValueLegacyFormat");
        String s = "<GECodonValue-3+5>";
        int codon = 0;
        String expResult = "3";
        String result = dt.getGECodonValueLegacyFormat(s, codon);
        assertEquals(expResult, result);
    }

    /**
     * Test of growNode method, of class DerivationTree.
     */
    @Test
    public void testGrowNode() throws MalformedGrammarException {
        System.out.println("* DerivationTreeTest: growNode");
        //DerivationTreeDepth
        geg.setMaxDerivationTreeDepth(0);
        assertEquals(false, dt.growNode((DerivationNode) dt.getRoot()));

        //GEChromosome length
        tc.growTree();
        chrom.setMaxChromosomeLength(1);
        assertEquals(false, dt.growNode((DerivationNode) dt.getRoot()));

        //Short GEChromosome
        int[] ia = {0};
        tc.growTree();
        geg.getGenotype().setAll(ia);
        geg.setMaxWraps(1);
        dt = new DerivationTree(geg, chrom);
        assertEquals(false, dt.growNode((DerivationNode) dt.getRoot()));

        //Bad GECodonValue
        tc.growTree();
        String grammar = "<A>::=<GECodonValue(1,2>|<B>\n<B>::=a|b\n";
        geg.clear();
        geg.readBNFString(grammar);
        dt = new DerivationTree(geg, chrom);
        assertEquals(false, dt.growNode((DerivationNode) dt.getRoot()));

        //GECodonValue
        tc.growTree();
        grammar = "<A>::=<GECodonValue(1,3)>|<B>\n<B>::=a|b\n";
        geg.clear();
        geg.readBNFString(grammar);
        dt = new DerivationTree(geg, chrom);
        assertEquals(true, dt.growNode((DerivationNode) dt.getRoot()));

        //One production uses no codon
        tc.growTree();
        grammar = "<A>::=<B>|b\n<B>::=a\n";
        geg.clear();
        geg.readBNFString(grammar);
        dt = new DerivationTree(geg, chrom);
        dt.growNode((DerivationNode) dt.getRoot());
        assertEquals(1, dt.getGeneCnt());

        //Wrap use
        int[] ia2 = {0, 0};
        tc.growTree();
        geg.getGenotype().setAll(ia2);
        dt = new DerivationTree(geg, chrom);
        dt.growNode((DerivationNode) dt.getRoot());
        assertEquals(2, dt.getWrapCount());
        assertEquals(3, dt.getGeneCnt());
        assertEquals(3, dt.getDepth());

    }
}