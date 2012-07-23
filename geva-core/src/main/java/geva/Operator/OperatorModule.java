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
 * OperatorModule.java
 *
 * Created on 14 March 2007, 15:40
 *
 */

package geva.Operator;

import geva.Individuals.Populations.Population;
import geva.Util.Random.RandomNumberGenerator;
import geva.Util.Random.Stochastic;

/**
 * Abstract class for Modules with operations.
 * The OperatorModule performes its operation oon a population.
 * @author Conor
 */
public abstract class OperatorModule implements Operator, Stochastic {
    
    protected RandomNumberGenerator rng;
    protected Population population;
    
    /** Creates a new instance of OperatorModule
     * @param rng random number generator
     */
    public OperatorModule(RandomNumberGenerator rng) {
        this.rng = rng;
    }
    
    /** Creates a new instance of OperatorModule */
    public OperatorModule() {
    }
    
    public abstract void perform();
    
    /**
     * Set the RandomNumberGenerator
     * @param m RandomNumberGenerator
     **/
    public void setRNG(RandomNumberGenerator m) {
        this.rng = m;
    }
    
    /**
     * Get the randomnumbergenerator
     * @return RandomNumberGenerator
     **/
    public RandomNumberGenerator getRNG() {
        return this.rng;
    }
    
    /**
     * Set the population that the module will operate on
     * @param p Population
     **/
    public void setPopulation(Population p) {
        this.population = p;
    }
    
}
