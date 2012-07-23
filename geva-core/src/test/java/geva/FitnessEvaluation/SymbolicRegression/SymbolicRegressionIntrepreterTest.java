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
package geva.FitnessEvaluation.SymbolicRegression;

import geva.FitnessEvaluation.SymbolicRegression.Range;
import geva.FitnessEvaluation.SymbolicRegression.SymbolicRegressionIntrepreter;
import geva.Helpers.IndividualMaker;
import geva.Helpers.JUnitHelper;
import geva.Individuals.GEIndividual;
import geva.Individuals.FitnessPackage.BasicFitness;
import geva.Mapper.Symbol;
import geva.Util.Enums;
import geva.Util.Random.MersenneTwisterFast;

import java.util.Properties;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author erikhemberg
 */
public class SymbolicRegressionIntrepreterTest {

    GEIndividual gei;
    SymbolicRegressionIntrepreter instance;

    public SymbolicRegressionIntrepreterTest() {
    }

    @Before
    public void setUp() {
        gei = IndividualMaker.makeIndividual();
        instance = new SymbolicRegressionIntrepreter();
        double[][] samples = {{2, 3}, {3, 4}};
        double[] x = new double[2];
        instance.setSamples(samples);
        instance.setX(x);

    }

    /**
     * Test of calculateTarget method, of class SymbolicRegressionIntrepreter.
     */
    @Test
    public void testCalculateTarget() {
        System.out.println("* SymbolicRegressionIntrepreterTest: calculateTarget");
        
        //Empty string
        String target = "";
        try {
            instance.calculateTarget(target);
        } catch(IllegalArgumentException e) {
            assertTrue(true);
        }

        //x^2
        target = "* x0 x1";
        double[] expected = {6, 12};
        instance.calculateTarget(target);
        JUnitHelper.checkArrays(instance.calculated_target, expected);
    }

    /**
     * Test of setPROPERTIES method, of class SymbolicRegressionIntrepreter.
     */
    @Test
    public void testSetProperties() {
        System.out.println("*SymbolicRegressionInterpreterTest: setProperties");
        MersenneTwisterFast mft = new MersenneTwisterFast(0);
        MersenneTwisterFast mft2 = new MersenneTwisterFast(0);
        Properties p = new Properties();
        instance = new SymbolicRegressionIntrepreter();
        instance.setRNG(mft);
        p.setProperty("sr_range", "x0 eq [0:0.5:1]; x1 eq rnd(0,2,1)");
        p.setProperty("sr_target", "+ x0 x1");
        instance.setProperties(p);
        double s1 = mft2.nextDouble();
        double s2 = mft2.nextDouble();
        double[] expectedTargets = {(0+s1),(0.5+s2)};
        double[][] expectedSamples = {{0,s1},{0.5,s2}};
        JUnitHelper.checkMatrix(expectedSamples, instance.samples);
        JUnitHelper.checkArrays(expectedTargets, instance.calculated_target);
    }

