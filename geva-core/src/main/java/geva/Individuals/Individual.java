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
 * geva.Individuals.java
 *
 * Created on March 27, 2007, 1:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package geva.Individuals;

import geva.Individuals.FitnessPackage.Fitness;
import geva.Mapper.Mapper;

/**
 * This interface defines the methods for individual objects. geva.Individuals 
 *  are intended to be a container class that includes a genotype, a fitness 
 *  object, a phenotype, and a mapper. It doesnt really need to do a lot beyond
 *  implementing a map() method, which predictably enough maps from genotype to
 *  a phenotype. 
 * @author Blip
 */
@SuppressWarnings({"CloneDoesntDeclareCloneNotSupportedException"})
public interface Individual extends Cloneable, Comparable<Individual> {
    
    /**
     * Map the input(Genotype) to output(Phenotype) using a grammar as a map
     * @param map which input to map if there are multiple
     */
    @SuppressWarnings({"SameParameterValue", "UnusedParameters"})
    void map(int map);
    
    //
    // The rest of the methods are fairly obvious get/set patterns 
    //
    /**
     * Get fitness of individuals
     * @return individual fitness
     */
    Fitness getFitness();
    
    /**
     * Get the genotype
     * @return genotype
     */
    Genotype getGenotype();
    
    /**
     * Get the map use to map input and output
     * @return map used
     */
    Mapper getMapper();
    
    /**
     * Get phenotype
     * @return phenotype
     */
    Phenotype getPhenotype();
    
    /**
     * Get a String representation of the output(Phenotype)
     * @param map which output to get if there are multiple
     * @return string of output
     */
    @SuppressWarnings({"UnusedParameters"})
    String getPhenotypeString(int map);
    
    /**
     * Set fitness
     * @param f fitness
     */
    void setFitness(Fitness f);
    
    /**
     * Set genotype
     * @param g genotype
     */
    void setGenotype(Genotype g);
    
    /**
     * Set mapper
     * @param m mapper
     */
    void setMapper(Mapper m);
    
    /**
     * Set phenotype
     * @param p phenotype
     */
    void setPhenotype(Phenotype p);
    
    /**
     * Has the individual been evaluated
     * @return boolean of evaluation status
     */
    public boolean isEvaluated();

    /**
     * Indicate if the individual should be evaluated or not
     * @param b set if individual should be evaluated
     */
    public void setEvaluated(boolean b);

    /**
     * Get the validity of the individual
     * @return validity of the individual
     */
    public boolean isValid();

    /**
     * Set the validity of the individual
     * @param b validity to be set
     */
    public void setValid(boolean b);

    /**
     * Clone the individual
     * @return a clone of the individual
     */
    public Individual clone();
    
    /**
     * Age is how long the individual has existed
     * @param age How long the individual has existed
     **/
    public void setAge(int age);

    /**
     * The age of the individual, counted as how many
     * iterations it has survived.
     * @return number of iterations survived
     */
    public int getAge();
}
