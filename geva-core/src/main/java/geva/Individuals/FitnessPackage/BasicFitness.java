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
 * BasicFitness.java
 *
 * Created on 06 March 2007, 16:14
 *
 */

package geva.Individuals.FitnessPackage;

import geva.Individuals.Individual;


/**
 * BasicFitness. Class holding a basic fitness measurement. Has a fitness value
 * as well as a reference to the individual the fitness refers to.
 * @author Conor
 */
public class BasicFitness implements Fitness {
    
    public static final double DEFAULT_FITNESS = 100000000.0;
    private static double Min_Double = 0.0;
    private static double Max_Double = DEFAULT_FITNESS;
    private static int Min_Int = 0;
    private static int Max_Int = (int)DEFAULT_FITNESS;
    private double value;
    private Individual individual;
    private Object data;
    
    /** Creates a new instance of BasicFitness */
    public BasicFitness() {
        this.value = 0;
    }

    /**
     * Creates new instance of BasicFitness
     * @param f fitness value
     * @param i reference to an individual
     */
    public BasicFitness(double f, Individual i) {
        this.value = f;
        this.individual = i;
    }

    /**
     * Creates new instance of BasicFitness
     * @param i reference to an individual
     */
    public BasicFitness(Individual i) {
        this.individual = i;
    }

    /**
     * Compare the Fitness ascending
     *
     * @param o fitness to compare to
     * @return value (-1,0,1) of comparison
     */
    public int compareTo(Fitness o) {
        double dO = o.getDouble();
        double dT = this.value;
        if(Double.isNaN(dO) || Double.isInfinite(dO)) {
            dO = BasicFitness.Max_Double;
        }
        if(Double.isNaN(dT) || Double.isInfinite(dT)) {
            dT = BasicFitness.Max_Double;
        }
        if(dT < dO) {
            return -1;
        }
        if(dT > dO) {
            return 1;
        } else {
            return 0;
        }
    }

    public double getMaxDoubleFitness() {
        return BasicFitness.Max_Double;
    }

    public void setMaxDoubleFitness(double d) {
        BasicFitness.Max_Double = d;
    }

    public double getMinDoubleFitness() {
        return BasicFitness.Min_Double;
    }

    public void setMinDoubleFitness(double d) {
        BasicFitness.Min_Double = d;
    }

    public int getMaxIntFitness() {
        return BasicFitness.Max_Int;
    }

    public void setMaxIntFitness(int d) {
        BasicFitness.Max_Int = d;
    }

    public int getMinIntFitness() {
        return BasicFitness.Min_Int;
    }

    public void setMinIntFitness(int d) {
        BasicFitness.Min_Int = d;
    }
    
    public void setDefault() {
        value = BasicFitness.DEFAULT_FITNESS;
    }
    
    public double getDefaultFitness() {
        return BasicFitness.DEFAULT_FITNESS;
    }
    
    public double getDouble() {
        return this.value;
    }
    
    public double getDistance(DoubleFitness f) {
        return this.value - f.getDouble();
    }
    
    public Individual getIndividual() {
        return this.individual;
    }
    
    public void setIndividual(Individual i) {
        this.individual  = i;
    }
    
    public void setDouble(double f) {
	if(Double.isNaN(f) || Double.isInfinite(f)) {
	    f = BasicFitness.Max_Double;
	}
        this.value = f;
    }
    
    public int getInt() {
        return (int)this.value;
    }
    
    public void setInt(int f) {
        this.value = (float)f;
    }

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
    
}
