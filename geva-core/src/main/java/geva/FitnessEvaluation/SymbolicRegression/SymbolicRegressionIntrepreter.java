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

package geva.FitnessEvaluation.SymbolicRegression;

import geva.FitnessEvaluation.FitnessFunction;
import geva.Individuals.Individual;
import geva.Individuals.FitnessPackage.BasicFitness;
import geva.Util.Constants;
import geva.Util.Random.RandomNumberGenerator;
import geva.Util.Random.Stochastic;

import java.util.Properties;

/**
 * Interpreter for symbolic regression.
 * @author erikhemberg
 */
public class SymbolicRegressionIntrepreter implements FitnessFunction, Stochastic {

    RandomNumberGenerator rng;
    double[] x; //Values of the data points for each variable x0,x1,...
    double[] calculated_target; //Values of the target at each data point
    double[][] samples; //Values for each variable at each data point
    String program[]; //Parsed phenotype string is the program
    int programCounter; //Counter for program
    private Range range; //Range of samples

    public SymbolicRegressionIntrepreter() {
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    /**
     * Calcualte the value of the target function for each data point
     * in the sample.
     * @param target target function in prefix notation
     */
    public void calculateTarget(final String target) {
        this.program = target.split(" ");//Split the target at whitspace
        this.calculated_target = new double[this.samples.length];
        for (int i = 0; i < this.samples.length; i++) {
            for (int j = 0; j < this.x.length; j++) {
                this.x[j] = this.samples[i][j];//Use the data points for each variable
            }
            this.programCounter = 0;//Initialize program counter
            this.calculated_target[i] += this.run();//Sum the values of the target
        }
    }

    public void setProperties(Properties p) {
        //Set samples
        //FIXME (If there is a random range there must be a random number generator)
        this.range = new Range(p.getProperty(Constants.SR_RANGE), this.rng);
        //Set the dimensions
        this.x = new double[this.range.getDimensions()];
        //Get the initial samples
        //FIXME(If random target must be calculated everytime)
        this.samples = this.range.getSamples();
        //Set the target
        this.calculateTarget(p.getProperty(Constants.SR_TARGET));
    }

    public RandomNumberGenerator getRNG() {
        return this.rng;
    }

    public void setRNG(RandomNumberGenerator m) {
        this.rng = m;
    }

    /**
     * Split the phenotype of the individual. Calculate the fitness as the
     * sum of the squared distance to the target. 
     * Errors in the calculation will lead to default fitness
     * @param ind Individual that will be evaluated and assigned fitness
     */
    public void getFitness(final Individual ind) {
        double fitness = 0;//Initialise fitness
        try {
            final String phenotype = ind.getPhenotype().getString();
            this.program = phenotype.split(" ");
//Get new samples for each evaluation of fitness is the input is random
            if (this.range != null && this.range.isRandom()) {
                this.samples = this.range.getSamples();
            }
            for (int i = 0; i < samples.length; i++) {
                for (int j = 0; j < x.length; j++) {
                    x[j] = samples[i][j];
                }
                this.programCounter = 0; //Reset counter
                final double runValue = this.run(); //Value from run
                //Squared error
                fitness += Math.pow(runValue - calculated_target[i], 2.0);
            }
        } catch (IllegalArgumentException e) {
            System.err.println(this.getClass().getName() + " Error geting fitness:" + e + "\n assining DEFAULT");
            fitness = BasicFitness.DEFAULT_FITNESS;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println(this.getClass().getName() + " Error geting fitness:" + e + "\n assining DEFAULT");
            fitness = BasicFitness.DEFAULT_FITNESS;
        }
        ind.getFitness().setDouble(fitness);
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
        if (s.equals("+")) {//Addition
            return (run() + run());
        } else if (s.equals("-")) {//Subtraction
                return (run() - run());
        } else if (s.equals("*")) {//Multiplication
                    return (run() * run());
        } else if (s.equals("/")) {
            /* Protected division. Returns the numerator if
            denominator <= 0.00001 */
            final double numerator = run(),  denominator = run();
            if (Math.abs(denominator) > 0.00001) {
                return (numerator / denominator);
            } else {
                return numerator;
            }
        } else if (s.matches("x\\d+")) {
            /*Get which variable is used of x0,..,xn
             * Varibale number is the digits after x
             */
            final int variableNumber = Integer.parseInt(s.substring(1));
            return x[variableNumber];
        } else if (s.equals("rnd")) {
            /*Insert a random number
             * FIXME: implement other random number generation techniques
             */
            return this.rng.nextDouble();
        } else if (s.matches("-?\\d+\\.?\\d*")) {
            //Insert a constant will be parsed as double
            return Double.parseDouble(s);
        }
        //Should not get here
        throw new IllegalArgumentException(this.getClass().getName() + " Bad execution value:" + s);
    }

    public boolean canCache() {
        return true;
    }

    public void setSamples(double[][] samples) {
        this.samples = samples;
    }

    public void setX(double[] x) {
        this.x = x;
    }
}