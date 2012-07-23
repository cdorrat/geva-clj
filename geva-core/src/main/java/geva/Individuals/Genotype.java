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
 * Genotype.java
 *
 * Created on March 5, 2007, 7:59 PM
 */

package geva.Individuals;

import java.util.ArrayList;

/**
 * Genotype is an ArryaList<Chromosome>.
 * This is a container for one or more chromosomes.
 * For most usage a single chromosome will be sufficient so the behavior of the 
 * Class will be to provide a wrapper for the chromosome class.
 *
 * @author Blip
 */
public class Genotype extends ArrayList<Chromosome>{
    
    /** Creates a new instance of Genotype */
    public Genotype() {
        super();
        
    }

    @SuppressWarnings({"SameParameterValue"})
    public Genotype(int i) {
        super(i);
    }

    /**
     * Copy constructor
     * @param g genotyp to copy
     */
    public Genotype(Genotype g) {
        super(g);
    }

    @SuppressWarnings({"SameParameterValue"})
    public Genotype(int i, Chromosome chrom) {
        super(i);
        this.add(chrom);
    }
    
}
