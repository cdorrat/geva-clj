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
import geva.Exceptions.InitializationException;
import geva.Individuals.*;
import geva.Individuals.FitnessPackage.BasicFitness;
import geva.Individuals.FitnessPackage.Fitness;
import geva.Mapper.GEGrammar;
import geva.Mapper.Production;
import geva.Mapper.Rule;
import geva.Mapper.Symbol;
import geva.Util.Constants;
import geva.Util.Enums;
import geva.Util.Random.RandomNumberGenerator;
import geva.Util.Random.Stochastic;
import geva.Util.Structures.NimbleTree;
import geva.Util.Structures.TreeNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Class for growing individuals to the maximum derrivationTree size of maxDepth
 * @author erikhemberg
 */
public class GrowInitialiser implements CreationOperation, Stochastic {

	private static Log logger = LogFactory.getLog(GrowInitialiser.class);
	
    protected Genotype genotype;
    protected GEChromosome chromosome;
    protected RandomNumberGenerator rng;
    protected int maxDepth;
    protected int minDepth;
    protected GEGrammar grammar;
    protected int initChromSize;
    protected int extraCodons; //Used when createing ADF

    /**
     * New instance
     * @param rng random number generator
     * @param gegrammar grammatical evolution grammar
     * @param maxDepth max growth depth of tree
     */
    public GrowInitialiser(RandomNumberGenerator rng, GEGrammar gegrammar, int maxDepth) {
        this.grammar = gegrammar;
        this.maxDepth = maxDepth;
        this.minDepth = 0;
        this.initChromSize = 100; //Default initial chrom size
        this.rng = rng;
        this.extraCodons = 0;
    }

    /**
     * New instance
     * @param rng random number generator
     * @param gegrammar grammatical evolution grammar
     * @param p properties
     */
    public GrowInitialiser(RandomNumberGenerator rng, GEGrammar gegrammar, Properties p) {
        this.grammar = gegrammar;
        setProperties(p);
        this.minDepth = 0;
        this.rng = rng;
        this.extraCodons = 0;
    }

    public void setRNG(RandomNumberGenerator m) {
        this.rng = m;
    }

    public RandomNumberGenerator getRNG() {
        return this.rng;
    }

    public void setProperties(Properties p) {
        int value;
        try {
            String key = Constants.MAX_DEPTH;
            value = Integer.parseInt(p.getProperty(key));
            if (value < 1) {
                throw new BadParameterException(key);
            }
        } catch (Exception e) {
            value = 10;
            logger.warn(e+" using default: "+value, e);
        }
        this.maxDepth = value;
        String std = Constants.DEFAULT_CHROMOSOME_SIZE;
        String key = Constants.INITIAL_CHROMOSOME_SIZE;
        try {
            value = Integer.parseInt(p.getProperty(key, std));
            if (value < 1) {
                throw new BadParameterException(key);
            }
        } catch (Exception e) {
            value = Integer.parseInt(std);
            logger.warn(e+" for "+key+" using default: "+value, e);
        }
        this.initChromSize = value;
    }

    /**
     *  Creates an geva.Individuals
     */
    public Individual createIndividual() {
        GEGrammar gram = GEGrammar.getGrammar(this.grammar);
        Phenotype phenotype = new Phenotype();
        int[] codons = new int[this.initChromSize];
        GEChromosome chrom = new GEChromosome(this.initChromSize, codons);
        // If the given max derivation tree depth is less than the max depth of
        // - the tree, set the max derivation tree depth to the max depth of the
        // - the tree.

        if (gram.getMaxDerivationTreeDepth() < this.maxDepth) {
            gram.setMaxDerivationTreeDepth(this.maxDepth);
        }
        chrom.setMaxChromosomeLength(gram.getMaxChromosomeLengthByDepth());
        Genotype geno = new Genotype(1, chrom);
        Fitness fitness = new BasicFitness();
        GEIndividual gei = new GEIndividual(gram, phenotype, geno, fitness);
        return gei;
    }

    /**
     * Get minimum depth of tree
     * @return minimum depth
     */
    public int getMinDepth() {
        return minDepth;
    }

    /**
     * Set minimum depth
     * @param minDepth minumum depth
     */
    public void setMinDepth(int minDepth) {
        this.minDepth = minDepth;
    }

    /**
     * Set maximum depth of tree
     * @param i max depth
     */
    public void setMaxDepth(int i) {
        this.maxDepth = i;
    }

    /**
     * Get max depth of tree
     * @return max depth
     */
    public int getMaxDepth() {
        return this.maxDepth;
    }

    public void doOperation(Individual operand) {
        GEIndividual ind = (GEIndividual) operand;
        //ind = new GEIndividual(ind);
        ind.setGenotype(this.getGenotype(((GEChromosome) ind.getGenotype().get(0)).getMaxChromosomeLength()));
    //ind.getMapper().setGenotype(ind.getGenotype().get(0));

    }

    // Implement
    public void doOperation(List<Individual> operands) {
    }

