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
 * Population.java
 *
 * Created on 07 March 2007, 11:35
 *
 */

package geva.Individuals.Populations;

import geva.Individuals.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Interface for a population. The population contains the individuals
 * @author Conor
 */
public interface Population {

    /**
     * Sort the individuals in the population
     */
    @SuppressWarnings({"EmptyMethod"})
    public void sort();

    /**
     * Get an iterator over the population
     * @return iterator over the geva.Individuals
     */
    public Iterator<Individual> iterator();

    /**
     * The number of individuals in the population
     * @return number of individuals
     */
    public int size();

    /**
     * Add a collection of individuals to the population
     * @param immigrants collection of individuals
     */
    public void addAll(Collection<Individual> immigrants);

    /**
     * Add an entire population to the population
     * @param pop population to add
     */
    public void addAll(Population pop);

    /**
     * Get a list of all the individuals
     * @return list view of the population
     */
    public List<Individual> getAll();

    /**
     * Check if the individual is contained in the population
     * @param individual individual to compare
     * @return boolean value if the individual exists in the population
     */
    @SuppressWarnings({"BooleanMethodIsAlwaysInverted"})
    public boolean contains(Individual individual);

    /**
     * Add an individual to the population
     * @param i individual to add
     */
    public void add(Individual i);

    /**
     * Get an individual from the specified index
     * @param index which individual to return
     * @return individual at index
     */
    public Individual get(int index);

    /**
     * Clear the population of all individuals
     */
    public void clear();

    /**
     * Remove individual from population
     * @param ind individual to remove
     */
    public void remove(Individual ind);
}
