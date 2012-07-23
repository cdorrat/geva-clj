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
 * EvenFiveParityHelpFile.java
 *
 * Created on den 12 mars 2007, 15:25
 *
*/

package geva.FitnessEvaluation.ParityProblem;

/**
 * Class implementing the functions called by the Even Five Parity problem
 * @author jonatan
 */
public abstract class EvenFiveParBSF {

    /**
     * Negates the input, 1 becomes 0, 0 becomes 1
     * @param x input
     * @return negated input
     * @throws ArithmeticException if input is not 0 or 1
     */
    public int not(int x)
        throws ArithmeticException
        {
        if (x == 0)
            return 1;
        else if (x ==1)
            return 0;
        else
            throw new ArithmeticException();
        }

    /**
     * Checks if there is an even number of 1s as input. 
     * @param d0 input
     * @param d1 input
     * @param d2 input
     * @param d3 input
     * @param d4 input
     * @return return 1 if the number of 1s in the input is even else 0
     */
    int fun(int d0,int d1,int d2,int d3,int d4) {
        int d=d0+d1+d2+d3+d4;
        if ((d % 2) == 0)
            return 1; //return one if even number of ones
        else
            return 0;
    }

    /**
     * Calculates the fitness by comparing the input expression to all possible cases. Minimizing 0 is the best
     * @return the fitness value
     */
    public int getFitness() {
        int fitness=32;
        for (int d0 = 0 ; d0 < 2 ; d0++) {
            for (int d1 = 0 ; d1 < 2 ; d1++) {
                for (int d2 = 0 ; d2 < 2 ; d2++) {
                    for (int d3 = 0 ; d3 < 2 ; d3++) {
                        for (int d4 = 0 ; d4 < 2 ; d4++) {
                            if(fun(d0,d1,d2,d3,d4) == expr(d0,d1,d2,d3,d4)) 
                                fitness --; //count down fitness when correct expr
                        }
                    }
                }
            }
        }
        return fitness;
    }
    /** Creates a new instance of EvenFiveParityHelpFile */
    public EvenFiveParBSF() {
    }

    /**
     * Abstract method to override for evaluating input expression.
     * @param d0 input
     * @param d1 input
     * @param d2 input
     * @param d3 input
     * @param d4 input
     * @return value of expression
     */
    public abstract int expr(int d0,int d1,int d2,int d3,int d4);
}
