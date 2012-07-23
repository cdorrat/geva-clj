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

import geva.Individuals.Populations.Population;
import geva.Operator.Operations.Operation;
import geva.Operator.Operations.ReplacementOperation;
import geva.Util.Random.RandomNumberGenerator;

/**
 * MeritReplacementStrategy joins to populations. It has a ReplacementOperation
 * The population is joined before the ReplacementOperation is performed
 * @author Blip
 */
public class MeritReplacementStrategy extends JoinOperator{
    
    protected ReplacementOperation replacementOperation;
    
    /**
     * Creates a new instance of MeritReplacementStrategy
     * @param rng random number generator
     * @param incPop incoming population
     * @param rO replacement operation
     */
    public MeritReplacementStrategy(RandomNumberGenerator rng, Population incPop, ReplacementOperation rO){
	super(rng, incPop);
        this.replacementOperation = rO;
        
    }

    /**
     * New instance
     */
    public MeritReplacementStrategy() {
        super();
    }
    
        /**
     * A ReplacementOperation is performed after the 
     * populations are joined.
     * Competition among the children if Selection Size is larger then replacement size
     * When replacement is done the age of the population is increased by one
     */
    public void perform() {
        final int populationSize = this.population.size();
        //System.out.println("ops:"+this.population.size()+" "+this.population);
        if(this.incomingPopulation.size()>this.replacementOperation.getReplacementSize()) {
            int size = this.incomingPopulation.size()-this.replacementOperation.getReplacementSize();
            this.replacementOperation.doOperation(this.incomingPopulation.getAll(), size);
        }
         //System.out.println("ips:"+this.incomingPopulation.size()+" "+this.incomingPopulation);
        this.population.addAll(this.incomingPopulation);
         //System.out.println("t-ps:"+this.population.size()+" "+this.population);
        
         
        this.replacementOperation.doOperation(this.population.getAll(), (this.population.size() - populationSize));
        this.incomingPopulation.clear();
         //System.out.println("nps:"+this.population.size()+" "+this.population);
        super.increaseAge(this.population.getAll());
    }


    public void setOperation(Operation op) {
        this.replacementOperation = (ReplacementOperation)op;
    }    

    public Operation getOperation() {
        return this.replacementOperation;
    }
    
}
