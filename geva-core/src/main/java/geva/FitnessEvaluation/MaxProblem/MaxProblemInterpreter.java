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

package geva.FitnessEvaluation.MaxProblem;

import geva.FitnessEvaluation.FitnessFunction;
import geva.Individuals.Individual;
import geva.Individuals.FitnessPackage.BasicFitness;
import geva.Util.Random.RandomNumberGenerator;
import geva.Util.Random.Stochastic;

import java.util.Properties;
import java.math.BigInteger;

/**
 * This problem tries to generate the largest number possible from with a given 
 * terminal and functional set and with a depth limit. Despite seeming to be a 
 * simple problem the fitness landscape is deceptive and leads to suboptimal 
 * solutions. Even though the problem tries to generate the largest number 
 * the fitness is still minimised. It is based on as Interpreter for symbolic
 * regression by Erik Hemberg.
 * @author erikhemberg
 */
public class MaxProblemInterpreter implements FitnessFunction, Stochastic {

    RandomNumberGenerator rng;
    String program[]; //Parsed phenotype string is the program
    int programCounter; //Counter for program
    int parseTreeDepth;//
    int maxParseTreeDepth;
    int maxProblemDepth;
    double maxValue;

    public MaxProblemInterpreter() {
    }

    public void setProperties(Properties p) {
        //Set samples
        this.maxProblemDepth = Integer.parseInt(p.getProperty("maxproblem_depth"));
        this.setMaxValue();
    }

    public RandomNumberGenerator getRNG() {
        return this.rng;
    }

    public void setRNG(RandomNumberGenerator m) {
        this.rng = m;
    }

    public double getMaxValue() {
        return maxValue;
    }

    /**
     * Split the phenotype of the individual. Calculate the fitness as the
     * sum of the squared distance to the target. 
     * Errors in the calculation will lead to default fitness
     * @param ind Individual that will be evaluated and assigned fitness
     */
    public void getFitness(final Individual ind) {
        double fitness = 0;//Initialise fitness

        final String phenotype = ind.getPhenotype().getString();
        this.program = phenotype.split(" ");

        this.programCounter = 0; //Reset counter
        this.parseTreeDepth = 0; //Reset counter
        this.maxParseTreeDepth = 0; //Reset counter
        final double runValue = this.run(); //Value from run

        fitness = 1/runValue;
        if (maxParseTreeDepth > this.maxProblemDepth) {
            ind.getFitness().setDouble(BasicFitness.DEFAULT_FITNESS);
        } else {
            ind.getFitness().setDouble(fitness);
        }
    }

    /**
     * Executes the function. Currently parses: +, -, *, /, x1,...,xn, constants
     * rnd (the next random double from the random number generator)
     * @return value of execution
     * @throw IllegalArgumentException if the symbol cannot be executed
     */
    private double run() throws IllegalArgumentException {
        final String s = this.program[this.programCounter];
        this.programCounter++;
        if (this.parseTreeDepth > this.maxParseTreeDepth) {
            this.maxParseTreeDepth = this.parseTreeDepth;
        }
        if (s.equals("+")) {//Addition
            this.parseTreeDepth++;
            double value = (run() + run());
            this.parseTreeDepth--;
            return value;

        } else {
            if (s.equals("-")) {//Subtraction
                this.parseTreeDepth++;
                double value = (run() - run());
                this.parseTreeDepth--;
                return value;
            } else {
                if (s.equals("*")) {//Multiplication
                    this.parseTreeDepth++;
                    double value = (run() * run());
                    this.parseTreeDepth--;
                    return value;
                } else {
                    /* Protected division. Returns the numerator if
                    denominator <= 0.00001 */
                    if (s.equals("/")) {
                        final double numerator = run(),  denominator = run();
                        if (Math.abs(denominator) > 0.00001) {
                            return (numerator / denominator);
                        } else {
                            return numerator;
                        }
                    } else {
                        //Insert a random number
                        if (s.equals("rnd")) {
                            return this.rng.nextDouble();
                        } else {
                            //Insert a constant will be parsed as double
                            if (s.matches("-?\\d+\\.?\\d*")) {
                                return Double.parseDouble(s);
                            }
                        }
                    }
                }
            }
        }
        //Should not get here
        throw new IllegalArgumentException(this.getClass().getName() + " Bad execution value:" + s);
    }

    public boolean canCache() {
        return true;
    }

    public void setMaxProblemDepth(int maxProblemDepth) {
        this.maxProblemDepth = maxProblemDepth;
    }

    public void setMaxValue() {
        this.maxValue = Math.pow(4, Math.pow(2, this.maxProblemDepth-3));
        System.out.println(this.maxValue);
    }

}