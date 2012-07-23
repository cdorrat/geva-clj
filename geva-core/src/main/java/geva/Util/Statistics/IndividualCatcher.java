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
 * StatCatcher.java
 *
 * Created on November 2, 2006, 1:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package geva.Util.Statistics;

import geva.Exceptions.BadParameterException;
import geva.FitnessEvaluation.MultiSquares.PictureCopy;
import geva.Individuals.Individual;
import geva.Individuals.FitnessPackage.Fitness;
import geva.Parameter.ParameterI;
import geva.Util.Constants;
import geva.Util.Random.MersenneTwisterFast;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Stores geva.Individuals
 * @author Blip
 */
public class IndividualCatcher implements ParameterI {
	private static Log logger = LogFactory.getLog(IndividualCatcher.class);    
    private StringBuffer sb;
    private int catchInterval;

    /** Creates a new instance of StatCatcher */
    public IndividualCatcher() {
        this.sb = new StringBuffer();
        this.catchInterval = 10;
    }

    /**
     * New instance
     * @param p properties
     */
    public IndividualCatcher(Properties p) {
        this.sb = new StringBuffer();
        this.setProperties(p);
    }

    public void setProperties(Properties p) {
        int valueI;
        try {
            String key = Constants.INDIVIDUAL_CATCH_INTERVAL;
            valueI = Integer.parseInt(p.getProperty(key));
            if(valueI < 1) {
                if(valueI!=-1) {
                    throw new BadParameterException(key);
                } else {
                    valueI = Integer.MAX_VALUE;
                }
            }
        } catch(Exception e) {
            valueI = Integer.MAX_VALUE;
            logger.warn("Catch interval default: best individual");
        }
        this.catchInterval = valueI;
    }

    /**
     * Add the population to a StringBuffer. geva.Individuals are taken according to catchInterval
     * @param population Population
     */
    
    public void addPop(List<Individual> population) {
        Fitness[] fA = new Fitness[population.size()];
        for(int i=0;i<fA.length;i++) {
            fA[i] = population.get(i).getFitness();
        }
        //Sort ascending
        Arrays.sort(fA);
        int cnt = 0;
        while(cnt<fA.length) {
            sb.append("Rank:").append(cnt).append(" Fit:").append(fA[cnt].getDouble());
            sb.append(" Phenotype:").append(fA[cnt].getIndividual().toString());
            sb.append(System.getProperty("line.separator"));
            cnt += this.catchInterval;
        }  
    }

    /**
     * Add a string to the string buffer
     * @param s string
     */
    public void addString(String s) {
        this.sb.append(s);
    }

    /**
     * Clear the stringbuffer
     */
    public void clear() {
        this.sb = new StringBuffer();
    }

    /**
     * Get the capacity of the stringbuffer
     * @return capacity of stringbuffer
     */
    public int getCapacity() {
        return this.sb.capacity();
    }
			      
    /**
     * geva.Individuals in the population are stored according to this interval.
     * E.g A value of 10 stores individual 1, 100, 200, ...
     * @param catchInterval Interval for catching
     */
    public void setCatchInterval(int catchInterval) {
        this.catchInterval = catchInterval;
    }

    /**
     * Get catch intervall
     * @return catch interval
     */
    public int getCatchInterval() {
        return this.catchInterval;
    }

    @Override
    public String toString() {
        return this.sb.toString();
    }
    
}
