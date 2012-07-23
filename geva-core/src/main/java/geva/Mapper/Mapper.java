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
 * geva.Mapper.java
 *
 * Created on 09 October 2006, 11:40
 *
 */

package geva.Mapper;

/**
 * Interface for mapping input (genotype) to output (phenotype).
 * @author EHemberg
 */
public interface Mapper{
    
    /**
     * Maps from a input(genotype) to an output (phenotype)
     * @return Boolean denoting a successful (True) or failed (false) mapping.
     */
    public abstract boolean genotype2Phenotype();

    /**
     * Not yet implemented!
     * @return Boolean denoting a successful (True) or failed (false) mapping.
     */
    @SuppressWarnings({"SameReturnValue"})
    public abstract boolean phenotype2Genotype();

    /**
     * Clears the mapper by dereferencing the genotype and phenotype. 
     * After this operation a mapper should be able to perform another 
     * mapping. The mapper may also need to reinitialse its internal state:
     * eg clear derivation tree clear string buffers if they are used.
     */
    public abstract void clear();

    /**
     * Returns the mappers output (phenotype)
     * @return output of the mapping
     */
    public abstract Object getPhenotype();
    /**
     * Returns the mappers input (genotype)
     * @return input of the mapper
     */
    public abstract Object getGenotype();

    /**
     * Sets the output(phenotype)
     * @param p output to set
     */
    public abstract void setPhenotype(Object p);// It's hard to avoid casting here

    /**
     * Sets the input (genotype)
     * @param g input to set
     */
    public abstract void setGenotype(Object g); // so lets pass objects about

}
