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

package geva.FitnessEvaluation.MultiSquares;

import geva.FitnessEvaluation.MultiSquares.PictureCopy;
import geva.Helpers.IndividualMaker;
import geva.Individuals.GEIndividual;
import geva.Mapper.Symbol;
import geva.Util.Constants;
import geva.Util.Enums;

import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jbyrne
 */
public class PictureCopyTest {

    GEIndividual gei;

    public PictureCopyTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        gei = IndividualMaker.makeIndividual();
        PictureCopy instance = new PictureCopy();
        assertEquals(true,instance !=null);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setProperties method, of class PictureCopy.
     */
    @Test
    public void testSetProperties() {
        System.out.println("setProperties");
        Properties p = new Properties();
        p.setProperty(Constants.TARGET_PHENOTYPE,"UpDwn_( -20 ) symX_( 100 ) sqr");
        PictureCopy instance = new PictureCopy();
        instance.setProperties(p);
        assertEquals(true,instance.targetFile==null);


        p.setProperty(Constants.TARGET_IMAGE,"/Users/doesntExist");
        try{
            instance.setProperties(p);
        } catch(Exception e) {
            assertTrue(true);
        }
         assertEquals(true,instance.targetFile==null);
      
    }

    /**
     * Test of canCache method, of class PictureCopy.
     */
    @Test
    public void testCanCache() {
        System.out.println("canCache");
        PictureCopy instance = new PictureCopy();
        boolean expResult = true;
        boolean result = instance.canCache();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFitness method, of class PictureCopy.
     */
    @Test
    public void testGetFitness() {
        System.out.println("getFitness");
        String target = "[ UpDwn_( -20 ) symX_( 100 ) tri ] gro_( 2 ) gro_( 2 ) UpDwn_( -20 ) UpDwn_( -20 ) LftRght_( -20 ) sqr";
        String input = "UpDwn_( -20 ) symX_( 100 ) sqr";

        Properties p = new Properties();
        p.setProperty(Constants.TARGET_PHENOTYPE,target);
        PictureCopy instance = new PictureCopy(p);
        instance.init();

        gei.getPhenotype().clear();
        gei.getPhenotype().add(new Symbol("UpDwn_( -20 )", Enums.SymbolType.TSymbol));
        gei.getPhenotype().add(new Symbol("symX_( 100 )", Enums.SymbolType.TSymbol));
        gei.getPhenotype().add(new Symbol("sqr", Enums.SymbolType.TSymbol));
        System.out.println(gei.getPhenotype().getString());
        instance.getFitness(gei);
        assertEquals(true,gei.getFitness().getDouble()==1655);
    }

}