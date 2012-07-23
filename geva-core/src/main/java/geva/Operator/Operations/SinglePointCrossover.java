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
 * SinglePointCrossover.java
 *
 * Created on March 5, 2007, 1:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package geva.Operator.Operations;


import geva.Individuals.GEChromosome;
import geva.Individuals.GEIndividual;
import geva.Individuals.Genotype;
import geva.Individuals.Individual;
import geva.Individuals.Populations.SimplePopulation;
import geva.Mapper.GEGrammar;
import geva.Operator.CrossoverModule;
import geva.Util.Constants;
import geva.Util.Random.MersenneTwisterFast;
import geva.Util.Random.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Single point crossover.
 * @author Blip
 */
public class SinglePointCrossover extends CrossoverOperation {

	private static Log logger = LogFactory.getLog(SinglePointCrossover.class);    
    protected boolean fixedCrossoverPoint = true;
    protected boolean codonsUsedSensitive;

    /**
     * Creates a new instance of SinglePointCrossover
     * @param m random number generator
     * @param prob crossover probability
     */
    @SuppressWarnings({"SameParameterValue"})
    public SinglePointCrossover(RandomNumberGenerator m, double prob) {
        super(prob, m);
    }

    /**
     * New instance
     * @param m random number generator
     * @param p properties
     */
    public SinglePointCrossover(RandomNumberGenerator m, Properties p) {
        super(m, p);
        this.setProperties(p);
    }

    /**
     * Set properties
     *
     * @param p object containing properties
     */
    @Override
    public void setProperties(Properties p) {
        super.setProperties(p);
        String value;
        boolean b = false;
        String key;
        try {
            key = Constants.FIXED_POINT_CROSSOVER;
            value = p.getProperty(key);
            if (value != null) {
                if (value.equals(Constants.TRUE)) {
                    b = true;
                }
            }
        } catch(Exception e) {
            logger.warn(e+" using default: "+b);
        }
        this.fixedCrossoverPoint = b;
        //CodonUsed sensitive
        b = false;
        key = Constants.CODONS_USED_SENSITIVE;
        value = p.getProperty(key);
        if (value != null) {
            if (value.equals(Constants.TRUE)) {
                b = true;
            }
        }
        this.codonsUsedSensitive = b;

    }

    public void doOperation(Individual operands) {}
    
    /**
     * Performes crossover on the 2 first individuals in the incoming list.
     * Depending on the crossover probability.
     * @param operands geva.Individuals to crossover
     **/
    public void doOperation(List<Individual> operands) {
        GEIndividual p1, p2;
        GEChromosome chrom1, chrom2;
        int p1maxXOPoint = 0;
        int p2maxXOPoint = 0;
        if (this.rand.nextDouble() < this.probability) {
            p1 = (GEIndividual) operands.get(0);
            p2 = (GEIndividual) operands.get(1);
            chrom1 = (GEChromosome) p1.getGenotype().get(0);
            chrom2 = (GEChromosome) p2.getGenotype().get(0);
            p1maxXOPoint = this.getMaxXOPoint(p1);
            p2maxXOPoint = this.getMaxXOPoint(p2);
            if (this.rand.nextBoolean()) {
                this.makeNewChromosome(chrom1, chrom2, p1maxXOPoint, p2maxXOPoint);
            } else {
                this.makeNewChromosome(chrom2, chrom1, p2maxXOPoint, p1maxXOPoint);
            }
        }
    }

    /**
     * Get xover max point based on used codons. 
     * If used codons are 0 or not codon use sensitive
     * chromosone length is returned (all are legal).
     * @param individual
     * @return max crossover point
     */
    private int getMaxXOPoint(final GEIndividual i) {
        final int chromsomeSize = i.getGenotype().get(0).getLength();
        // Mod for wrapping
        int maxXOpoint = i.getPreviouslyUsedCodons() % chromsomeSize;
        if (maxXOpoint <= 0 || !this.codonsUsedSensitive) {
            maxXOpoint = chromsomeSize;
        }
        //System.out.println(maxXOpoint+" mL:"+chromsomeSize+" cU:"+i.getPreviouslyUsedCodons());
        return maxXOpoint;
    }

    /**
     * Get the crossover point within the shortest of the incoming chromosomes
     * @param l1 Chromsome length 1
     * @param l2 Chromsome length 2
     * @return The crossover point
     **/
    private int getXoverPoint(final int l1, final int l2) {
        final int crossoverPoint;
        if (l1 < l2) {
            crossoverPoint = rand.nextInt(l1);
        } else {
            crossoverPoint = rand.nextInt(l2);
        }
        return crossoverPoint;
    }

