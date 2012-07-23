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
 * SymRegFunkBSF.java
 *
 * This file contains the mathematical expression we're trying to evolve.
 * This is named "fun" :-) and generates values on the fly. (Reading data from
 * expernal sources (real-world problems) is not yet implemented.)
 * Created on den 18 februari 2007, 19:00
 *
 */

package geva.FitnessEvaluation.SymbolicRegression;

/**
 * help class for SymbolicRegression.java
 * @author jonatan
 */
public abstract class SymRegFunkBSF {

    
    /** Creates a new instance of SymRegFunkBSF */
    public SymRegFunkBSF() {
    }

    /**
     * Expression to evaluate
     * @param X point
     * @return value
     */
    public abstract double expr(double X);

    /**
     * Calculating the fitness as the sum of errors. 
     * Deterministically picks 20 points in [-1,1) Minimizing, 0 is best
     * @return value as sum of errors
     */
     public double getFitness(){
        double dd = 0;
        for (double x = -1; x < 1; x+=0.1){
            dd = Math.abs(expr(x) - fun(x)) + dd;
        }
        return dd;
    }

    /**
     * Hardcoded quadratic target function
     * @param x value
     * @return target function value
     */
    public double fun(double x){ // The target function!!, implement in another way ?!
        return x + x*x + x*x*x + x*x*x*x;
    }

    /**
     * Method for inverting x. Returns 1 if x = 0
     * @param x value to invert
     * @return inverted value
     */
    public double inv(double x){
         if (x == 0)
             return 1.0;
         else
             return 1 / x;
    }

    /**
     * Method for x to the power of y
     * @param x base
     * @param y exponent
     * @return x^y
     */
     public double pow(double x,double y) {
         return Math.pow(x,y);
     }

    /**
     * Protected division. If d = 0. Return Double.MAX_VALUE.
     * If n = 0 return 0
     * @param n dividend
     * @param d divisor
     * @return quotient
     */
    public double div(double n, double d) {
        if(d==0.0) {
            return Double.MAX_VALUE;
        } else {
            if(n==0.0) {
                return 0.0;
            } else {
                return n / d;
            }
        }
    }

    /**
     * Trigonometric sine function
     * @param x angle in radians
     * @return sine of argument
     */
    public double sin(double x){
        return Math.sin(x);
    }

    /**
         * Trigonometric cosine function
         * @param x angle in radians
         * @return cosine of argument
         */
    public double cos(double x){
        return Math.cos(x);
    }

    /**
     * Returns Euler's number e raised to the power of a double value.
     * @param x exponent to raise e to
     * @return e^a
     */
    public double exp(double x){
        return Math.exp(x);
    }

    /**
     * Natural logarithm
     * @param x value to take the logarithm of
     * @return log(x)
     */
    public double log(double x){
        if (x == 0)
            return 1.0;
        else
            return Math.log(x);
    }

}
