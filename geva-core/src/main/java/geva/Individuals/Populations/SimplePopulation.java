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
 * SimplePopulation.java
 *
 * Created on 08 March 2007, 03:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package geva.Individuals.Populations;

import geva.Individuals.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Population using an array list structure for storing the
 * individuals. The functions are wrappers for the ArrayList<Individual> structure.
 * @author Conor
 */
public class SimplePopulation implements Population {

    ArrayList<Individual> indies;
    //Used for testing some classes

    /**
     * Creates a new instance of SimplePopulation
     */
    public SimplePopulation() {
        indies = new ArrayList<Individual>();
    }

    public SimplePopulation(int size) {
        indies = new ArrayList<Individual>(size);
    }

    public void add(Individual i) {
        indies.add(i);
    }

    public void sort() {
        Collections.sort(indies);
    }

    public Iterator<Individual> iterator() {
        return indies.iterator();
    }

    public int size() {
        return indies.size();
    }

    public void addAll(Collection<Individual> immigrants) {
        for (Individual immigrant : immigrants) {
            indies.add(immigrant);
        }

    }

    public void addAll(Population immigrants) {
        Iterator<Individual> indIt = immigrants.iterator();
        while (indIt.hasNext()) {
            indies.add(indIt.next());
        }

    }

    public List<Individual> getAll() {
        return indies;
    }

    public boolean contains(AbstractIndividual individual) {
        return indies.contains(individual);
    }

    @Override
    public String toString() {
        final StringBuffer s = new StringBuffer();
        final Iterator<Individual> iIt = indies.iterator();
        DecimalFormat df = new DecimalFormat("#0.00");
        while (iIt.hasNext()) {
            final Individual ind = iIt.next();
            final String f = df.format(ind.getFitness().getDouble());
            s.append(f);
            if(iIt.hasNext()) {
                s.append(",");
            }
        }
        return s.toString();
    }

    public boolean contains(Individual individual) {
        return this.indies.contains(individual);
    }

    public Individual get(int index) {
        return this.indies.get(index);
    }

    public void clear() {
        this.indies.clear();
    }

    public void remove(Individual ind) {
        this.indies.remove(ind);
    }

    public Individual set(int i, Individual ind) {
        return this.indies.set(i, ind);
    }
}
