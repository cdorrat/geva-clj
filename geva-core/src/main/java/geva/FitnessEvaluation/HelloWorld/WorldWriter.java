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

package geva.FitnessEvaluation.HelloWorld;

import java.util.HashMap;

/**
 * Class to be extended by HelloWorldBSF
 * Contains methods for appending,
 * printing and calculating the fitness compared to
 * "geva"
 */
public class WorldWriter {

    @SuppressWarnings({"StringBufferField"})
    private final StringBuffer world;
    public static String s = "geva";

    /**
     * Create an instance
     */
    public WorldWriter(){
        this.world = new StringBuffer();
    }

    /**
     * Append a char to the string
     * @param c char to append
     */
    public void writeChar(char c) {
        this.world.append(c);
    }

    public void writeChar(String c) {
        this.world.append(c);
    }

    /**
     * Append a char to the string from a hashmap
     * @param c structure containg a char
     */
    public void writeChar(HashMap c) {
        this.world.append(c.get("c"));
    }

    /**
     * Print the string
     * @return the word contained in the string
     */
    private String printWorld() {
        return world.toString();
    }

    /**
     * Compare a string. Each symbol not matching increases the fitness by 1.
     * The length difference between the string and the sought string is added to the fitness
     * @return Number of missmatches
     */
    public double calculateFitness() {
        String pS = printWorld();
        double tmpFit = 0;
        int minLength;
        if(pS.length()<s.length()) {
            minLength =  pS.length();
        } else {
            minLength = s.length();
        }
        for(int i = 0; i<minLength; i++) {
            if(pS.charAt(i) != s.charAt(i)) {
                tmpFit++;
            }
        }
        tmpFit += (Math.abs(s.length()-pS.length()));
        //System.out.println(s + " vs " + pS + " = " + tmpFit);
        return tmpFit;
    }

}
