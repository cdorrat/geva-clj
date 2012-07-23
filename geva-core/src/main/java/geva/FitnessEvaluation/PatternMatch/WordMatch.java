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
 * WordMatch.java
 *
 * Created on November 1, 2006, 4:59 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package geva.FitnessEvaluation.PatternMatch;

import geva.Exceptions.BadParameterException;
import geva.FitnessEvaluation.FitnessFunction;
import geva.Individuals.Individual;
import geva.Individuals.Phenotype;
import geva.Util.Constants;

import java.util.Properties;


/**
 * Matches strings
 * @author erikhemberg
 */
public class WordMatch implements FitnessFunction {
    
    private static String s;
    
    /** Creates a new instance of WordMatch */
    public WordMatch() {
	s = "geva";
    }

    /**
     * Creates a new instance of WordMatch
     * @param s String to match
     */
    public WordMatch(String s) {
        WordMatch.s = s;
    }
    public void setProperties(Properties p) {
        String key = Constants.WORDMATCH_WORD;
        String value;
	value = p.getProperty(key,"geva");
	WordMatch.s = value;
    }

    /**
     * Compare a string to the word. Each symbol not matching increases the fitness by 1.
     * Max fitness is max(length of the word, phenotype).
     * @param p Compared phenotype
     * @return Number of missmatches
     */
    public double evaluateString(Phenotype p) {
        String pS = p.getStringNoSpace();
        int minLength = Math.min(pS.length(), s.length());
        double tmpFit = Math.max(pS.length(), s.length());
        for(int i = 0; i<minLength; i++) {
            if(pS.charAt(i) == s.charAt(i)) {
                tmpFit--;
            }
        }
        return tmpFit;
    }

    public void getFitness(Individual i) {
        i.getFitness().setDouble(this.evaluateString(i.getPhenotype()));
    }

    public boolean canCache()
    {   return true;
    }

}
