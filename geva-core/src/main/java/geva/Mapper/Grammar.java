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
 * Grammar.java
 *
 * Created on 09 October 2006, 11:41
 *
 */
package geva.Mapper;

import geva.Util.Enums;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Abstract class for mapping using a Grammar
 * @author EHemberg
 */
public abstract class Grammar implements Mapper {

    // Variables
    private boolean validGrammar;
    private int startSymbol_index;
    ArrayList<Rule> rules;

    // Abstract methods
    public abstract boolean genotype2Phenotype();

    public abstract boolean phenotype2Genotype();

    public abstract String getDerivationString();
    public abstract void clear();

    // Constructors
    /** Default constructor */
    Grammar() {

        setValidGrammar(false);
        startSymbol_index = 0;
        this.rules = new ArrayList<Rule>();
    }

    /** Copy constructor.
     * @param copy grammar to copy
     */
    Grammar(Grammar copy) {
        ArrayList<Rule> aLR = new ArrayList<Rule>();
        Iterator<Rule> ruleIt = copy.rules.iterator();
        Rule r;
        while (ruleIt.hasNext()) {
            r = new Rule(ruleIt.next());
            aLR.add(r);
        }
        this.rules = aLR;

        setValidGrammar(copy.getValidGrammar());
        startSymbol_index = copy.startSymbol_index;
    }

    //Get & Set
    /** Return the validity of the current grammar.
     * @return validity of grammar
     */
    boolean getValidGrammar() {
        return validGrammar;
    }

    /** Set the validity of the grammar.
     * @param newValidGrammar validity of grammar
     */
    void setValidGrammar(boolean newValidGrammar) {
        validGrammar = newValidGrammar;
    }

    /**
     * Get the start symbol of the grammar
     * If there are no rules null is returned
     * @return start symbol
     */
    public Symbol getStartSymbol() {
        if (rules.size() > 0) {
            return rules.get(startSymbol_index).getLHS();
        }
        return null;
    }

    /** Change start symbol by index on ArrayList of rules.
     * @param index index of start symbol
     * @return if the setting of start symbol was successfull
     */
    @SuppressWarnings({"UnusedReturnValue", "SameParameterValue"})
    boolean setStartSymbol(int index) {
        if (index < rules.size() && index >= 0) {// Check boundaries.
            startSymbol_index = index;
            genotype2Phenotype();// Update phenotype.
            return true;
        }
        return false;
    }

    /** Change start symbol by symbol.
     * @param newStartSymbol new start symbol
     * @return success of replacement
     */
    public boolean setStartSymbol(Symbol newStartSymbol) {
        Rule rule;
        for (Rule rule1 : rules) {
            rule = rule1;
            if (rule.getLHS().equals(newStartSymbol)) { //Check that start symbol exists
                startSymbol_index = rules.indexOf(rule);
                genotype2Phenotype();// Update phenotype.
                return true;
            }
        }
        return false;
    }

    /** Change start symbol by string.
     * @param newStartSymbol new start symbol
     * @return success of replacement
     */
    public boolean setStartSymbol(String newStartSymbol) {
        Rule rule;
        for (Rule rule1 : rules) {
            rule = rule1;
            if (rule.getLHS().getSymbolString().equals(newStartSymbol)) {
                startSymbol_index = rules.indexOf(rule);
                genotype2Phenotype();// Update phenotype.
                return true;
            }
        }
        return false;
    }

    /** Return pointer to current start rule.
     * @return start rule
     */
    public Rule getStartRule() {
        return rules.get(startSymbol_index);
    }

    /**
     * Get the rules in the grammar
     * @return rules in the grammar
     */
    public ArrayList<Rule> getRules() {
        return rules;
    }

    /**
     * Get the number of productions in the grammar
     * @return number of productions
     */
    public int getProductionCount() {
	int n = 0;
	for (Rule r: rules) {
	    n += r.size();
	}
	return n;
    }

    /**
     * Set rules in grammar
     * @param newRules rules to set
     */
    public void setRules(ArrayList<Rule> newRules) {
        this.rules = newRules;
    }

    /**
     * Get a list of all the symbols which are terminals, ie the
     * alphabet over which this grammar's language is defined.
     * Warning: this won't include any terminals defined via
     * GECodonValue.
     * @return ArrayList of terminal Symbols.
     */
    public ArrayList<Symbol> getTerminals() {
        ArrayList<Symbol> t = new ArrayList<Symbol>();
        for (Rule rule : rules) {
            for (Production p : rule) {
                for (Symbol s : p) {
                    if (s.getType() == Enums.SymbolType.TSymbol) {
                        t.add(s);
                    }
                }
            }
        }
        return t;
    }

    /**
     * Get a list of all the terminal strings for this grammar.
     * Warning: this won't include any terminals defined via
     * GECodonValue.
     * @return list of terminal Strings.
     */
    public ArrayList<String> getTerminalStrings() {
        ArrayList<Symbol> tsym = getTerminals();
        ArrayList<String> tstr = new ArrayList<String>();
        for (Symbol sym : tsym) {
            tstr.add(sym.getSymbolString());
        }
        return tstr;
    }
}
