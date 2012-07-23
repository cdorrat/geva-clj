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

import geva.Exceptions.InitializationException;
import geva.Mapper.GEGrammar;
import geva.Mapper.Production;
import geva.Mapper.Rule;
import geva.Mapper.Symbol;
import geva.Util.Enums;
import geva.Util.Random.RandomNumberGenerator;
import geva.Util.Structures.NimbleTree;

import java.util.ArrayList;
import java.util.Iterator;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Create a genotype by growing a tree to maxDepth for all leaves
 * @author erikhemberg
 */
public class FullInitialiser extends GrowInitialiser {

	private static Log logger = LogFactory.getLog(FullInitialiser.class);
    /**
     * New instance
     * @param rng random number genrator
     * @param gegrammar grammatical evolution grammar (GEGrammar)
     * @param maxDepth max growth depth of tree
     */
    public FullInitialiser(RandomNumberGenerator rng, GEGrammar gegrammar, int maxDepth) {
        super(rng, gegrammar, maxDepth);
    }

    /**
     * New instance
     * @param rng random number generator
     * @param gegrammar grammatical evolution grammar (GEGrammar)
     * @param p properties
     */
    public FullInitialiser(RandomNumberGenerator rng, GEGrammar gegrammar, Properties p) {
        super(rng, gegrammar, p);
    }
    
    /**
     * Recursively builds a tree.
     * @param dt Tree to grow on
     * @return If the tree is valid
     **/
    @Override
    public boolean grow(NimbleTree<Symbol> dt) {
        Rule rule;
        Iterator<Production> prodIt;
        ArrayList<Integer> possibleRules = new ArrayList<Integer>();
        boolean recursiveRules;
        Production prod;
        int prodVal;
        boolean result;
        int newMaxDepth;
        Iterator<Symbol> symIt;
        Symbol symbol;
        
        try {
            if(dt.getCurrentNode().getData().getType() == Enums.SymbolType.TSymbol) {
                //Check if it is for ADF
		if(dt.getCurrentNode().getData().getSymbolString().contains("BRR")) {
		    this.extraCodons++;
		}
                return true;
            }
            if(dt.getCurrentLevel() > this.maxDepth) {
                logger.warn("Too deep:"+dt.getCurrentLevel()+">"+this.maxDepth);
                return false;
            }
            rule = grammar.findRule(dt.getCurrentNode().getData());
            if(rule!=null){
                prodIt = rule.iterator();
                possibleRules.clear();
                int ii = 0;
                recursiveRules = false;
                //		System.out.print(rule.getLHS().getSymbolString()+" minD:"+rule.getMinimumDepth()+" maxD:"+maxDepth+" cD:"+dt.getCurrentLevel());
                
                while(prodIt.hasNext()) {
                    prod = prodIt.next();
                    if((dt.getCurrentLevel()+prod.getMinimumDepth()) <= this.maxDepth) {
                        if(!recursiveRules && prod.getRecursive()) {
                            recursiveRules = true;
                            possibleRules.clear(); //Only recursive rules allowed? What about non-recursive rules with the proper length??
                        }
                        if(!recursiveRules || (recursiveRules && prod.getRecursive())) {
                            possibleRules.add(ii);
                        }
                    }
                    ii++;
                }
                //		System.out.print(" \n");
                if(possibleRules.isEmpty()) {
                    logger.info("EmptyPossible rules:"+rule);
                    return false;
                } else {
                    prodVal = this.rng.nextInt(possibleRules.size());
                    int modVal = possibleRules.get(prodVal);
                    int tmp1 = this.rng.nextInt((Integer.MAX_VALUE-rule.size()));
                    int tmp;
                    int mod = tmp1%rule.size();
                    int diff;
                    if(mod>modVal) {
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
                while(symIt.hasNext() && result) {
                    symbol = symIt.next();
                    dt.addChild(symbol);
                    dt.setCurrentNode(dt.getCurrentNode().getEnd());
                    dt.setCurrentLevel(dt.getCurrentLevel() + 1);
                    result = grow(dt);
                    dt.setCurrentLevel(dt.getCurrentLevel() - 1);
                    if(newMaxDepth < dt.getDepth()) {
                        newMaxDepth = dt.getDepth();
                    }
                }
                chromosome.setValid(result);
                dt.setDepth(newMaxDepth);
                return result;
            } else {
		if(!checkGECodonValue(dt)) {
		    throw new InitializationException("Non-existent rule, maybe GECODON not yet impelemnted");
		}
                return true;
            }
        } catch(InitializationException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
