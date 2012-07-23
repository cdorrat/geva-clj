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
import geva.Util.Random.RandomNumberGenerator;
import geva.Util.Random.Stochastic;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * This is an abstract class for creating Roulette wheel selection
 * methods with different distributions
 * @author jbyrne
 */
public abstract class RouletteWheel extends SelectionOperation implements Stochastic {

    protected RandomNumberGenerator rng;
    protected double minFit;
    protected double sumFit;
    protected boolean smallFit = true;
    protected double[] accProbs;

    public RouletteWheel(int size, RandomNumberGenerator rng) {
    super(size);
    this.rng = rng;
    }

    /**
    * New instance
    */
    public RouletteWheel() {
        super();
    }

    @Override
    public void setProperties(Properties p) {
        super.setProperties(p);
    }

    @Override
    public void doOperation(Individual operand) {
    }


    public void doOperation(List<Individual> operands) {
        rankPopulation(operands);
        calculateFitnessSum(operands);
        calculateAccumulatedFitnessProbabilities(operands);
        spinRoulette(operands);
    }

    /**
     * Rank the population
     * @param operands population
     */
    public void rankPopulation(List<Individual> operands) {
        Collections.sort(operands);
    }

    /**
     * Selects Indivudals from operand and adds to the selected population
     * until the selected population is full.
     * @param operands geva.Individuals to be chosen form
     **/
    protected void spinRoulette(List<Individual> operands) {
        double prob;
        Individual selected;
        this.selectedPopulation.clear();

        while(this.selectedPopulation.size()<super.getSize()) {
            prob = rng.nextDouble();

            int cnt = 0;        
            while(cnt < operands.size() && this.accProbs[cnt] < prob) {
                cnt++;
            }
            if(cnt >= operands.size()) {
		//                System.out.println("Doh:"+cnt);
                cnt = operands.size() - 1; //If the selction with the roulette fails, take the last individual
            }
            selected = operands.get(cnt);   
            this.selectedPopulation.add(selected.clone());

        }
    }

    protected abstract void calculateAccumulatedFitnessProbabilities(List<Individual> operands);

     /**
     * Calculate the fitness sum.
     * Get the minimum fitness.
     * If fitness is NaN or Infinite Double.MAX_VAALUE is assigned
     * @param c List of individuals which fitness is taken into account
     **/
    protected void calculateFitnessSum(List<Individual> c) {
        double sum = 0;
        double tmp;
        Iterator<Individual> itI = c.iterator();
        this.minFit = Double.MAX_VALUE;
        while(itI.hasNext()) {
            tmp = itI.next().getFitness().getDouble();
            if (tmp > 1)
            {
                this.smallFit = false;
            }

            sum += tmp;
            if(this.minFit < tmp) {
                this.minFit = tmp;
            }
        }
        this.sumFit = sum;
    }

    public RandomNumberGenerator getRNG() {
        return this.rng;
    }

    public void setRNG(RandomNumberGenerator m) {
        this.rng = m;
    }


}
