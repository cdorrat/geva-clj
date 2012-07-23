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
             * FitnessEvaluator.java
             *
             * Created on October 24, 2006, 6:01 PM
             *
             * To change this template, choose Tools | Template Manager
             * and open the template in the editor.
             */

package geva.FitnessEvaluation;

import geva.Individuals.Individual;
import geva.Parameter.ParameterI;

/**
 * A simple interface to be implemented by any fitness evaluator
 * makes making fitness evaluation classes pluggable. If you want to be one
 * all you have to do is implement the single method defined here.
 * @author Blip
 * 
 * 
 */
public interface FitnessFunction extends ParameterI {
    
    /**
     * Creates a new instance of FitnessEvaluator
     * @param i Evaluated individual
     */
    public void getFitness(Individual i);

    /**
     * Return true if it is ok to cache the results of the fitness function
     */
    public boolean canCache();

}