    /**
     * Creates the new chromsome. Either with fixed crossver point or not.
     * @param c1 Chromsome 1
     * @param c2 Chromsome 2
     * @param p1maxXOPoint Max crossover point Chromsome 1
     * @param p2maxXOPoint Max crossover point Chromsome 2
     **/
    public void makeNewChromosome(final GEChromosome c1, final GEChromosome c2,
            final int p1maxXOPoint, final int p2maxXOPoint) {
        final int point1, point2;

        if (this.fixedCrossoverPoint) {
            point1 = getXoverPoint(p1maxXOPoint, p2maxXOPoint);
            int tmp1, tmp2;
            for (int i = 0; i < point1; i++) {
                tmp1 = c1.get(i);
                tmp2 = c2.get(i);
                c1.set(i, tmp2);
                c2.set(i, tmp1);
            }
        } else {
            point1 = this.rand.nextInt(p1maxXOPoint);
            point2 = this.rand.nextInt(p2maxXOPoint);

            int[] tmp1 = c1.toArray();
            int[] tmp2 = c2.toArray();

            c1.clear();
            c2.clear();

            for (int i = 0; i < point1; i++) {
                c1.add(tmp1[i]);
            }
            for (int i = point2; i < tmp2.length; i++) {
                c1.add(tmp2[i]);
            }
            for (int i = 0; i < point2; i++) {
                c2.add(tmp2[i]);
            }
            for (int i = point1; i < tmp1.length; i++) {
                c2.add(tmp1[i]);
            }
        }
    }

    /**
     * Chech is the crossover point is fixed
     * @return true if crossover point is fixed
     */
    public boolean isFixedCrossoverPoint() {
        return fixedCrossoverPoint;
    }

    /**
     * Set crossover point to be fixed (same on both chromsomes) or not fixed
     * @param fixedCrossoverPoint crossverpoint fixation
     */
    @SuppressWarnings({"SameParameterValue"})
    public void setFixedCrossoverPoint(boolean fixedCrossoverPoint) {
        this.fixedCrossoverPoint = fixedCrossoverPoint;
    }

    public static void main(String[] args) {
        SinglePointCrossover cop = new SinglePointCrossover(new MersenneTwisterFast(), 1);
        cop.setFixedCrossoverPoint(false);
        GEChromosome c1 = new GEChromosome(10);
        GEChromosome c2 = new GEChromosome(10);

        for (int i = 0; i < 20; i++) {
            c1.add(1);

            c2.add(2);

        }

        cop.makeNewChromosome(c1, c2, c1.size(), c2.size());

        System.out.println(c1.toString());
        System.out.println(c2.toString());

        Genotype g1 = new Genotype();
        Genotype g2 = new Genotype();
        g1.add(c1);
        g2.add(c2);
        GEIndividual i1 = new GEIndividual();
        GEIndividual i2 = new GEIndividual();
        i1.setMapper(new GEGrammar());
        i1.setGenotype(g1);
        i2.setMapper(new GEGrammar());
        i2.setGenotype(g2);


        ArrayList<Individual> aI = new ArrayList<Individual>(2);
        aI.add(i1);
        aI.add(i2);

        cop.doOperation(aI);

        System.out.println();
        System.out.println("Testing operation crossover");
        System.out.println();
        c1 = (GEChromosome) i1.getGenotype().get(0);
        c2 = (GEChromosome) i2.getGenotype().get(0);

        System.out.println(c1.toString());
        System.out.println(c2.toString());


        CrossoverModule cm = new CrossoverModule(new MersenneTwisterFast(), cop);

        SimplePopulation p = new SimplePopulation();
        p.add(i1);
        p.add(i2);
        cm.setPopulation(p);
        long st = System.currentTimeMillis();
        for (int i = 1; i < 100000000; i += 20) {

            cm.perform();


        }
        long et = System.currentTimeMillis();
        System.out.println("Done running: Total time(Ms) for " + 100000000 + " generations was" + (et - st));
        System.out.println();
        System.out.println("Testing module crossover");
        System.out.println();

        c1 = (GEChromosome) i1.getGenotype().get(0);
        c2 = (GEChromosome) i2.getGenotype().get(0);

        System.out.println(c1.toString());
        System.out.println(c2.toString());



    }
}
