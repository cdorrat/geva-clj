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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package geva.Individuals.FitnessPackage;

import geva.Individuals.Individual;
import geva.Individuals.FitnessPackage.DoubleFitness;
import geva.Individuals.FitnessPackage.Fitness;

/**
 *
 * @author erikhemberg
 */
public class DoubleFitnessMock implements DoubleFitness{

    double value;

    public DoubleFitnessMock(double d) {
        this.setDouble(d);
    }

    public int compareTo(Fitness o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double getDistance(DoubleFitness f) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double getDouble() {
        return this.value;
    }

    public Individual getIndividual() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getInt() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double getMaxDoubleFitness() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getMaxIntFitness() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double getMinDoubleFitness() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getMinIntFitness() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setDefault() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setDouble(double f) {
        this.value = f;
    }

    public void setIndividual(Individual i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setInt(int f) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setMaxDoubleFitness(double d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setMaxIntFitness(int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setMinDoubleFitness(double d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setMinIntFitness(int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
