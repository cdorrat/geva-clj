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
 * GEChromosome.java
 *
 * Created on 09 October 2006, 12:34
 *
 */

package geva.Individuals;

import geva.Util.Structures.IntIterator;
import geva.Util.Structures.StaticIntList;

/**
 * Fixed length linear integer chromosome. This class is built on the static int list class from the 
 * the util package. A fast static list structure with dynamic size. 
 *
 */
public class GEChromosome extends StaticIntList implements Chromosome{
    
    private static int defaultLength = 100;
    private boolean validGenotype;
    private int maxCodonValue;
    private int maxCodonValueBitSize = Integer.SIZE;
    private int usedGenes;
    private int usedWraps;
    int maxChromosomeLength;
    
    public GEChromosome() {
        super(defaultLength);
    }
    
    public GEChromosome(int size){
        super(size);
    }
    
    public GEChromosome(int size, int[] data){
        super(size);
        setAll(data);
    }
    
    /**
     * Copy constructor
     * @param c copy
     */
    public GEChromosome(GEChromosome c) {
        super(c);
        this.validGenotype = c.validGenotype;
        this.maxCodonValue = c.maxCodonValue;
        this.maxChromosomeLength = c.maxChromosomeLength;
    }

    public int getCodonSizeBits() {
        return this.maxCodonValueBitSize;
    }

    public int getMaxChromosomeLength() {
        return maxChromosomeLength;
    }

    public void setMaxChromosomeLength(int maxChromosomeLength) {
        this.maxChromosomeLength = maxChromosomeLength;
        super.setMaxSize(this.maxChromosomeLength);
    }
    
    /**
     * Set how many wraps that where used
     * @param usedWraps number of wraps used
     */
    public void setUsedWraps(int usedWraps) {
           this.usedWraps = usedWraps;
    }

    /**
     * Get how many wraps were used
     * @return number of wraps used
     */
    public int getUsedWraps() {
        return usedWraps;
    }

    /**
     * Get how many genes where used when mapping
     * @return number of genes used
     */
    public int getUsedGenes() {
        return usedGenes;
    }

    /**
     * Set how many genes where used during mapping
     * @param usedGenes number of genes used
     */
    public void setUsedGenes(int usedGenes) {
        this.usedGenes = usedGenes; 
    }

    /**
     * Maximum value of a codon
     * @return codon max value
     */
    public int getMaxCodonValue() {
        return maxCodonValue;
    }

    
    public int getCodonSize() {
        final int intSize = (int)Math.ceil((double)this.maxCodonValueBitSize/Byte.SIZE);
        return intSize;
    } 
    /**
     * Set maximum codon value. Small values can bias the
     * choices of production rules when applying mod to the codon value.
     * @param maxCodonValue maximum codon value allowed
     */
    public void setMaxCodonValue(final int maxCodonValue) {
        final double mcvLog = Math.log(maxCodonValue);
        final double twoLog = Math.log(2.0);
        if(maxCodonValue==Integer.MAX_VALUE) {
            this.maxCodonValueBitSize = Integer.SIZE;
        } else {
            this.maxCodonValueBitSize = (int) Math.ceil(mcvLog / twoLog);
        }
        this.maxCodonValue = maxCodonValue;
    }
    
    
    /**
     * Returns the length of the genotype. 
     * @return length
     */
    public int getLength() {
        return this.currentSize;
    }
    
    /**
     * Returns the current valid field.
     * @return validity of the genotype
     */
    public boolean getValid(){
        return this.validGenotype;
    }
    
    /**
     * Set a new value for the valid field.
     * @param newValid value for genotype validity
     */
    public void setValid(boolean newValid){
        this.validGenotype=newValid;
    }
    
    /**
     * Double the size of the data ???? This is already a feature of the staticIntList class.
     */
    public void doubleSize() {
        if(this.getLength()<GEChromosome.maxSize) {
            int[] newA = new int[this.getLength()*2];
            System.arraycopy(this.data,0,newA,0,this.data.length);
            this.setAll(newA);
        } else {
            System.out.println("No doubling, too long");
        }
    }

    /**
     * Set the default length
     * @param length default length
     */
    public static void setDefaultLength(int length) {
        GEChromosome.defaultLength = length;
    }

    /**
     * get the default length
     * @return default length
     */
    public static int getDefaultLength() {
        return GEChromosome.defaultLength;
    }
    
    public String toString() {
        StringBuffer s = new StringBuffer();
        IntIterator i = this.iterator();
        s.append("Chromosome Contents: ");
        while(i.hasNext()) {
            s.append(i.next());
            s.append(",");
        }
        return s.toString();
    }

}
