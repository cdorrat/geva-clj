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
 * EvenFiveParityFitness.java
 *
 * Created on den 12 mars 2007, 15:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package geva.FitnessEvaluation.ParityProblem;


import geva.FitnessEvaluation.InterpretedFitnessEvaluationBSF;
import geva.Individuals.Phenotype;

import java.util.Properties;

/**
 * Evaluates the fitness for the EvenFiveParityExperiment.java.
 * Uses the help class EvenFiveParity.java
 * Interprets the code using BSF
 * @author jonatan
 */
public class EvenFiveParityFitnessBSF extends InterpretedFitnessEvaluationBSF {
    
    /** Creates a new instance of EvenFiveParityFitness */
    public EvenFiveParityFitnessBSF() {
        
    }

    /**
     * Set properties
     * @param p properties
     */
    public void setProperties(Properties p) {
    }

    /**
     * Create a header and a tail for the input string. Uses the groovy language
     * Creates a class that extends the EvenFiveParBSF class. Inserts the input from
     * the phenotype. Adds a tail.
     * @param p the input to be evaluated
     * @return fitness of the input
     */
    public String createCode(Phenotype p) {
        StringBuffer code = new StringBuffer();
        //Head
        code.append("package geva.FitnessEvaluation.ParityProblem;\n");
        code.append("public class Test extends EvenFiveParBSF {\n");
        code.append("\tpublic Test() {}\n");
        code.append("\tpublic int expr(int d0, int d1, int d2, int d3, int d4) {\n\t\treturn ");
        //Input
        code.append(p.getString());
        //Tail
        code.append("\n\t}\n}\ntest = new Test();\ntest.getFitness();");
        //Evaluate
        return code.toString();
    }
}
