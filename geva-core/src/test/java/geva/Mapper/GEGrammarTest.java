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
import geva.Individuals.GEChromosome;
import geva.Individuals.Phenotype;
import geva.Mapper.ContextFreeGrammar;
import geva.Mapper.GEGrammar;
import geva.Util.Constants;

import java.util.Properties;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author erikhemberg
 */
public class GEGrammarTest {

    GEGrammar geg;
    Properties p;

    public GEGrammarTest() {
    }

    @Before
    public void setUp() {
        p = GrammarCreator.getProperties();
        p.setProperty(Constants.DERIVATION_TREE,"geva.Mapper.DerivationTree");
        geg = GrammarCreator.getGEGrammar(p);
        geg.setPhenotype(new Phenotype());
        GEChromosome chrom = GrammarCreator.getGEChromosome();
        geg.setGenotype(chrom);
    }

    /**
     * Test of constructor GEGrammar, of class GEGrammar
     */
    @Test
    public void testGEGrammar() {
        System.out.println("* GEGrammarTest: GEGrammar");
        GEGrammar geg2 = new GEGrammar();
        assertEquals(1, geg2.getMaxWraps());
    }

    /**
     * Test of constructor GEGrammar, of class GEGrammar
     * @param properties
     */
    @Test
    public void testGEGrammar_Properties() {
        System.out.println("* GEGrammarTest: GEGrammar(Properties p)");
        // use the setup
        assertEquals(4, geg.getMaxWraps());
        assertEquals("geva.Mapper.DerivationTree",geg.getDerivationString());
    }

    /**
     * Test of constructor GEGrammar, of class GEGrammar
     * @param file
     */
    @Test
    public void testGEGrammar_String() {
        System.out.println("* GEGrammarTest: GEGrammar(String s)");
        GEGrammar geg2 = new GEGrammar();
        assertEquals(1, geg2.getMaxWraps());
    }

    /**
     * Test of constructor GEGrammar, of class GEGrammar
     * @param copy
     */
    @Test
    public void testGEGrammar_GEGrammar() {
        System.out.println("* GEGrammarTest: GEGrammar(GEGrammar copy)");
        GEGrammar copy = new GEGrammar(geg);
        assertEquals(copy.getMaxWraps(), geg.getMaxWraps());
        assertEquals(copy.getMaxDerivationTreeDepth(), geg.getMaxDerivationTreeDepth());
        assertEquals(copy.getMaxCurrentTreeDepth(), geg.getMaxCurrentTreeDepth());
        assertNotSame(copy, geg);
        assertEquals(copy, geg);
        assertNull(copy.getPhenotype());
        assertNull(copy.getGenotype());

    }

