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

package geva.Operator;

import geva.Individuals.Individual;
import geva.Individuals.Populations.Population;
import geva.Operator.Operations.Operation;
import geva.Util.Random.RandomNumberGenerator;

import java.util.Iterator;
import java.util.List;

/**
 * Abstract class for joining two populations. 
 * The incomingPopulation is added to the population.
 * Eg in ReplacementOperator
 */
public abstract class JoinOperator extends OperatorModule {

    protected Population incomingPopulation;
   
    /**
     * Constructor
     * @param rng random number generator
     * @param incPop incomming population
     */
    public JoinOperator(RandomNumberGenerator rng, Population incPop){
        super(rng);
        this.incomingPopulation = incPop;
    }

    /** Create new instance*/
    public JoinOperator() {
        
    }
    
    public abstract void perform();

    public abstract void setOperation(Operation op);

    /**
     * Get the population coming in
     * @return incomming population
     */
    public Population getIncomingPopulation() {
        return incomingPopulation;
    }

    /**
     * Set the incomming population
     * @param incomingPopulation incomming population
     */
    public void setIncomingPopulation(Population incomingPopulation) {
        this.incomingPopulation = incomingPopulation;
    }


    /**
     * Increase the age of the operands by 1
     * @param operands operands for the operation
     **/
    protected void increaseAge(List<Individual> operands) {
        Iterator<Individual> iO = operands.iterator();
        Individual ind;
        //System.out.println(printPop(operands));
        while(iO.hasNext()) {
            ind = iO.next();
            int age = ind.getAge()+1;
            ind.setAge(age);
            //System.out.print(printPop(operands)+"|");
        }
        //System.out.println();
    }

}