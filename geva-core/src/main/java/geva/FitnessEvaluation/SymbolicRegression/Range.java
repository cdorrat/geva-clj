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

package geva.FitnessEvaluation.SymbolicRegression;

import geva.Util.Random.RandomNumberGenerator;

/**
 * Parses an input string to a range of doubles, either random or fixed
 * FIXME allow samples from each dimension to be of different sizes
 * @author erikhemberg
 */
public class Range {

    private double[] start;
    private double[] stop;
    private double[] step;
    private boolean[] random;
    RandomNumberGenerator rng;
    private int totalSamples;

    public Range(String s) {
        this.stringToRange(s);
    }

    public Range(String s, RandomNumberGenerator rng) {
        this.stringToRange(s);
        this.rng = rng;
    }

    /**
     * For a fixed range input string as x eq [start:step:stop];
     * e.g inputs 0,0.1,0.2,...,1 for x0 and x1 is x0 eq [0:0.1:1]; x1 eq [0:0.1:1]
     * For random numbers from a range or x eq rnd(start, cases ,stop);
     * e.g random inputs rom range 0 to 1 for x0 and x1 is x0 eq rnd(0,10,1); x1 eq rnd(0,10,1)
     * @param s string to parse for number ranges
     */
    public void stringToRange(String s) {
        String[] sA = s.split(";");
        this.start = new double[sA.length];
        this.stop = new double[sA.length];
        this.step = new double[sA.length];
        this.random = new boolean[sA.length];
        for (int j = 0; j < sA.length; j++) {
            String[] sA_2 = sA[j].split("\\s*eq\\s*");
            for (int i = 0; i < sA_2.length; i++) {
                if (sA_2[i].contains("[")) {
                    String sA_3[] = sA_2[i].split(":");
                    this.start[j] = Double.parseDouble(sA_3[0].substring(1));
                    this.step[j] = Double.parseDouble(sA_3[1]);
                    this.stop[j] = Double.parseDouble(sA_3[2].substring(0, (sA_3[2].length() - 1)));
                    this.random[j] = false;
                } else {
                    if (sA_2[i].contains("(")) {
                        String sA_3[] = sA_2[i].split(",");
                        this.start[j] = Double.parseDouble(sA_3[0].substring(4));
                        this.step[j] = Double.parseDouble(sA_3[1]);
                        this.stop[j] = Double.parseDouble(sA_3[2].substring(0, (sA_3[2].length() - 1)));
                        this.random[j] = true;
                    }
                }
            }
        }
        this.totalSamples = this.getTotalSamples();
    }

    /**
     * Total samples for the dimension. For fixed values the calcualtion is
     * totalSamples = (stop-start)/step
     * @param i dimension
     * @return total number of samples from the dimension
     */
    int getTotalSamples(final int i) {
        if (this.isRandom(i)) {
            return (int) this.getStep(i);
        } else {
            return ((int) ((this.getStop(i)
                    - this.getStart(i)) / this.getStep(i)));
        }
    }

    /**
     * FIXME allow samples from each dimension to be of different sizes
     * @return total number of samples from the range
     * @throws java.lang.IllegalArgumentException
     */
    int getTotalSamples() throws IllegalArgumentException{
        final int totalSamplesTmp = getTotalSamples(0);
        for (int i = 1; i < step.length; i++) {
            if (totalSamplesTmp != getTotalSamples(i)) {
                throw new IllegalArgumentException(this.getClass().getName() +
                        " Samples from each dimension must be of the same size");
            }
        }
        return totalSamplesTmp;
    }

    /**
     * Get samples from the range. If random new samples are generated.
     * @return double containg samples for each dimension
     */
    public double[][] getSamples() {
        double[][] samples = new double[totalSamples][this.getDimensions()];
        for (int i = 0; i < samples[0].length; i++) {
            //Intervall for each dimension
            final double interval = this.stop[i] - this.start[i];
            for (int j = 0; j < samples.length; j++) {
                final double value; //Value of samplex
                if (this.random[i]) {
                    value = this.rng.nextDouble() * interval;
                } else {
                    value = this.step[i] * j;
                }
                samples[j][i] = this.start[i] + value;
            }
        }
        return samples;
    }

    public boolean isRandom() {
        return random[0];
    }

    public void setRandom(boolean random) {
        this.random[0] = random;
    }

    public boolean isRandom(int i) {
        return random[i];
    }

    public void setRandom(boolean random, int i) {
        this.random[i] = random;
    }

    public double getStart(int dim) {
        return start[dim];
    }

    public double getStep(int dim) {
        return step[dim];
    }

    public double getStop(int dim) {
        return stop[dim];
    }

    public int getDimensions() {
        return this.start.length;
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < start.length; i++) {
            s += this.start[i] + "\n";
            s += this.step[i] + "\n";
            s += this.stop[i] + "\n";
        }
        s += this.random;
        return s;
    }

    double getStep() {
        return this.getStep(0);
    }

    double getStop() {
        return this.getStop(0);
    }

    double getStart() {
        return this.getStart(0);
    }
}