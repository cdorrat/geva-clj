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
import geva.Operator.Operations.EliteReplacementOperation;
import geva.Operator.Operations.Operation;
import geva.Util.Random.RandomNumberGenerator;

import java.util.Collections;
import java.util.List;

/**
 * EliteReplacementOperator has a EliteReplacementOperation.
 * Use this module for Elite replacement
 * @author Blip
 */
public class EliteReplacementOperator extends JoinOperator {
    
    private EliteReplacementOperation replacementOperation;
    
    /**
     * Creates a new instance of EliteReplacementOperator
     * @param rng random number generator
     * @param incPop incomming population
     * @param rO replacement operation
     */
    public EliteReplacementOperator(RandomNumberGenerator rng, Population incPop, EliteReplacementOperation rO){
        super(rng, incPop);
        this.replacementOperation = rO;
    }

    /**
     * Remove worst individuals form the elite population.
     * Add the elites to the original population.
     * Remove the worst individuals from the origninal population.
     **/
    public void perform() {
        //System.out.print("+OE:"+this.incomingPopulation+" => ");
        this.replacementOperation.doOperation(this.incomingPopulation.getAll());
        //System.out.print("+NE:"+this.incomingPopulation+" ");
        //for(int i=0;i<this.incomingPopulation.size();i++) {
            //System.out.println(this.incomingPopulation.get(i).isValid());
        //}
        //System.out.println("-OP:"+this.population);
        trimPopulation(this.population.getAll());
        //System.out.println("-TP:"+this.population);
        this.population.addAll(this.incomingPopulation);
        Collections.sort(this.population.getAll());
        //System.out.println("-NP:"+this.population);
    }
    
    /**
     * Remove the worst individuals from the population.
     * @param operand List of individuals
     **/
    private void trimPopulation(List<Individual> operand) {
        Collections.sort(operand);
        int cnt = operand.size();
        final int cut = operand.size() - this.incomingPopulation.size();
        while(cnt > cut && cnt > 0) {
            cnt--;
            operand.remove(cnt);
        }
    }
    
    public void setOperation(Operation op) {
        this.replacementOperation = (EliteReplacementOperation)op;
    }
    
    public Operation getOperation() {
        return this.replacementOperation;
    }
        
}
