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


import geva.Individuals.*;
import geva.Individuals.FitnessPackage.BasicFitness;
import geva.Individuals.Populations.Population;
import geva.Individuals.Populations.SimplePopulation;
import geva.Mapper.GEGrammar;
import geva.Operator.Operations.Operation;
import geva.Operator.Operations.SelectionOperation;
import geva.Operator.Operations.TournamentSelect;
import geva.Util.Random.MersenneTwisterFast;
import geva.Util.Random.RandomNumberGenerator;

import java.util.Iterator;
import java.util.Properties;

/**
 * SelectionScheme has a SelectionOperation
 * This opertor is used for selecting from the population
 * @author Blip
 */
public class SelectionScheme extends SplitOperator{
    
    /** Creates a new instance of SelectionScheme
     * @param rng random number generator
     * @param size size
     * @param op operation
     */
    @SuppressWarnings({"SameParameterValue"})
    public SelectionScheme(RandomNumberGenerator rng, int size, Operation op){
        super(rng, size, op);
        SelectionOperation sOp = (SelectionOperation)op;
        this.destinationPopulation = sOp.getSelectedPopulation();
    }
    
    /** Creates a new instance of SelectionScheme
     * @param rng random number generator
     * @param op operation
     */
    public SelectionScheme(RandomNumberGenerator rng, Operation op){
        super(rng, ((SelectionOperation)op).getSize(), op);
        SelectionOperation sOp = (SelectionOperation)op;
        this.destinationPopulation = sOp.getSelectedPopulation();
    }

    /**
     * Set properties
     *
     * @param p object containing properties
     */
    public void setProperties(Properties p) {
        
    }

    public void perform() {
        super.operation.doOperation(super.population.getAll());
        //System.out.println("ops:"+this.population.size()+" "+this.population);
        //System.out.println("sps:"+this.destinationPopulation.size()+" "+this.destinationPopulation);
    }
    
    public void setOperation(Operation op) {
        this.operation = op;
    }
    
    public Operation getOperation() {
        return this.operation;
    }
    
    /**
     * Returns the selected population.
     * @return Selected population
     **/
    public Population getPopulation() {
        return this.destinationPopulation;
    }
    
    public static void main(String[] Args) {
        Population p = new SimplePopulation();
        Population selected;
        
        for(int i=0;i<100;i++) {
        GEIndividual ind = new GEIndividual();
        Chromosome c = new GEChromosome(100);
        Genotype g = new Genotype();
        g.add(c);
        ind.setGenotype(g);
        ind.setPhenotype(new Phenotype());
        ind.setMapper(new GEGrammar());
        
        BasicFitness f = new BasicFitness((double)i, ind );
        //f.setSortDirection(true);
        ind.setFitness(f);
        p.add(ind);
        }
        
        MersenneTwisterFast m = new MersenneTwisterFast();
        TournamentSelect t = new TournamentSelect(50,3,m);
        SelectionScheme selecta = new SelectionScheme(m,100,t);
        
        
        selecta.setPopulation(p);
        int tests = 100;
        double totalavg = 0;
        //long tstart = System.currentTimeMillis();
        for(int i = 0;i<tests;i++){
        selecta.perform();
        selected = selecta.getPopulation();
        Iterator<Individual> pItr = selected.iterator();
        double fitsofar = 0;
        while(pItr.hasNext())
        {
            
        fitsofar += pItr.next().getFitness().getDouble();
        
        }
        double avgFitness = fitsofar / (double)p.size();
        totalavg += avgFitness;
        }
        //ttotal = System.currentTimeMillis() - tstart;
        //System.out.println("time taken for " + tests + " selection operations on a population size of 2000");
        double x = totalavg/(double)tests;
        System.out.println(x);
     }
    
}
