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
 * IntFlipMutation.java
 *
 * Created on 15 March 2007, 20:05
 *
 */

package geva.Operator.Operations;


import geva.Individuals.GEChromosome;
import geva.Individuals.GEIndividual;
import geva.Individuals.Individual;
import geva.Util.Random.RandomNumberGenerator;

import java.util.List;
import java.util.Properties;

/**
 * IntFlipMutation does integer mutation. This method
 * changes the integer into a byte before mutation so as to show the 
 * effects of locality on the mutation operation
 * @author Conor
 */
public class IntFlipByteMutation extends MutationOperation {
    
    /** Creates a new instance of IntFlipMutation
     * @param prob mutation probability
     * @param rng random number generator
     */
    public IntFlipByteMutation(double prob,RandomNumberGenerator rng) {
        super(prob, rng);
    }

    /**
     * New instance
     * @param rng random number generator
     * @param p properties
     */
    public IntFlipByteMutation(RandomNumberGenerator rng, Properties p) {
        super(rng, p);
    }
    
    public void doOperation(List<Individual> operands) {
        for (Individual operand : operands) {
            this.doOperation(operand);

        }
    }
    
    /**
     * Calls doMutation(GEIndividual c) and then calls Individual.invalidate()
     * @param operand operand to operate on
     */
    public void doOperation(Individual operand) {
        doMutation((GEChromosome)operand.getGenotype().get(0));
        ((GEIndividual)operand).invalidate();
    }
    
    /**
     * According to this.probability a codon in the chromosome is  
     * replaced with a new randomly chosen integer
     * @param c input to mutate
     */
    private void doMutation(GEChromosome c) 
    {
        for(int i=0;i<c.getLength();i++) 
        {         
        int mut = 0;

        for(int ii=0; ii<c.getCodonSize(); ii++) {
            //this is where the integer is turned into a byte array
            for(int j=0; j<Byte.SIZE; j++) 
            {
                if(this.rng.nextBoolean(this.probability)) {
                mut = mut + (int)Math.pow(2,j*(ii+1));              
                }
            }
        }
        c.set(i,c.get(i)^mut);
        }       
    }
}