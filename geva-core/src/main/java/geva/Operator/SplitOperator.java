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

package geva.Operator;


import geva.Individuals.Populations.Population;
import geva.Operator.Operations.Operation;
import geva.Util.Random.RandomNumberGenerator;

import java.util.Properties;

/**
 * Abstract class for splitting a population.
 * Splits the popluation to destinationPopulation
 * used for eg. selectionScheme
 */
public abstract class SplitOperator extends SourceModule {

    protected Population destinationPopulation;
    protected Operation operation;

    /**
     * New instance
     * @param rng random number generator
     * @param size size
     * @param op operation
     */
    public SplitOperator(RandomNumberGenerator rng, int size, Operation op){
        super(rng, size);
        this.operation = op;
    }

    /**
     * New instance
     * @param rng random number generator
     * @param op operation
     * @param p properties
     */
    public SplitOperator(RandomNumberGenerator rng, Operation op, Properties p){
        super(rng, p);
        this.operation = op;
    }

    public abstract void perform();
    public abstract void setOperation(Operation op);
    public abstract Population getPopulation();
    public abstract Operation getOperation(); 

}