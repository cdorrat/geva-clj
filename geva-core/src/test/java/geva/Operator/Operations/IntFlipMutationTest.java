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

import geva.Helpers.IndividualMaker;
import geva.Helpers.JUnitHelper;
import geva.Individuals.GEChromosome;
import geva.Individuals.GEIndividual;
import geva.Individuals.GEIndividualTest;
import geva.Operator.Operations.IntFlipMutation;
import geva.Util.Random.MersenneTwisterFast;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author erikhemberg
 */
public class IntFlipMutationTest {

    public IntFlipMutationTest() {
    }

    /**
     * Test of doOperation method, of class IntFlipMutation.
     */
    @Test
    public void testDoOperation_Individual() {
    System.out.println("*IntFlipMutationTest: doOperation");
        //Integer.MAX_VALUE
        GEIndividual operand = IndividualMaker.makeIndividual();
        int[] chromosome = {0,2,Integer.MIN_VALUE,Integer.MAX_VALUE,0,2};
        int[] expected = {0, 2, Integer.MIN_VALUE, Integer.MAX_VALUE,1520873195,1277901399};
        GEChromosome geChromosome = (GEChromosome)operand.getGenotype().get(0);
        geChromosome.setAll(chromosome);
        IntFlipMutation instance = new IntFlipMutation(0.5, new MersenneTwisterFast(0));
        instance.doOperation(operand);
        JUnitHelper.checkArrays(expected, geChromosome.data);
        GEIndividualTest.testInvalidated(operand);

        //Null
        operand = IndividualMaker.makeIndividual();
        geChromosome = null;
        try {
            instance.doOperation(operand);
        } catch(NullPointerException e) {
            assertTrue(true);
        }
        GEIndividualTest.testInvalidated(operand);
    }

}