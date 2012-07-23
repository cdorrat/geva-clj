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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geva.FitnessEvaluation.SantaFeAntTrail;

import geva.FitnessEvaluation.FitnessFunction;
import geva.Individuals.Individual;
import geva.Individuals.Phenotype;
import geva.Individuals.FitnessPackage.BasicFitness;

import java.util.Properties;

/**
 * Too run the interpreter the -trail_type needs to be provided
 * FIXME parse the phenotype to a tree instead of using lookahead
 * @author erikhemberg
 */
public class SantaFeAntTrailInterpreter implements FitnessFunction {

    private Phenotype phenotype;
    private int programCounter;
    private Trail trail;
    String trail_type;
    static final String IF = "if(food_ahead()==1) {";
    static final String ELSE = "} else {";
    static final String END_IF = "}";
    static final String MOVE = "move();";
    static final String LEFT = "left();";
    static final String RIGHT = "right();";
    static final String TRAIL_TYPE_FLAG = "trail_type";

    public SantaFeAntTrailInterpreter() {
    }

    Trail getTrail(final String trail_type) {
        final Trail trailTmp;
        if (trail_type.equals("geva.FitnessEvaluation.SantaFeAntTrail.Trail")) {
            trailTmp = new Trail();
        } else if (trail_type.equals("geva.FitnessEvaluation.SantaFeAntTrail.SanMateoTrail")) {
            trailTmp = new SanMateoTrail();
        } else if (trail_type.equals("geva.FitnessEvaluation.SantaFeAntTrail.LosAltosTrail")) {
            trailTmp = new LosAltosTrail();
        } else {
            throw new IllegalArgumentException("Unkown trail type:" + trail_type + "\n Specify using " + SantaFeAntTrailInterpreter.TRAIL_TYPE_FLAG + " e.g the basic Santa Fe Trail: " + SantaFeAntTrailInterpreter.TRAIL_TYPE_FLAG + " geva.FitnessEvaluation.SantaFeAntTrail.Trail");
        }
        return trailTmp;
    }

    public void setTrail_type(String trail_type) {
        this.trail_type = trail_type;
    }

    /**
     * Calculate the fitness for the individual by interpreting the
     * the phenotype
     * @param individual Evaluated individual
     */
    public void getFitness(Individual individual) {
        trail = getTrail(this.trail_type); //Trail to travese
        double fitness = trail.getFood(); //Initial fitness
        try {
            phenotype = individual.getPhenotype();
            if (this.phenotype.size() > 0) {
                while (trail.get_Energy() > 0) {
                    this.programCounter = 0;
                    run();
                }
                fitness = trail.getFitness();
            } else {
                throw new IllegalArgumentException("Bad phenotype size");
            }
        } catch (IllegalArgumentException e) {
            System.err.println(this.getClass().getName() + " Error geting fitness:" + e + "\n assining DEFAULT");
            fitness = BasicFitness.DEFAULT_FITNESS;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println(this.getClass().getName() + " Error geting fitness:" + e + "\n assining DEFAULT");
            fitness = BasicFitness.DEFAULT_FITNESS;
        }
        individual.getFitness().setDouble(fitness);
    }

    public boolean canCache() {
        return true;
    }

    public void setProperties(Properties p) {
        this.trail_type = p.getProperty(SantaFeAntTrailInterpreter.TRAIL_TYPE_FLAG);
    }

    /**
     * Find the else statement
     */
    private void lookAheadElse() {
        boolean found = false;
        int depth = 0;//Keep track of nested ifs
        //While program not finished and the else bloch for depth 0 is found
        while (this.phenotype.size() > this.programCounter && !found) {
            //Current token
            final String token = this.phenotype.get(this.programCounter).getSymbolString();
//Check else statment and correct depth
            if (token.equals(SantaFeAntTrailInterpreter.ELSE) && depth == 0) {
                found = true; //Found
            } else {
                //If staement increase depth
                if (token.equals(SantaFeAntTrailInterpreter.IF)) {
                    depth++;
                } else {
                    //End of if statemnt decrease depth
                    if (token.equals(SantaFeAntTrailInterpreter.END_IF)) {
                        depth--;
                    }
                }
            }
            this.programCounter++;
        }
    }

    /**
     * Find the end if statement
     */
    private void lookAheadEnd_If() {
        boolean found = false;
        int depth = 0;//Keep track of nested ifs
        //While program not finished and the else bloch for depth 0 is found
        while (this.phenotype.size() > this.programCounter && !found) {
            //Current token
            final String token = this.phenotype.get(this.programCounter).getSymbolString();
//Check else statment and correct depth
            if (token.equals(SantaFeAntTrailInterpreter.END_IF)) {
                if (depth == 0) {
                    found = true; //Found
                } else {
                    depth--;
                }
            } else {
                //If staement increase depth
                if (token.equals(SantaFeAntTrailInterpreter.IF)) {
                    depth++;
                }
            }
            this.programCounter++;
        }
    }

    /**
     * Execute the program by calling the functions in Trail
     */
    private void run() {
        //Check if end of program
        if (this.programCounter < this.phenotype.size()) {
            //Get current token
            final String token = this.phenotype.get(this.programCounter).getSymbolString();
            //Increase program counter
            this.programCounter++;
            if (token.equals(SantaFeAntTrailInterpreter.IF)) {//IF food ahead
                if (this.trail.food_ahead() != 1) {
//Find else statement
                    lookAheadElse();
                }
            } else if (token.equals(SantaFeAntTrailInterpreter.LEFT)) {//left
                this.trail.left();
            } else if (token.equals(SantaFeAntTrailInterpreter.MOVE)) {//move
                this.trail.move();
            } else if (token.equals(SantaFeAntTrailInterpreter.RIGHT)) {//right
                this.trail.right();
            } else if (token.equals(SantaFeAntTrailInterpreter.ELSE)) {
                lookAheadEnd_If();
            } else if (!token.equals(SantaFeAntTrailInterpreter.END_IF)) {
                throw new IllegalArgumentException("Illegal Terminal symbol:" + token);
            }
            run();//countinue executing program
        }
    }
}