    /** Creates a genotype by building a tree to the most maxDepth for one branch.
     *  WHAT TO DO IF SIZE IS LARGER THAN MAX_LENGTH*WRAPS??
     *  @return A valid Genotype
     **/
    public Genotype getGenotype(int maxLength) {
        genotype = new Genotype();
        chromosome = new GEChromosome(this.initChromSize);
        chromosome.setMaxChromosomeLength(maxLength);
        genotype.add(chromosome);
        // Initialise derrivationTree
        NimbleTree<Symbol> dt = new NimbleTree<Symbol>();
        TreeNode<Symbol> tn = new TreeNode<Symbol>();
        tn.setData(grammar.getStartSymbol());
        dt.populateStack();
        dt.setRoot(tn);
        dt.setCurrentNode(dt.getRoot());
        // Grow tree
        grow(dt);
        if (this.extraCodons > 0) {
            for (int i = 0; i < this.extraCodons; i++) {
                this.chromosome.add(this.rng.nextInt(Integer.MAX_VALUE));
            }
        }
        return genotype;
    }

    /**
     * Recursively builds a tree.
     * @param dt Tree to grow on
     * @return If the tree is valid
     **/
    public boolean grow(NimbleTree<Symbol> dt) {
        Rule rule;
        Iterator<Production> prodIt;
        ArrayList<Integer> possibleRules = new ArrayList<Integer>();
        Production prod;
        int prodVal;
        boolean result;
        int newMaxDepth;
        Iterator<Symbol> symIt;
        Symbol symbol;

        try {
            if (dt.getCurrentNode().getData().getType() == Enums.SymbolType.TSymbol) {
                //Check if it is for ADF
                if (dt.getCurrentNode().getData().getSymbolString().contains("BRR")) {
                    this.extraCodons++;
                }
                return true;
            }
            if (dt.getCurrentLevel() > this.maxDepth) {
                //System.out.println("Too deep:"+dt.getCurrentLevel()+">"+this.maxDepth);
                return false;
            }
            rule = grammar.findRule(dt.getCurrentNode().getData());
            if (rule != null) {
                prodIt = rule.iterator();
                possibleRules.clear();
                int ii = 0;
                //System.out.print(rule.getLHS().getSymbolString()+" minD:"+rule.getMinimumDepth()+" maxD:"+maxDepth+" cD:"+dt.getCurrentLevel());

                while (prodIt.hasNext()) {
                    prod = prodIt.next();
                    if ((dt.getCurrentLevel() + prod.getMinimumDepth()) <= this.maxDepth) {
                        possibleRules.add(ii);
                    }
                    ii++;
                }
                //System.out.print(" \n");
                if (possibleRules.isEmpty()) {
                    //System.out.println("EmptyPossible rules:"+rule);
                    return false;
                } else {
                    prodVal = this.rng.nextInt(possibleRules.size());
                    int modVal = possibleRules.get(prodVal);
                    int tmp1 = this.rng.nextInt((Integer.MAX_VALUE - rule.size()));
                    int tmp;
                    int mod = tmp1 % rule.size();
                    int diff;
                    if (mod > modVal) {
                        diff = mod - modVal;
                        tmp = tmp1 - diff;
                    } else {
                        diff = modVal - mod;
                        tmp = tmp1 + diff;
                    }
                    int newMod = tmp%rule.size();
                    if(newMod!=modVal) {
                        logger.info("modVal:"+modVal+" tmp1:"+tmp1+" mod:"+mod+" tmp:"+tmp+" rule.size():"+rule.size()+" newMod:"+newMod);

                    }
                    if(rule.size() > 1) {
                        this.chromosome.add(tmp); //correct choosing of production??
                        prod = rule.get(possibleRules.get(prodVal));
                    } else {
                        // only one rule does not use a codon
                        //this.chromosome.add(tmp); //correct choosing of production??
                        prod = rule.get(0);
                    }
                }
                result = true;
                newMaxDepth = dt.getDepth();
                symIt = prod.iterator();
                while (symIt.hasNext() && result) {
                    symbol = symIt.next();
                    dt.addChild(symbol);
                    dt.setCurrentNode(dt.getCurrentNode().getEnd());
                    dt.setCurrentLevel(dt.getCurrentLevel() + 1);
                    result = grow(dt);
                    dt.setCurrentLevel(dt.getCurrentLevel() - 1);
                    if (newMaxDepth < dt.getDepth()) {
                        newMaxDepth = dt.getDepth();
                    }
                }
                chromosome.setValid(result);
                dt.setDepth(newMaxDepth);
                return result;
            } else {
                if (!checkGECodonValue(dt)) {
                    throw new InitializationException("Non-existent rule, maybe GECODON not yet impelemnted. Could not find" + dt.getCurrentNode().getData().getSymbolString());
                }
                return true;
            }
        } catch(InitializationException e) {
      	  logger.error("Exception initializing", e);
            return false;
        }
    }

    /**
     * Check if it is a GECodonValue. Sapecific construct for inserting informatino into the grammar
     * @param dt tree
     * @return if it is a GECodonValue
     */
    @SuppressWarnings({"BooleanMethodIsAlwaysInverted"})
    protected boolean checkGECodonValue(NimbleTree<Symbol> dt) {
        boolean ret = false;
        if (dt.getCurrentNode().getData().getSymbolString().contains(Constants.GE_CODON_VALUE)) {
            this.chromosome.add(this.rng.nextInt(Integer.MAX_VALUE));
            ret = true;
        }
        return ret;
    }
}
