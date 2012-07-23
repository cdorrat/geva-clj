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
 * GEIndividual.java
 *
 * Created on February 23, 2007, 1:54 PM
 *
 */
package geva.Individuals;


import geva.Individuals.FitnessPackage.BasicFitness;
import geva.Individuals.FitnessPackage.Fitness;
import geva.Mapper.DerivationTree;
import geva.Mapper.GEGrammar;
import geva.Mapper.Mapper;

import java.util.ArrayList;

/**
 * GEIndividual. Has a genotype, Phenotype and a GEGrammar.
 * Has fields for validity and mapped status.
 * Only has one chromosome and grammar
 * @author Blip
 */
@SuppressWarnings({"CloneDoesntCallSuperClone"})
public class GEIndividual extends AbstractIndividual {
    
    private Genotype            genotype;
    private Phenotype           phenotype;
    private GEGrammar           grammar;
    private boolean             mapped;
    private boolean             valid;
    private int                 usedCodons;
    private int                 usedWraps;
    private int                 previouslyUsedCodons;
    private boolean             previouslyValid;
    private DerivationTree      dT;

    int[] mutationPoints;
    int[] crossoverPoints;
    ArrayList<Fitness> parentsFitness;

    /** Creates a new instance of GEIndividual
     *
     */
    public GEIndividual() {
        super();
        this.mapped = false;
        this.valid = false;
	this.previouslyValid = false;
        this.usedCodons = -1;
        this.usedWraps = -1;
    }

    /**
     * Create new GEindividual instance
     * @param g mapper(grammar)
     * @param p output(phenotype)
     * @param gen input(genotype)
     * @param f fitness
     */
    public GEIndividual(GEGrammar g, Phenotype p, Genotype gen, Fitness f) {
        this.grammar = GEGrammar.getGrammar(g);
        this.phenotype = p;
        this.genotype = gen;
        this.fitness = f;
        this.evaluated = false;
        this.mapped = false;
        this.valid = false;
	this.previouslyValid = false;
        this.usedCodons = -1;
        this.usedWraps = -1;
        //Setting genotype and phenotype
        this.grammar.setPhenotype(this.phenotype);
        this.grammar.setGenotype(this.genotype.get(0));
        this.fitness.setIndividual(this);
        this.age = 0;

        this.mutationPoints = null;
        this.crossoverPoints = null;
        this.parentsFitness = null;
    }

    /**
     * Copy constructor
     * @param i individual to copy
     */
    private GEIndividual(GEIndividual i) {
        super(i);
        //Check the grammar type
        this.grammar = GEGrammar.getGrammar(i.grammar);
        this.phenotype = new Phenotype(i.phenotype);
        if (i.getGenotype() != null) {
            // FIXME this hack allows NGram inds to work with no genotype
            this.genotype = new Genotype(1);
            this.genotype.add(new GEChromosome((GEChromosome) i.getGenotype().get(0)));
            this.grammar.setGenotype(this.genotype.get(0));
        }

        this.crossoverPoints = null;
        this.mutationPoints = null;
        if (i.parentsFitness != null) {
            this.parentsFitness = new ArrayList<Fitness>(i.parentsFitness.size());
            for (Fitness f : i.parentsFitness) {
                this.parentsFitness.add(new BasicFitness(i));
            }
        }
        this.fitness = new BasicFitness(i.fitness.getDouble(), this);
        this.grammar.setPhenotype(this.phenotype);
    }

    /**
     * Factory method for creating GEIndivdual with different grammars
     * @param grammar grammar
     * @param phen phenotype
     * @param genotype genotype
     * @param fitness fitness
     * @return new GEIndividual
     */
    public static GEIndividual getIndividual(final GEGrammar grammar, final Phenotype phen, final Genotype genotype, final Fitness fitness) {
        final GEIndividual gei;
	if(grammar instanceof GEGrammar) {
	    gei = new GEIndividual(grammar, phen, genotype, fitness);
        } else {
	    throw new IllegalArgumentException(grammar+" is not GEGrammar");
	}
        return gei;
    }

    /**
     * Sets the values reset by invalidate to the values from the individual
     * passed in.
     * @param ind individual which values are taken from
     */
    public void revalidate(GEIndividual ind) {
        this.setMapped(ind.isMapped());
        this.setEvaluated(ind.isEvaluated());
        this.setUsedCodons(ind.getUsedCodons());
        this.setUsedWraps(ind.getUsedWraps());
        this.setValid(ind.isValid());
        this.setAge(ind.getAge());
    }

