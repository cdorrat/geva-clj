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

import geva.Individuals.Individual;
import geva.Operator.Operations.RouletteWheel;
import geva.Util.Random.RandomNumberGenerator;

import java.util.List;

/**
 *
 * @author jbyrne
 */
public class RouletteWheelMock extends RouletteWheel {

    RouletteWheelMock() {
        super();
    }

    RouletteWheelMock(int i, RandomNumberGenerator rNG) {
        super(i, rNG);
    }

    @Override
    protected void calculateAccumulatedFitnessProbabilities(List<Individual> operands) {
        this.accProbs= new double[]{.17,.33,.5};
    }
}
