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
package geva.Individuals;

import geva.Helpers.GrammarCreator;
import geva.Helpers.IndividualMaker;
import geva.Individuals.GEChromosome;
import geva.Individuals.GEIndividual;
import geva.Individuals.Genotype;
import geva.Individuals.Phenotype;
import geva.Individuals.FitnessPackage.BasicFitness;
import geva.Individuals.FitnessPackage.Fitness;
import geva.Mapper.GEGrammar;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author erikhemberg
 */
public class GEIndividualTest {

    GEIndividual gei;

    public GEIndividualTest() {
    }

    @Before
    public void setUp() {
        gei = IndividualMaker.makeIndividual();
    }

    /**
     * Test of constructor, of class GEIndividual
     */
    @Test
    public void testGEIndividual() {
        System.out.println("* GEIndividualTest: GEIndividual");
        GEIndividual gei = new GEIndividual();
        assertEquals(false, gei.isMapped());
        assertEquals(false, gei.isValid());
        assertEquals(-1, gei.getUsedCodons());
        assertEquals(-1, gei.getUsedWraps());
        assertEquals(false, gei.isEvaluated());
        assertNull(gei.getFitness());
    }

    /**
     * Test of constructor (GEGrammar, Phenotype, Genotype, Fitness), of class GEIndividual
     */
    @Test
    public void testGEIndividual_GEG_P_G_F() {
        System.out.println("* GEIndividualTest: GEIndividual(GEGrammar, Phenotype, Genotype, Fitness)");
        GEGrammar geg = new GEGrammar();
        Phenotype p = new Phenotype();
        Genotype geno = new Genotype();
        int[] ia = {0};
        geno.add(new GEChromosome(1, ia));
        int[] ia2 = {0};
        Genotype geno2 = new Genotype();
        geno2.add(new GEChromosome(1, ia2));
        Fitness f = new BasicFitness();
        GEIndividual gei = new GEIndividual(geg, p, geno, f);

        assertEquals(new GEGrammar(), gei.getMapper());
        assertEquals(false, new GEGrammar() == gei.getMapper());
        assertEquals(new Phenotype(), gei.getPhenotype());
        assertEquals(geno, gei.getGenotype());
        assertEquals(true, geno == gei.getGenotype());
        assertEquals(false, new BasicFitness() == gei.getFitness());
        assertEquals(true, f == gei.getFitness());
        assertEquals(false, gei.isMapped());
        assertEquals(false, gei.isValid());
        assertEquals(-1, gei.getUsedCodons());
        assertEquals(-1, gei.getUsedWraps());
        assertEquals(false, gei.isEvaluated());
        assertEquals(0, gei.getAge());
        assertEquals(new Phenotype(), gei.getMapper().getPhenotype());
        assertEquals(false, geno == gei.getMapper().getGenotype());
        assertEquals(gei, gei.getFitness().getIndividual());
        assertSame(gei, gei.getFitness().getIndividual());
    }

    /**
     * Test of invalidate method, of class GEIndividual.
     */
    @Test
    public void testInvalidate() {
        System.out.println("* GEIndividualTest: invalidate");
        GEIndividual instance = new GEIndividual();
        instance.invalidate();
        GEIndividualTest.testInvalidated(instance);
    }

    public static void testInvalidated(GEIndividual instance) {
        assertEquals(instance.getUsedCodons(), -1);
        assertEquals(instance.getUsedWraps(), -1);
        assertEquals(instance.isValid(), false);
        assertEquals(instance.isMapped(), false);
        assertEquals(instance.isEvaluated(), false);
        assertEquals(instance.age, 1);
    }

    /**
     * Test of isMapped method, of class GEIndividual.
     */
    @Test
    public void testIsMapped() {
        System.out.println("* GEIndividualTest: isMapped");
        GEIndividual instance = new GEIndividual();
        boolean expResult = false;
        boolean result = instance.isMapped();
        assertEquals(expResult, result);

    }

