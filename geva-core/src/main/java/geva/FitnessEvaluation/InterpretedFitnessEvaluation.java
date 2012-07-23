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
 * InterpretedFitnessEvaluation.java
 *
 * Created on May 29, 2007, 12:10 PM
 *
 */

package geva.FitnessEvaluation;

import geva.Individuals.Individual;
import geva.Individuals.Phenotype;
import geva.Individuals.FitnessPackage.BasicFitness;

/**
 * Abstract class for interpreting generated code, instead of compiling
 * @author erikhemberg
 */
public abstract class InterpretedFitnessEvaluation implements FitnessFunction{
    
    /** Creates a new instance of InterpretedFitnessEvaluation */
    public InterpretedFitnessEvaluation() {
    }

    /**
     * Run the phenotype and return the fitness
     * @param p input
     * @return fitness of input
     */
    public abstract double runFile(Phenotype p);

    /**
     * Evaluate an individual and set the fitness
     * @param i input to evaluate
     */
    public void getFitness(Individual i) {
        if(i.isValid()) {
            i.getFitness().setDouble(runFile(i.getPhenotype()));
        } else {
            i.getFitness().setDouble(((BasicFitness)i.getFitness()).getDefaultFitness());
        }
    }
    
    public boolean canCache()
    {   return true;
    }

}
