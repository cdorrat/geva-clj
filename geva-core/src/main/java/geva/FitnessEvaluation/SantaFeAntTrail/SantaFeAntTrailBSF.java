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
 * SantaFeAntTrailGr.java
 *
 * Created on May 29, 2007, 3:51 PM
 *
 */
package geva.FitnessEvaluation.SantaFeAntTrail;

import geva.FitnessEvaluation.InterpretedFitnessEvaluationBSF;
import geva.Individuals.Phenotype;
import geva.Mapper.Symbol;

import java.util.Properties;

/**
 * Class for running Santa Fe ant trail interpreted via BSF
 * @author erikhemberg
 */
public class SantaFeAntTrailBSF extends InterpretedFitnessEvaluationBSF {

    boolean setLineBreaks;
    private String trailType;

    /** Creates a new instance of SantaFeAntTrailBSF */
    public SantaFeAntTrailBSF() {
    }

    /**
     * Create a header and a tail for the input string. Uses the groovy language
     * Creates a class that extends the Trail class. Inserts the input from
     * the phenotype. Adds a tail.
     * @param p the input to be evaluated
     * @return code
     */
    public String createCode(Phenotype p) {
        StringBuffer code = new StringBuffer();
        String phenotypeString;
        if (setLineBreaks) {
            phenotypeString = setLineBreaks(p);
        } else {
            phenotypeString = p.getString();
        }
        //Head
        code.append("package geva.FitnessEvaluation.SantaFeAntTrail;\n");
        code.append("public class Test extends Trail {\n");
        code.append("\tpublic Test() {\n");
        code.append("\t\twhile(get_Energy() > 0) {\n");
        //Input
        code.append(phenotypeString);
        //Tail
        code.append("\n}\n\t}\n}\ntest = new Test();\ntest.getFitness();");
        //Evaluate
        return code.toString();
    }

    private String setLineBreaks(Phenotype p) {
        StringBuffer sb = new StringBuffer();
        for (Symbol sym : p) {
            String str = sym.getSymbolString();
            if (str.startsWith("}")) {
                sb.append("\n");
            }
            sb.append(str);
            if (str.endsWith("{") || str.startsWith("}")) {
                sb.append("\n");
            }
        }
        //	System.out.println(this.getClass().getName()+".setLineBreaks(.) phen:\n"+p+"\n sb:\n"+sb);
        return sb.toString();
    }

    public void setProperties(Properties p) {
    }

}
