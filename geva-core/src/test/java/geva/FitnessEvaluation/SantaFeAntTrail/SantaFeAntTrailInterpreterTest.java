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

package geva.FitnessEvaluation.SantaFeAntTrail;

import geva.FitnessEvaluation.SantaFeAntTrail.SantaFeAntTrailInterpreter;
import geva.FitnessEvaluation.SantaFeAntTrail.Trail;
import geva.Helpers.IndividualMaker;
import geva.Individuals.GEIndividual;
import geva.Individuals.Phenotype;
import geva.Mapper.Symbol;
import geva.Util.Enums;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author erikhemberg
 */
public class SantaFeAntTrailInterpreterTest {

    public SantaFeAntTrailInterpreterTest() {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testGetTrail() {
        System.out.println("* SantaFeAntTrailInterpreter: getTrail");
//SFA
        SantaFeAntTrailInterpreter instance = new SantaFeAntTrailInterpreter();
        String trail_type = "geva.FitnessEvaluation.SantaFeAntTrail.Trail";
        Trail trail = instance.getTrail(trail_type);
        assertEquals(true, trail.getClass().getName().equals(trail_type));

     //SM
        instance = new SantaFeAntTrailInterpreter();
        trail_type = "geva.FitnessEvaluation.SantaFeAntTrail.SanMateoTrail";
        trail = instance.getTrail(trail_type);
        assertEquals(true, trail.getClass().getName().equals(trail_type));

     //LA
        instance = new SantaFeAntTrailInterpreter();
        trail_type = "geva.FitnessEvaluation.SantaFeAntTrail.LosAltosTrail";
        trail = instance.getTrail(trail_type);
        assertEquals(true, trail.getClass().getName().equals(trail_type));

        //Exception
        instance = new SantaFeAntTrailInterpreter();
        trail_type = "geva.FitnessEvaluation.SantaFeAntTrail.Grr";
        trail = null;
        try {
            trail = instance.getTrail(trail_type);
        } catch(IllegalArgumentException e) {
            assertTrue(true);
        }
        assertEquals(true, trail == null);
    }

    /**
     * Test of getFitness method, of class SantaFeAntTrailInterpreter.
     */
    @Test
    public void testGetFitness() {
        System.out.println("*SantaFeAntTrailInterpreter: getFitness");
        GEIndividual individual = IndividualMaker.makeIndividual();
        Phenotype p = IndividualMaker.getPhenotype("move();");
        individual.setPhenotype(p);
        SantaFeAntTrailInterpreter instance = new SantaFeAntTrailInterpreter();
        instance.setTrail_type("geva.FitnessEvaluation.SantaFeAntTrail.Trail");
        instance.getFitness(individual);
        assertEquals((int) individual.getFitness().getDouble(), 86);

        individual = IndividualMaker.makeIndividual();
        p.clear();
        p.add(new Symbol(SantaFeAntTrailInterpreter.IF,Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.IF, Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.MOVE, Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.ELSE, Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.LEFT, Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.END_IF, Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.ELSE, Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.RIGHT, Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.END_IF, Enums.SymbolType.TSymbol));
        individual.setPhenotype(p);
        instance = new SantaFeAntTrailInterpreter();
        instance.setTrail_type("geva.FitnessEvaluation.SantaFeAntTrail.Trail");
        instance.getFitness(individual);
        assertEquals((int) individual.getFitness().getDouble(), 78);

        individual = IndividualMaker.makeIndividual();
        p.clear();
        p.add(new Symbol(SantaFeAntTrailInterpreter.IF,Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.MOVE, Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.ELSE, Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.LEFT, Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.MOVE, Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.RIGHT, Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.END_IF, Enums.SymbolType.TSymbol));
        individual.setPhenotype(p);
        instance = new SantaFeAntTrailInterpreter();
        instance.setTrail_type("geva.FitnessEvaluation.SantaFeAntTrail.Trail");
        instance.getFitness(individual);
        assertEquals((int) individual.getFitness().getDouble(), 72);

individual = IndividualMaker.makeIndividual();
        p.clear();
        p.add(new Symbol(SantaFeAntTrailInterpreter.IF,Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.MOVE, Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.ELSE, Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.IF,Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.RIGHT, Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.LEFT, Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.ELSE, Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.RIGHT, Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.END_IF, Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.IF,Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.RIGHT, Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.ELSE, Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.MOVE, Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.END_IF, Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.END_IF, Enums.SymbolType.TSymbol));
        p.add(new Symbol(SantaFeAntTrailInterpreter.END_IF, Enums.SymbolType.TSymbol));
        individual.setPhenotype(p);
        instance = new SantaFeAntTrailInterpreter();
        instance.setTrail_type("geva.FitnessEvaluation.SantaFeAntTrail.Trail");
        instance.getFitness(individual);
        assertEquals((int) individual.getFitness().getDouble(), 85);

    }
}