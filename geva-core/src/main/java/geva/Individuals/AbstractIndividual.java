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
 * AbstractIndividual.java
 *
 * Created on October 25, 2006, 3:38 PM
 *
 */

package geva.Individuals;

import geva.Individuals.FitnessPackage.Fitness;
import geva.Mapper.Mapper;

/**
 * An Abstract individual. An abstract individual class implements a
 * couple of the get/set patterns and adds a fitness data member.
 * @author Blip
 */
@SuppressWarnings({"CloneDoesntDeclareCloneNotSupportedException"})
public abstract class AbstractIndividual implements Individual {
    
    Fitness fitness;
    boolean evaluated;
    int age;

    /**
     * Copy constructor
     * @param copy individual to be copied
     */
    @SuppressWarnings({"UnusedDeclaration", "UnusedParameters"})
    AbstractIndividual(AbstractIndividual copy) {
    }

    AbstractIndividual() {
        
    }

    public abstract void map(int map);

    public abstract String getPhenotypeString(int map);

    public abstract Mapper getMapper();

    public abstract Genotype getGenotype();

    public abstract void setMapper(Mapper m);

    public abstract void setGenotype(Genotype g);

    public abstract void setPhenotype(Phenotype p);

    public abstract Phenotype getPhenotype();

    public abstract Individual clone();

    /**
     * Compare the indivdual
     * @param o individual to compare to
     * @return -1,0,-1
     */
    public int compareTo(Individual o) {
        return this.fitness.compareTo(o.getFitness());
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Fitness getFitness(){
        return this.fitness;
    }

    public void setFitness(Fitness f){
        this.fitness = f;
        f.setIndividual(this);
    }

    public boolean isEvaluated() {
        return this.evaluated;
    }

    public void setEvaluated(boolean b) {
        this.evaluated = b;
    }
    
    
}
