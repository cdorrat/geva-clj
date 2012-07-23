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


import geva.Exceptions.BadParameterException;
import geva.Individuals.Individual;
import geva.Individuals.Populations.SimplePopulation;
import geva.Mapper.GEGrammar;
import geva.Operator.Operations.CreationOperation;
import geva.Operator.Operations.FullInitialiser;
import geva.Operator.Operations.GrowInitialiser;
import geva.Operator.Operations.UserSelect;
import geva.Util.Constants;
import geva.Util.Random.RandomNumberGenerator;

import java.util.List;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class for performing ramped full and grow initialization. 
 * The population is divided(ramped) between minimum depth(shortest possible depth of a derivationTree)
 * and maximum depth(Maximum allowed depth of a derivationTree). 
 * For each individual the full or the grow operation is chosen.
 */
public class RampedFullGrowInitialiser extends Initialiser {
	private static Log logger = LogFactory.getLog(RampedFullGrowInitialiser.class);    
    protected CreationOperation growInitialisation;
    protected double growProb;
    protected int currentDepth;
    protected int maxDepth;
    protected int depthInterval;

    /**
         * New instance
         * @param rng random number generator
         * @param size size
         * @param op operation
         * @param op2 operation
         * @param growProb probability of chosing grow operation
         * @param maxDepth max depth of initalised trees
         */
    public RampedFullGrowInitialiser(RandomNumberGenerator rng, int size, CreationOperation op, CreationOperation op2, double growProb, int maxDepth) {
        super(rng, size, op);
        this.growProb = growProb;
        this.currentDepth = 0;
        this.maxDepth = maxDepth;
        this.population = new SimplePopulation();
        this.growInitialisation = op2;
        for(int i=0; i<size; i++) {
            this.population.add(this.operation.createIndividual());
        }
    }

    /**
     * New instance
     */
    public RampedFullGrowInitialiser() {
        super();
    }

    @Override
    public void setProperties(Properties p) {
	super.setProperties(p);
	double value  ;
        String key = Constants.GROW_PROBABILITY;
        try {
            value = Double.parseDouble(p.getProperty(key));
            if(value < 0.0 || value > 1.0) {
                throw new BadParameterException(key);
            }
        } catch(Exception e) {
            value = 0.5;
            p.setProperty(key, Double.toString(value));
            logger.warn(e+" using default: "+value, e);
        }
        this.growProb = value;
	int valueI  ;
        key = Constants.MAX_DEPTH;
        try {
            valueI = Integer.parseInt(p.getProperty(key));
            if(valueI < 1) {
                throw new BadParameterException(key);
            }
        } catch(Exception e) {
            valueI = 10;
            p.setProperty(key, Integer.toString(valueI));
            logger.warn(e+" using default: "+valueI, e);
        }
        this.maxDepth = valueI;
    }

    /**
     * The population is divided(ramped) between minimum depth(shortest possible depth of a derivationTree)
     * and maximum depth(Maximum allowed depth of a derivationTree). 
     * For each individual the full or the grow operation is chosen.
     */
    @Override
    public void perform() {
        this.depthInterval = getDepthInterval(this.size);
        int i = 0;
        Individual individual;
        while(i<this.size) {
            individual = this.population.get(i);
            if(i%this.depthInterval==0) { //ramp up the depth
                ((FullInitialiser)operation).setMaxDepth(this.currentDepth);
                ((GrowInitialiser)growInitialisation).setMaxDepth(this.currentDepth);
                this.currentDepth++;
            }
            // Choose full or grow
            if(getRNG().nextDouble() > growProb) {
                growInitialisation.doOperation(individual);
            } else {
                operation.doOperation(individual);
            }
            i++;
            //Insert check for not having identical individuals in initialisation!!??
            //System.out.println(individual.getGenotype().get(0));
        }
    }
    
    /**
     * Calculates how the population should be partitioned between the different
     * initialisation depths
     * @param populationSize Size of the population
     * @return The interval for each depth
     **/
    public int getDepthInterval(int populationSize) {
        Individual individual = this.population.get(0);
        int depthInt;
        if(currentDepth < ((GEGrammar)individual.getMapper()).findRule(((GEGrammar)individual.getMapper()).getStartSymbol()).getMinimumDepth()) {
            //System.out.println("Min depth to small, must be larger then the minDepth of start symbol.");
            this.currentDepth = ((GEGrammar)individual.getMapper()).findRule(((GEGrammar)individual.getMapper()).getStartSymbol()).getMinimumDepth();
        }
        if(currentDepth > maxDepth) {
            logger.warn("Max depth to small, must be larger then minDepth.");
            maxDepth = currentDepth;
        }
        depthInt = populationSize/(maxDepth - currentDepth + 1);
        //        System.out.println(depthInterval+"="+populationSize+"/("+maxDepth+"-"+currentDepth+"+1)");
        if(depthInt < 1) {
            logger.warn("Too small population for initialisation on all depths");
            depthInt = 1;
        }
        return depthInt;
    }

    /**
     * First item in collection is set as fullInitalion. 
     * Second item is set as growInitialisation.
     * @param ops List of the operations to be set
     */
    public void setOperations(List<CreationOperation> ops) {
        this.operation = ops.get(0);
        this.growInitialisation = ops.get(1);
    }

 }