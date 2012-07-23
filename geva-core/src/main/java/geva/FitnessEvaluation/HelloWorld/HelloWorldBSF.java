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
 * HelloWorldBSF.java
 *
 * Created on May 29, 2007, 12:08 PM
 *
 */

package geva.FitnessEvaluation.HelloWorld;


import geva.FitnessEvaluation.InterpretedFitnessEvaluationBSF;
import geva.Individuals.Phenotype;
import geva.Util.Constants;

import java.util.Properties;

/**
 * Class for executing the HelloWorld example using BSF. Not the fastest.
 *
 * @author erikhemberg
 */
public class HelloWorldBSF extends InterpretedFitnessEvaluationBSF {
    
    /** Creates a new instance of HelloWorldBSF */
    public HelloWorldBSF() {
    }

    /**
     * Create a header and a tail for the input string. Uses the groovy language
     * Creates a class that extends the WorldWriter class. Inserts the input from
     * the phenotype. Adds a tail.
     * @param p the input
     * @return code
     */

    protected String createCode(Phenotype p) {
        StringBuffer code = new StringBuffer();
        //Head
	// Set package for code to be generated
        code.append("package geva.FitnessEvaluation.HelloWorld;\n"); 
	// Chose the class that the code should extend
        code.append("public class Test extends WorldWriter {\n");
	// Create the constructor for the new class
        code.append("\tpublic Test() {\n");
        code.append("\t\t");
        //Input
        code.append(p.getString());
        //Tail
        code.append("\n");
	code.append("\t}\n"); // End constructor
	code.append("}\n"); //End class
	// Create an object of the newly created class
	code.append("test = new Test()\n");
	// Call calculate fitness to evaluate the object
	code.append("test.calculateFitness()");
        //System.out.println(code);
	return code.toString();
    }

    /**
     * Set properties
     * @param p properties
     */
    public void setProperties(Properties p) {
        String key = Constants.WORDMATCH_WORD;
        String value;
	value = p.getProperty(key,"geva");
	WorldWriter.s = value;
    }

    
}
