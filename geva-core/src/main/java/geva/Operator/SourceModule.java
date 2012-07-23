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
 * SourceModule.java
 *
 * Created on 07 March 2007, 23:25
 *
 */

package geva.Operator;


import geva.Individuals.Populations.Population;
import geva.Parameter.ParameterI;
import geva.Util.Random.RandomNumberGenerator;

import java.util.Properties;

/**
 * Abstract class used for a source module.
 * Contains the size of the source.
 * @author Conor
 */
public abstract class SourceModule extends OperatorModule implements ParameterI {
    
    protected int size;
    
    /** Creates a new instance of SourceModule
     * @param rng random number generator
     * @param size size of source
     */
    public SourceModule(RandomNumberGenerator rng, int size) {
        super(rng);
        this.size = size;
    }
    
    /** Creates a new instance of SourceModule */
    public SourceModule() {
        super();
    }
    
    /** Creates a new instance of SourceModule
     * @param rng random number generator
     * @param p properies
     */
    public SourceModule(RandomNumberGenerator rng, Properties p) {
        super(rng);
        this.setProperties(p);
    }
    
    public abstract Population getPopulation();

    /**
     * Set the size
     * @param size The integer size
     **/
    public void setSize(int size) {
        this.size = size;
    }
    
}
