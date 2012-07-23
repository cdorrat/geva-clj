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
import geva.Individuals.Individual;
import geva.Operator.Operations.TournamentSelect;
import geva.Util.Constants;
import geva.Util.Random.MersenneTwisterFast;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author erikhemberg
 */
public class TournamentSelectTest {

    List<Individual> operands;
    public TournamentSelectTest() {
    }

    @Before
    public void setUp() {
        operands = new ArrayList<Individual>();
        for (int i = 10; i > 0; i--) {
            operands.add(IndividualMaker.makeIndividual());
            operands.get(10-i).getFitness().setDouble(i);
        }
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setProperties method, of class TournamentSelect.
     */
    @Test
    public void testSetProperties() {
        System.out.println("* TournamentSelect: setProperties");
        Properties p = GrammarCreator.getProperties();
        p.setProperty(Constants.TOURNAMENT_SIZE, "4");
        TournamentSelect instance = new TournamentSelect();
        instance.setProperties(p);
        assertEquals(instance.tournamentSize, 4);

        //TOURNAMENT_SIZE <1
        p.setProperty(Constants.TOURNAMENT_SIZE, "0");
        instance = new TournamentSelect();
        instance.setProperties(p);
        assertEquals(instance.tournamentSize, 3);
    }

    /**
     * Test of doOperation method, of class TournamentSelect.
     */
    @Test
    public void testDoOperation_List() {
        System.out.println("* TournamentSelect: doOperation");
        TournamentSelect instance = new TournamentSelect(2,2,new MersenneTwisterFast(0));
        instance.doOperation(operands);
        assertEquals(instance.getSelectedPopulation().size(), 2);
        assertEquals(instance.getSelectedPopulation().get(0).getFitness().getDouble(),1.0,0.001);
        assertEquals(instance.getSelectedPopulation().get(1).getFitness().getDouble(), 4.0, 0.001);
        assertNotSame(instance.getSelectedPopulation().get(0),operands.get(9));
        assertNotSame(instance.getSelectedPopulation().get(1),operands.get(6));
    }

    /**
     * Test of getTour method, of class TournamentSelect.
     */
    @Test
    public void testGetTour() {
        System.out.println("* TournamentSelect: getTour");
        TournamentSelect instance = new TournamentSelect(2,2,new MersenneTwisterFast(0));
        instance.getTour(operands);
        assertEquals(instance.tour.size(), 2);
        assertEquals(instance.tour.get(0).getDouble(),8.0,0.001);
        assertEquals(instance.tour.get(1).getDouble(), 1.0, 0.001);
        assertSame(instance.tour.get(0),operands.get(2).getFitness());
        assertSame(instance.tour.get(1),operands.get(9).getFitness());
    }

    /**
     * Test of selectFromTour method, of class TournamentSelect.
     */
    @Test
    public void testSelectFromTour() {
        System.out.println("* TournamentSelect: selectFromTour");
        TournamentSelect instance = new TournamentSelect(2,2,new MersenneTwisterFast(0));
        instance.getTour(operands);
        instance.selectFromTour();
        assertEquals(instance.getSelectedPopulation().size(), 1);
        assertEquals(instance.getSelectedPopulation().get(0).getFitness().getDouble(), 1.0, 0.001);
        assertNotSame(instance.getSelectedPopulation().get(0),operands.get(9));
    }

}