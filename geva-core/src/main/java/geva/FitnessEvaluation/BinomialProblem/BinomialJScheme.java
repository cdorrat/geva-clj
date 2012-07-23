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
 * SymbolicRegression.java
 *
 * Created on den 12 februari 2007, 13:02
 *
 */

package geva.FitnessEvaluation.BinomialProblem;

import geva.FitnessEvaluation.InterpretedJScheme;
import geva.Individuals.Phenotype;
import geva.Individuals.FitnessPackage.BasicFitness;
import geva.Util.ConstantGenerator;
import geva.Util.Random.RandomNumberGenerator;
import geva.Util.Random.Stochastic;

import java.util.Properties;


/**
 * Evaluates the fitness for the BinomialProblem class. The help class BinomialFunk
 * is used to evaluate the arithmetic expressions.
 * @author michael
 */
public class BinomialJScheme extends InterpretedJScheme implements Stochastic {
    RandomNumberGenerator rng;
    ConstantGenerator cg;
    private Range sr_range;
    private String sr_target;
    private static final String SQUARE = "(define square (lambda (x) (* x x)))";
    private static final String PROTECTED_DIVISION = "(define d (lambda (t n) (if (= n 0) .0 (/ t n))))";
    private String target;
    private String tail;
    private String pre_statement;
    private String statement;
    private static final String POST_STATEMENT = ") )";
    private static final String RUN = "(run aim guess)";
    private String intervalS;
    
    /** Creates a new instance of BinomialJScheme */
    public BinomialJScheme() {
        super();
	this.sr_range = new Range("rnd(-1, 50, 1)");
        this.cg = new ConstantGenerator();
        // 1+3x+3x^2+x^3
        this.sr_target = "(+ (+ (+ 1(* 3 x0)) (* 3(* x0 x0)))(* x0(* x0 x0)))";
    }
    
    public void setProperties(Properties p) {
        
    }
    
    public RandomNumberGenerator getRNG() {
        return this.rng;
    }
    
    public void setRNG(RandomNumberGenerator m) {
        this.rng = m;
    }

    public void setHead(Range range) {
	    StringBuffer sb = new StringBuffer();
            //inputs
	    double interval = range.getStop() - range.getStart();
	    sb.append("(define inputs0 '(");
	    for(int j = 0; j <range.getStep(); j++) {
		double d = this.rng.nextDouble();
		sb.append((range.getStart() + (d*interval)));
		if(j<(range.getStep()-1)) {
		    sb.append(" ");
		}
	    }
	    sb.append("))");
	    this.intervalS = sb.toString();
	    sb = new StringBuffer();
	    //Target
	    sb.append("(define aim (lambda (");
	    sb.append("x0");
	    sb.append(")");
	    sb.append(sr_target);
	    sb.append(") )");
	    this.target = sb.toString();
    }

    private void setTail(Range range, String s) {
	    StringBuffer sb = new StringBuffer();
	    //run loop
	    double total_samples = range.getStep();
	    sb.append("(define run (lambda (aim_x guess_x) ");
	    sb.append("(do ((i 0 (+ 1 i)) ");
	    sb.append("(diff 0) ");
	    sb.append(")");
	    sb.append(" ((= i "+total_samples+")"); 
	    sb.append(" diff)");
	    sb.append("(set! diff (+ diff (square (abs (- (aim_x ");
	    //target diff call
	    sb.append("(list-ref inputs0 i)");
	    sb.append(") (guess_x ");
	    //guess diff call
	    sb.append("(list-ref inputs0 i)");
	    sb.append(")");
	    sb.append(")))))))");
	    //closing
	    sb.append(" )");

	    this.tail = sb.toString();
	    //Guess setting pre_Head
	    StringBuffer guess = new StringBuffer();
	    guess.append("(define guess (lambda (");
	    //	    System.out.println(var_guess);
	    guess.append("x0 ) ");
	    this.pre_statement = guess.toString();
    }

    public double runFile(Phenotype p) {
        double fit = BasicFitness.DEFAULT_FITNESS;
        statement = p.getString();
	setHead(sr_range);
	setTail(sr_range,statement);
	statement = pre_statement + statement + POST_STATEMENT;
        try {
	    //System.out.println(SQUARE);
	    this.js.eval(SQUARE);
	    //System.out.println(PROTECTED_DIVISION);
	    this.js.eval(PROTECTED_DIVISION);
	    this.js.eval(intervalS);
	    //System.out.println(target);
	    this.js.eval(target);
	    //System.out.println(tail);
	    this.js.eval(tail);
	    //System.out.println(statement);
	    this.js.eval(statement);
	    //System.out.println(RUN);
	    Object res = this.js.eval(RUN);
            String sfit = res.toString();
            fit = new Double(sfit);
        } catch (Exception ex) {
	                ex.printStackTrace();
            System.err.println("woops! "+statement);
        }
        return fit;
    }
    
}