    /**
     * Test of setProperties method, of class GEGrammar.
     */
    @Test
    public void testSetProperties() {
        System.out.println("* GEGrammarTest: setProperties");
        GEGrammar instance = new GEGrammar();
        instance.setProperties(p);
        //See if it loads a grammar file
        assertNotNull(instance.getRules());
        assertEquals(4, instance.getMaxWraps());
        assertEquals(Integer.parseInt(Constants.DEFAULT_MAX_DERIVATION_TREE_DEPTH), instance.getMaxDerivationTreeDepth());

        //Null
        p = null;
        instance = new GEGrammar();
        try {
            instance.setProperties(p);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    /**
     * Test of genotype2Phenotype method, of class GEGrammar.
     */
    @Test
    public void testGenotype2Phenotype_0args() {
        System.out.println("* GEGrammarTest: genotype2Phenotype");
        GEGrammar instance = new GEGrammar();
        boolean expResult = true;
        boolean result = instance.genotype2Phenotype();
        assertEquals(expResult, result);
    }

    /**
     * Test of clear method, of class GEGrammar.
     */
    @Test
    public void testClear() {
        System.out.println("* GEGrammarTest: clear");
        geg.clear();
        assertEquals(0, geg.getRules().size());
        //null returned when rules are empty in GEGrammar
        try {
            assertEquals(null, geg.getStartSymbol());
        } catch (AssertionError e) {
            assertTrue(true);
        }
        assertFalse(geg.getValidGrammar());
    }

    /**
     * Test of genotype2Phenotype method, of class GEGrammar.
     */
    @Test
    public void testGenotype2Phenotype_boolean() {
        System.out.println("* GEGrammarTest: genotype2Phenotype");
        //true
        boolean b = false;
        GEGrammar instance = new GEGrammar();
        boolean expResult = true;
        boolean result = instance.genotype2Phenotype(b);
        assertEquals(expResult, result);

        //false
        expResult = true;
        b = true;
        geg.genotype2Phenotype(b);
        assertTrue(geg.getPhenotype().getStringNoSpace().equals("a"));
        assertEquals(3, geg.getUsedCodons());
        assertEquals(1, geg.getUsedWraps());
        assertEquals(3, geg.getMaxCurrentTreeDepth());
        assertTrue(geg.getName().equals("0[0[0[]]]"));
    }

    /**
     * Test of getMaxChromosomeLengthByDepth method, of class GEGrammar.
     */
    @Test
    public void testGetMaxChromosomeLengthByDepth() {
        System.out.println("* GEGrammarTest: getMaxChromosomeLengthByDepth");
        //Default value
        int expResult = Integer.MAX_VALUE;
        geg.setMaxDerivationTreeDepth(Integer.MAX_VALUE);
        int result = geg.getMaxChromosomeLengthByDepth();
        assertEquals(expResult, result);

        //Set value
        geg.setMaxDerivationTreeDepth(4);
        assertEquals(15, geg.getMaxChromosomeLengthByDepth());
    }

    /**
     * Test of equals method, of class GEGrammar.
     */
    @Test
    public void testEquals() {
        System.out.println("* GEGrammarTest: equals");
        //obj==null
        Object obj = null;
        GEGrammar instance = new GEGrammar();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);

        //obj.getClass() != getClass()
        ContextFreeGrammar cfg = new ContextFreeGrammarMock();
        assertEquals(false, geg.equals(cfg));

        //name.equals(that.name) == true name != that.name
        instance = new GEGrammar(geg);
        instance.setPhenotype(new Phenotype());
        int[] ia = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        GEChromosome chrom = new GEChromosome(10, ia);
        chrom.setMaxChromosomeLength(10);
        instance.setGenotype(chrom);
        geg.genotype2Phenotype(true);
        instance.genotype2Phenotype(true);
        assertEquals(true, geg.equals(instance));
        assertEquals(false, geg.getName() == instance.getName());

        //name.equals(that.name) == false
        instance = new GEGrammar(geg);
        instance.setPhenotype(new Phenotype());
        ia[0] = 1;
        chrom = new GEChromosome(10, ia);
        chrom.setMaxChromosomeLength(10);
        instance.setGenotype(chrom);
        geg.genotype2Phenotype(true);
        instance.genotype2Phenotype(true);
        assertEquals(false, geg.equals(instance));


        //name == null
        instance = new GEGrammar(geg);
        assertEquals(true, instance.equals(geg));
        assertNotSame(instance, geg);
    }

    /**
     * Test of hashCode method, of class GEGrammar.
     */
    @Test
    public void testHashCode() {
        System.out.println("* GEGrammarTest: hashCode");
        GEGrammar instance = new GEGrammar();
        //Uninitiated
        int expResult = 0;
        int result = instance.hashCode();
        assertEquals(expResult, result);

        //Mapped
        geg.genotype2Phenotype(true);
        assertEquals(1126371036, geg.hashCode());
    }

    @Test
    public void testGetGrammar() {
        System.out.println("* GEGrammarTest: getGrammar");
        //Attributegegrammar
        GEGrammar pageg = GrammarCreator.getGEGrammar(p);
        GEGrammar expec = GEGrammar.getGrammar(pageg);
        assertEquals(true, expec instanceof GEGrammar);
    }

}