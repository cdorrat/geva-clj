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
import geva.Operator.Operations.SelectionOperation;
import geva.Util.Constants;

import java.util.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author erikhemberg
 */
public class SelectionOperationTest {

    public SelectionOperationTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setProperties method, of class SelectionOperation.
     */
    @Test
    public void testSetProperties() {
        System.out.println("* SelectionOperation: setProperties");
//STEADY_STATE
        Properties p = GrammarCreator.getProperties();
        SelectionOperation instance = new SelectionOperationMock();
        p.setProperty(Constants.REPLACEMENT_TYPE, Constants.STEADY_STATE);
        p.setProperty(Constants.POPULATION_SIZE, "10");
        instance.setProperties(p);
        assertEquals(instance.size,2);

        //GENERATIONAL
        p = GrammarCreator.getProperties();
        instance = new SelectionOperationMock();
        p.setProperty(Constants.REPLACEMENT_TYPE, Constants.GENERATIONAL);
        p.setProperty(Constants.POPULATION_SIZE, "10");
        instance.setProperties(p);
        assertEquals(instance.size,10);

        //SELECTINO_SIZE = 0.5
        p = GrammarCreator.getProperties();
        instance = new SelectionOperationMock();
        p.setProperty(Constants.SELECTION_SIZE, "0.5");
        p.setProperty(Constants.POPULATION_SIZE, "10");
        p.remove(Constants.REPLACEMENT_TYPE);
        instance.setProperties(p);
        assertEquals(instance.size,5);

            //SELECTINO_SIZE = -0.5
        p = GrammarCreator.getProperties();
        instance = new SelectionOperationMock();
        p.setProperty(Constants.SELECTION_SIZE, "-0.5");
        p.setProperty(Constants.POPULATION_SIZE, "10");
        p.remove(Constants.REPLACEMENT_TYPE);
        try {
            instance.setProperties(p);
        } catch(IllegalArgumentException e) {
            assertTrue(true);
        }
        assertEquals(instance.size,0);
}

}