    /**
     * Invalidate the individual.
     */
    public void invalidate() {
        this.usedCodons = -1;
        this.usedWraps = -1;
        this.valid = false;
        this.mapped = false;
        this.evaluated = false;// All new individuals must be evaluated seperaately
        this.age = 1;
    //this.fitness.setDefault();
    }

    /**
     * Is the individual mapped
     * @return mapped
     */
    public boolean isMapped() {
        return mapped;
    }

    /**
     * Set the mapped status of the individual
     * @param mapped status of the individuals mapping
     */
    public void setMapped(boolean mapped) {
        this.mapped = mapped;
    }

    public boolean isValid() {
        return this.valid;
    }

    public void setValid(boolean b) {
        this.valid = b;
	this.previouslyValid = b;
    }

    /**
     * Find out whether individual was *previously* valid. Used in NGramUpdateOperator,
     * because it needs to know, after selection has invalidated everything, whether individuals
     * are "really" valid.
     * @return whether it was valid before being invalidated by clone().
     */
    public boolean wasPreviouslyValid() {
	return this.previouslyValid;
    }

    /**
     * Set how many codons were used
     * @param usedCodons number of codons used
     */
    public void setUsedCodons(int usedCodons) {
        this.usedCodons = usedCodons;
    }

    /**
     * Set how many wraps were used
     * @param usedWraps number of wraps used
     */
    public void setUsedWraps(int usedWraps) {
        this.usedWraps = usedWraps;
    }

    /**
     * Setting these both here so as to negate the need for multiple
     * copies of the geva.Mapper.
     * @param map index to use
     */
    public void map(int map) {
        if (!this.mapped) {
            //Clear the phenotype
            this.phenotype.clear();
            this.valid = this.grammar.genotype2Phenotype(true);
	    this.previouslyValid = this.valid;
            this.mapped = true;
            this.dT = this.grammar.getDerivationTree();
            this.usedCodons = this.grammar.getUsedCodons();
            this.usedWraps = this.grammar.getUsedWraps();
        }
    }

    /**
     * @param map phenotype to chose
     * @return output string
     */
    public String getPhenotypeString(int map) {
        return this.phenotype.getString();
    }

    public Genotype getGenotype() {
        return this.genotype;
    }

    public Mapper getMapper() {
        return this.grammar;
    }

    public void setMapper(Mapper m) {
        this.grammar = (GEGrammar) m;
    }

    /**
     * Invalidates the individual because a change has been made to the genotype.
     * Sets the new genotype in the indivudual as well as in the mapper
     * @param g genotype
     */
    public void setGenotype(Genotype g) {
        this.invalidate();
        this.genotype = g;
        this.getMapper().setGenotype(this.genotype.get(0));
    }

    public void setPhenotype(Phenotype p) {
        this.phenotype = p;
    }

    public Phenotype getPhenotype() {
        return this.phenotype;
    }

    /**
     * Clone this individual, invialidate and return the clone
     * @return Individual cloned and invalidated individual
     */
    public Individual clone() {
        //System.out.println("Before clone dT:"+this.grammar.getDerivationTree());
        GEIndividual ind = new GEIndividual(this);
        ind.setPreviouslyUsedCodons(this.usedCodons);
        ind.invalidate();
        //System.out.println("After invalidate dT:"+this.grammar.getDerivationTree());
        return ind;
    }

    @Override
    public String toString() {
        String s = "";
        if (this.phenotype != null) {
            s = this.phenotype.getString();
        }
        return s;
    }

    /**
     * Get number of codons used for mapping
     * @return codons used for mapping
     */
    public int getUsedCodons() {
        return usedCodons;
    }

    public void setPreviouslyUsedCodons(int previouslyUsedCodons) {
        this.previouslyUsedCodons = previouslyUsedCodons;
    }

    /**
     * Get number of codons used for mapping previoulsy. Used when the
     * individual gets invalidated but previous information is needed
     * @return codons used for mapping previously
     */
    public int getPreviouslyUsedCodons() {
        return this.previouslyUsedCodons;
    }

    /**
     * Get number of wraps used for mapping
     * @return wraps used
     */
    public int getUsedWraps() {
        return usedWraps;
    }

    public int[] getCrossoverPoints() {
        return crossoverPoints;
    }

    public int[] getMutationPoints() {
        return mutationPoints;
    }

    public ArrayList<Fitness> getParents() {
        return parentsFitness;
    }

    public void setCrossoverPoints(int[] crossoverPoints) {
        this.crossoverPoints = crossoverPoints;
    }

    public void setMutationPoints(int[] mutationPoints) {
        this.mutationPoints = mutationPoints;
    }

    public void setParents(ArrayList<Fitness> parents) {
        this.parentsFitness = parents;
    }
}