    /**
     * Test of getFitness method, of class SymbolicRegressionIntrepreter.
     */
    @Test
    public void testGetFitness() {
        System.out.println("*SymbolicRegressionInterpreterTest: getFitness");
        //Unfinished phenotype
        gei.getPhenotype().add(new Symbol("+", Enums.SymbolType.TSymbol));
        try{
            instance.getFitness(gei);
        } catch(ArrayIndexOutOfBoundsException e) {
            assertTrue(true);
        }
        assertEquals(true, BasicFitness.DEFAULT_FITNESS == gei.getFitness().getDouble());

        //Bad symbol ++
        gei = IndividualMaker.makeIndividual();
        gei.getPhenotype().add(new Symbol("++", Enums.SymbolType.TSymbol));
        try{
            instance.getFitness(gei);
        } catch(IllegalArgumentException e) {
            assertTrue(true);
        }
        assertEquals(true, BasicFitness.DEFAULT_FITNESS==gei.getFitness().getDouble());

        //+ x0 x1
        gei.getPhenotype().clear();
        gei.getPhenotype().add(new Symbol("+", Enums.SymbolType.TSymbol));
        gei.getPhenotype().add(new Symbol("x0", Enums.SymbolType.TSymbol));
        gei.getPhenotype().add(new Symbol("x1", Enums.SymbolType.TSymbol));
        double expected = (5 - 6) * (5 - 6) + (7 - 12) * (7 - 12);
        instance.calculateTarget("* x0 x1");
        instance.getFitness(gei);
        assertEquals(expected, gei.getFitness().getDouble(),0.00001);

        //- -1.5 0.5
        gei.getPhenotype().clear();
        gei.getPhenotype().add(new Symbol("-", Enums.SymbolType.TSymbol));
        gei.getPhenotype().add(new Symbol("-1.5", Enums.SymbolType.TSymbol));
        gei.getPhenotype().add(new Symbol("0.5", Enums.SymbolType.TSymbol));
        expected = Math.pow(-2 - 6,2) + Math.pow(-2 - 12, 2);
        instance.calculateTarget("* x0 x1");
        instance.getFitness(gei);
        assertEquals(expected, gei.getFitness().getDouble(),0.00001);

        // / 2 0
        gei.getPhenotype().clear();
        gei.getPhenotype().add(new Symbol("/", Enums.SymbolType.TSymbol));
        gei.getPhenotype().add(new Symbol("2", Enums.SymbolType.TSymbol));
        gei.getPhenotype().add(new Symbol("0", Enums.SymbolType.TSymbol));
        expected = Math.pow(2 - 6,2) + Math.pow(2 - 12,2);
        instance.calculateTarget("* x0 x1");
        instance.getFitness(gei);
        assertEquals(expected, gei.getFitness().getDouble(),0.00001);

        // + * x0 - x1 x0 x0
        gei.getPhenotype().clear();
        gei.getPhenotype().add(new Symbol("+", Enums.SymbolType.TSymbol));
        gei.getPhenotype().add(new Symbol("*", Enums.SymbolType.TSymbol));
        gei.getPhenotype().add(new Symbol("x0", Enums.SymbolType.TSymbol));
        gei.getPhenotype().add(new Symbol("-", Enums.SymbolType.TSymbol));
        gei.getPhenotype().add(new Symbol("x1", Enums.SymbolType.TSymbol));
        gei.getPhenotype().add(new Symbol("x0", Enums.SymbolType.TSymbol));
        gei.getPhenotype().add(new Symbol("x0", Enums.SymbolType.TSymbol));
        expected = Math.pow(4 - 6,2) + Math.pow(6 - 12,2);
        instance.calculateTarget("* x0 x1");
        instance.getFitness(gei);
        assertEquals(expected, gei.getFitness().getDouble(),0.00001);

        // / rnd x0
        gei.getPhenotype().clear();
        gei.getPhenotype().add(new Symbol("/", Enums.SymbolType.TSymbol));
        gei.getPhenotype().add(new Symbol("rnd", Enums.SymbolType.TSymbol));
        gei.getPhenotype().add(new Symbol("x0", Enums.SymbolType.TSymbol));
        MersenneTwisterFast mft = new MersenneTwisterFast(0);
        MersenneTwisterFast mft2 = new MersenneTwisterFast(0);
        instance.setRNG(mft2);
        double rndDouble = mft.nextDouble();
        double rndDouble2 = mft.nextDouble();
        expected = Math.pow(rndDouble/2 - 6, 2) + Math.pow(rndDouble2/3 - 12,2);
        instance.calculateTarget("* x0 x1");
        instance.getFitness(gei);
        assertEquals(expected, gei.getFitness().getDouble(),0.00001);

        // Random samples + x0 x1
        gei.getPhenotype().clear();
        gei.getPhenotype().add(new Symbol("+", Enums.SymbolType.TSymbol));
        gei.getPhenotype().add(new Symbol("x0", Enums.SymbolType.TSymbol));
        gei.getPhenotype().add(new Symbol("x1", Enums.SymbolType.TSymbol));
        expected = 138.85324825;
        instance.setRange(new Range("x0 eq rnd(0,2,1); x1 eq rnd(0,2,1)", new MersenneTwisterFast(0)));
        instance.getFitness(gei);
        assertEquals(expected, gei.getFitness().getDouble(),0.00001);
        expected = 135.8672430;
        instance.getFitness(gei);
        assertEquals(expected, gei.getFitness().getDouble(),0.00001);
    }

    
}