    /**
     * Test of map method, of class GEIndividual.
     */
    @Test
    public void testMap() {
        System.out.println("* GEIndividualTest: map");
        int map = 0;
        //Null derivationTreeType
        ((GEGrammar)gei.getMapper()).setDerivationTreeType(null);
        try {
            gei.map(map);
        } catch(NullPointerException e) {
            assertTrue(true);
        }

        //From setup
        gei = IndividualMaker.makeIndividual();
        gei.map(map);
        assertEquals(true, gei.isMapped());
        assertEquals(true, gei.isValid());
        assertEquals(3, gei.getUsedCodons());
        assertEquals(1, gei.getUsedWraps());
        assertEquals(true, gei.getPhenotype().getStringNoSpace().equals("a"));

        // different map value
        gei = IndividualMaker.makeIndividual();
        gei.map(Integer.MIN_VALUE);
        assertEquals(true, gei.isMapped());
        assertEquals(true, gei.isValid());
        assertEquals(3, gei.getUsedCodons());
        assertEquals(1, gei.getUsedWraps());
        assertEquals(true, gei.getPhenotype().getStringNoSpace().equals("a"));

        // mapped true
        gei = IndividualMaker.makeIndividual();
        gei.setMapped(true);
        gei.map(0);
        assertEquals(false, gei.isValid());
        assertEquals(true, gei.isMapped());

    }

    /**
     * Test of setGenotype method, of class GEIndividual.
     */
    @Test
    public void testSetGenotype() {
        System.out.println("* GEIndividualTest: setGenotype");
        Genotype g = new Genotype();
        g.add(GrammarCreator.getGEChromosome());
        gei.setGenotype(g);
        assertEquals(g, gei.getGenotype());
        assertEquals(g.get(0), gei.getMapper().getGenotype());
        assertSame(gei.getGenotype().get(0), gei.getMapper().getGenotype());
    }

    /**
     * Test of clone method, of class GEIndividual.
     */
    @Test
    public void testClone() {
        System.out.println("* GEIndividualTest: clone");
        gei.map(0);
        gei.getFitness().setDouble(0.0);
        GEGrammar geg = new GEGrammar();
        GEIndividual copy = (GEIndividual) gei.clone();
        assertNotSame(copy, gei);
        assertEquals(false, copy == gei);
        assertEquals(true, copy.getGenotype().get(0).toString().equals(gei.getGenotype().get(0).toString()));
        assertNotSame(copy.getGenotype(), gei.getGenotype());
        assertNotSame(copy.getFitness(), gei.getFitness());
        assertEquals(true, copy.getFitness().getDouble() == gei.getFitness().getDouble());
        assertEquals(false, copy.isMapped());
        assertEquals(false, copy.isMapped() == gei.isMapped());
        assertEquals(false, copy.isValid());
        assertEquals(false, copy.isValid() == gei.isValid());
        assertEquals(-1, copy.getUsedCodons());
        assertEquals(true, copy.getUsedCodons() != gei.getUsedCodons());
        assertEquals(-1, copy.getUsedWraps());
        assertEquals(true, copy.getUsedWraps() != gei.getUsedWraps());
        assertEquals(false, copy.isEvaluated());
        assertEquals(1, copy.getAge());
        assertEquals(true, gei.getPhenotype().getString().equals(copy.getPhenotype().getString()));
        assertNotSame(gei.getPhenotype(), copy.getPhenotype());
        assertEquals(copy, copy.getFitness().getIndividual());
        assertSame(copy, copy.getFitness().getIndividual());
        assertEquals(false, copy.getGenotype().get(0) == gei.getMapper().getGenotype());
        assertEquals(true, copy.getGenotype().get(0).toString().equals(copy.getMapper().getGenotype().toString()));
        assertEquals(gei.getPhenotype(), copy.getPhenotype());
        assertEquals(true, copy.getMapper() instanceof GEGrammar);
        assertEquals(false, copy.getMapper() == gei.getMapper());
    }

    @Test
    public void testGetIndividual() {
        System.out.println("* GEIndividualTest: getIndividual");
        GEGrammar geg = new GEGrammar();
        Phenotype p = new Phenotype();
        int[] ia2 = {0};
        Genotype geno2 = new Genotype();
        geno2.add(new GEChromosome(1, ia2));
        Fitness f = new BasicFitness();
        GEIndividual gei2 = GEIndividual.getIndividual(geg, p, geno2, f);
        assertEquals(true, gei2.getMapper() instanceof GEGrammar);
    }

    /**
     * Test of toString method, of class GEIndividual.
     */
    @Test
    public void testToString() {
        System.out.println("* GEIndividualTest: toString");
        String expResult = "";
        String result = gei.toString();
        assertEquals(expResult, result);

        //Mapped
        gei = IndividualMaker.makeIndividual();
        gei.map(0);
        result = gei.toString();
        expResult = "a ";
        assertEquals(true, result.equals(expResult));
    }
}