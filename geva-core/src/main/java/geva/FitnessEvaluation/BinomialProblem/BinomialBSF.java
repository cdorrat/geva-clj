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
 * BinomialBSF.java
 *
 * Created on den 12 februari 2007, 13:02
 *
 */

package geva.FitnessEvaluation.BinomialProblem;


import geva.FitnessEvaluation.InterpretedFitnessEvaluationBSF;
import geva.Individuals.Phenotype;

import java.util.Properties;

/**
 * Evaluates the fitness for the BinomialProblemExperiment class. The help class BinomialFunk
 * is used to evaluate the arithmetic expressions.
 * @author jonathan
 */
public class BinomialBSF extends InterpretedFitnessEvaluationBSF {
    
    /** Creates a new instance of Binomial problem */
    public BinomialBSF() {
    }
    
    public void setProperties(Properties p) {
    }
    
    public String createCode(Phenotype p) {
        StringBuffer code = new StringBuffer();
        //Header
        code.append("package geva.FitnessEvaluation.BinomialProblem;\n");
        code.append("public class Test extends BinomialFunkBSF {\n");
        code.append("\tpublic Test() {}\n");
        code.append("\tpublic double expr(double X) {\n\t\treturn ");
        //Input
        code.append(p.getString());
        //Tail
        code.append("\n\t}\n}\n");
	code.append("test = new Test()\ntest.getFitness()");
        return code.toString();
    }
    
}
