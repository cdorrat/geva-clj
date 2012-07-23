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
import geva.Operator.Operations.EliteReplacementOperation;
import geva.Util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author erikhemberg
 */
public class EliteReplacementOperationTest {

    public EliteReplacementOperationTest() {
    }

    /**
     * Test of setProperties method, of class EliteReplacementOperation.
     */
    @Test
    public void testSetProperties() {
        System.out.println("* EliteReplacementOperation: setProperties");
        Properties p = GrammarCreator.getProperties();
        p.setProperty(Constants.ELITE_SIZE, "1");
        EliteReplacementOperation instance = new EliteReplacementOperation(0);
        instance.setProperties(p);
        assertEquals(instance.getEliteSize(), Integer.parseInt(p.getProperty(Constants.ELITE_SIZE)));

        //ELITE_SIZE -1
        instance = new EliteReplacementOperation(0);
        p.setProperty(Constants.ELITE_SIZE, "-1");
        instance.setProperties(p);
        assertEquals(instance.getEliteSize(), 0);
    }

    /**
     * Test of doOperation method, of class EliteReplacementOperation.
     */
    @Test
    public void testDoOperation_List() {
        System.out.println("* EliteReplacementOperation: doOperation");
        List<Individual> operands = new ArrayList<Individual>();
        operands.add(IndividualMaker.makeIndividual());
        operands.get(0).getFitness().setDouble(2.0);
        operands.get(0).setValid(true);
        operands.get(0).setEvaluated(true);
        operands.add(IndividualMaker.makeIndividual());
        operands.get(1).getFitness().setDouble(1.0);
        operands.get(1).setValid(true);
        operands.get(1).setEvaluated(true);
        Individual ind = operands.get(1);
        EliteReplacementOperation instance = new EliteReplacementOperation(1);
        instance.doOperation(operands);
        assertEquals(instance.getEliteSize(),1);
        assertEquals(operands.get(0).getFitness().getDouble(),1.0,0.001);
        assertSame(ind,operands.get(0));
       }

}