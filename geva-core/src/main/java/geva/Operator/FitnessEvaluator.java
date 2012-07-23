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
 * FitnessEvaluator.java
 *
 * Created on April 17, 2007, 11:10 AM
 *
 */

package geva.Operator;

import geva.Operator.Operations.FitnessEvaluationOperation;
import geva.Operator.Operations.Operation;
import geva.Util.Random.RandomNumberGenerator;

/**
 * FitnessEvaluator is a module that is used to evaluate an entire populations fitness.
 * It can host many different types of fitness function
 *
 * @author erikhemberg
 */
public class FitnessEvaluator extends OperatorModule{
    
    private FitnessEvaluationOperation fitnessEvaluationOperation;
    
    /** Creates a new instance of FitnessEvaluator
     * @param rng random number generator
     * @param fEO fitness evaluation operation
     */
    public FitnessEvaluator(RandomNumberGenerator rng, FitnessEvaluationOperation fEO) {
        super(rng);
        this.fitnessEvaluationOperation = fEO;
    }
    
    public Operation getOperation() {
        return  this.fitnessEvaluationOperation;
    }
    
    /**
     * Calls doOperation(List<Individual> operands) in the
     * FitnessEvaluationOperation.
     **/
    public void perform() {
        //System.out.println("pFE:"+this.population);
        this.fitnessEvaluationOperation.doOperation(this.population.getAll());
         //System.out.println("aFE:"+this.population);
    }
    
    public void setOperation(Operation op) {
        this.fitnessEvaluationOperation = (FitnessEvaluationOperation)op;
    }
    
}
