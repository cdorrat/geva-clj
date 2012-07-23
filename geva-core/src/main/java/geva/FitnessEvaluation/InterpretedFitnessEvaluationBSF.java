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
 * InterpretedFitnessEvaluationGr.java
 *
 * Created on May 29, 2007, 12:09 PM
 *
 */

package geva.FitnessEvaluation;

import geva.Individuals.Phenotype;
import geva.Individuals.FitnessPackage.BasicFitness;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;

/**
 * Interprets code using the BSF framework (http://jakarta.apache.org/bsf/)
 * @author erikhemberg
 */
public abstract class InterpretedFitnessEvaluationBSF extends InterpretedFitnessEvaluation {
    
    protected final BSFManager mngr;

    /** Creates a new instance of InterpretedFitnessEvaluationGr */
    public InterpretedFitnessEvaluationBSF() {
        mngr = new BSFManager();
    }

    /**
     * Create code by adding header and tail to the evolved input
     * @param p input
     * @return code
     */
    protected abstract String createCode(Phenotype p);

    /**
     * Create a header and a tail for the input string. Uses the groovy language
     * Inserts the input from
     * the phenotype. Adds a tail. Call eval in the BSF manager to interpret the class.
     * @param p the input to be evaluated
     * @return fitness of the input
     */
    public double runFile(Phenotype p) {
        double fit = BasicFitness.DEFAULT_FITNESS;
        final String lng = "groovy";
        final String src = "Ind";
        final int lineNo = 0;
        final int columnNo = 0;
        final String code = this.createCode(p);
        //Evaluate
        try {
            Object o = this.mngr.eval(lng,src,lineNo,columnNo,code);
            if(o instanceof Double) {
                fit = (Double)o;
            } else {
                if(o instanceof Integer) {
                    fit = (Integer)o;
                }
            }
        } catch (BSFException ex) {
            System.err.println("Exception evaluating code using bsf:"+ex);
            ex.printStackTrace();
        }
        return fit;
    }
}
