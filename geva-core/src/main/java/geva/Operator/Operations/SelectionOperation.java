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

package geva.Operator.Operations;


import geva.Exceptions.BadParameterException;
import geva.Individuals.Individual;
import geva.Individuals.Populations.Population;
import geva.Individuals.Populations.SimplePopulation;
import geva.Util.Constants;

import java.util.List;
import java.util.Properties;

/**
 * Abstract SelectionOperation class. Has a selectedPopulation and the size of the selection
 */
public abstract class SelectionOperation implements Operation {

    protected Population selectedPopulation;
    protected int size;

    /**
     * New instance
     * @param size size of selection
     */
    public SelectionOperation(int size) {
        this.selectedPopulation = new SimplePopulation(size);
        this.size = size;
    }

    /**
     * New instance
     * @param p properties
     */
    public SelectionOperation(Properties p) {
        this.setProperties(p);
    }

    /**
     * New instance
     */
    public SelectionOperation() {
    }

    /**
     * Set properties
     *
     * @param p object containing properties
     */
    public void setProperties(Properties p) {
        double valueD = 1.0;
        String key;
        int valueI = Integer.parseInt(Constants.DEFAULT_POPULATION_SIZE);
        key = Constants.POPULATION_SIZE;
        valueI = Integer.parseInt(p.getProperty(key, String.valueOf(valueI)));
        key = Constants.REPLACEMENT_TYPE;
        if (p.getProperty(key) != null) {
            if (p.getProperty(key).equals(Constants.STEADY_STATE)) {
                valueD = 2.0 / valueI;
            } else {
                if (p.getProperty(key).equals(Constants.GENERATIONAL)) {
                    valueD = 1.0;
                }
            }
        } else {
            key = Constants.SELECTION_SIZE;
            valueD = Double.parseDouble(p.getProperty(key, "1.0"));
            if (valueD < 0.0 || valueD > 1.0) {
                System.err.println(this.getClass().getName() + ".setProperties() bad value for parameter -" + Constants.SELECTION_SIZE + ":" + valueD + " must be between 0.0 and 1.0");
                throw new IllegalArgumentException();
            }
        }
        this.size = (int) (valueD * valueI);
        this.selectedPopulation = new SimplePopulation(this.size);
    }

    @SuppressWarnings({"EmptyMethod"})
    public abstract void doOperation(Individual operand);

    public abstract void doOperation(List<Individual> operands);

    /**
     * Returns the selected population.
     * @return Selected population
     **/
    public Population getSelectedPopulation() {
        return selectedPopulation;
    }

    /**
     * Size of the population to be selecetd
     * @return selected population size
     **/
    public int getSize() {
        return size;
    }
}