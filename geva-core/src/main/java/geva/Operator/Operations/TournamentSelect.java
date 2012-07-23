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

package geva.Operator.Operations;


import geva.Exceptions.BadParameterException;
import geva.Individuals.Individual;
import geva.Individuals.FitnessPackage.Fitness;
import geva.Util.Constants;
import geva.Util.Random.RandomNumberGenerator;
import geva.Util.Random.Stochastic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * The operation of this class is tournament selection.
 * The individual with the best fitness from a randomly
 * selected tournament of size tournamentSize is cloned to the selected population.
 **/
public class TournamentSelect extends SelectionOperation implements Stochastic {
	private static Log logger = LogFactory.getLog(TournamentSelect.class);
    protected RandomNumberGenerator rng;
    protected int tournamentSize;
    protected double pressureModifier; // p=1 for deterministic selection
    protected ArrayList<Fitness> tour;
    
    /** Creates a new instance of TournamentSelect
     * @param size size of selected population
     * @param tourSize size of tournament
     * @param rand random number generator
     */
    public TournamentSelect(int size, int tourSize, RandomNumberGenerator rand) {
        super(size);
        this.rng = rand;
        this.tournamentSize = tourSize;
        tour = new ArrayList<Fitness>(tourSize);
    }
    
    /** Creates a new instance of TournamentSelect
     * @param rand random number generator
     * @param p properties
     */
    public TournamentSelect(RandomNumberGenerator rand, Properties p) {
        super(p);
        setProperties(p);
        this.rng = rand;
        tour = new ArrayList<Fitness>(this.tournamentSize);
    }

    /**
     * New instantion
     */
    public TournamentSelect() {
        super();
        tour = new ArrayList<Fitness>(this.tournamentSize);
    }
    
    public void setProperties(Properties p) {
        super.setProperties(p);
        int value;
                
        String key;
        try {
            key = Constants.TOURNAMENT_SIZE;
            value = Integer.parseInt(p.getProperty(key));
            if(value < 1) {
                throw new BadParameterException(key);
            }
        } catch(Exception e) {
            value = 3;
            logger.warn(e+" using default: "+value);
        }
        this.tournamentSize = value;
    }
    
    public void doOperation(Individual operand) {}
    
    /**
     * geva.Individuals from operands will be added to the selected population
     * if the win their tournament.
     * @param operands geva.Individuals to be selected from
     **/
    public void doOperation(List<Individual> operands) {
        this.selectedPopulation.clear();
        while(this.selectedPopulation.size()<this.size){
            getTour(operands);
            selectFromTour();
        }
    }

    /**
     * Adds individual to the tournament by randomly selecting from
     * the operands untill the tounramentSize is reached.
     * @param operands geva.Individuals that can be selected to the tournament
     **/
    public void getTour(List<Individual> operands) {
        tour.clear();
        int contestant;
        for(int i = 0;i<this.tournamentSize;i++) {
            contestant = this.rng.nextInt(operands.size());
            tour.add(operands.get(contestant).getFitness());
        }
    }
    
    /**
     * Select a winner from the tournament and add to the selected population.
     **/
    public void selectFromTour() {
	    Collections.sort(tour);
	    this.selectedPopulation.add(tour.get(0).getIndividual().clone());

    }

    public void setRNG(RandomNumberGenerator m) {
            this.rng =m;
    }

    public RandomNumberGenerator getRNG() {
        return this.rng;
    }

}

