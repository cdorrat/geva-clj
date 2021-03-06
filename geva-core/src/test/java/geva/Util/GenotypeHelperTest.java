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

package geva.Util;

import geva.Helpers.GrammarCreator;
import geva.Helpers.IndividualMaker;
import geva.Individuals.GEIndividual;
import geva.Mapper.ContextualDerivationTree;
import geva.Mapper.DerivationTree;
import geva.Util.Constants;
import geva.Util.GenotypeHelper;

import java.util.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jbyrne
 */
public class GenotypeHelperTest {

    GEIndividual gei;
    GEIndividual gei2;
    public GenotypeHelperTest() {
    }

    @Before
    public void setUp() {
        Properties p = GrammarCreator.getProperties();
	p.setProperty(Constants.MAX_WRAPS,"0");
        p.setProperty(Constants.DERIVATION_TREE,"geva.Mapper.ContextualDerivationTree");
        gei = IndividualMaker.makeIndividual(p);
        p.setProperty(Constants.DERIVATION_TREE,"geva.Mapper.DerivationTree");
        gei2 = IndividualMaker.makeIndividual(p);
    }

    @After
    public void tearDown() {
    }


    /**
     * Test of buildDerivationTree method, of class GenotypeHelper.
     */
    @Test
    public void testBuildDerivationTree_Individual() {
        System.out.println("buildDerivationTree");
        DerivationTree result = GenotypeHelper.buildDerivationTree(gei);
        assertEquals(true, result instanceof ContextualDerivationTree);
        result = GenotypeHelper.buildDerivationTree(gei2);
        assertEquals(true, result instanceof DerivationTree);
    }



}