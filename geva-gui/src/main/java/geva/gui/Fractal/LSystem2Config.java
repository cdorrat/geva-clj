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

package geva.gui.Fractal;

import java.util.ArrayList;

/**
 * Track information about each LSystem displayed to the user in guiComp
 * @author eliott bartley
 */
public class LSystem2Config
{

    public static class Indexes extends ArrayList<Integer> { }

    /**
     * Because all LSystems are coalesced before display, when the user selects
     *  one, it must relate back to all the orignal LSystems that were
     *  coalsesced. This keeps a list of all the LSystems (with their original
     *  index) that are the same as this one
     */
    public Indexes indexes = new Indexes();
    /**
     * The phenotype of this LSystem's grammar
     */
    public String grammar;
    /**
     * The phenotype of this LSystem's depth
     */
    public int depth;
    /**
     * The phenotype of this LSystem's angle
     */
    public float angle;
    /**
     * The fitness of this LSystem, multiplied by .99 each time it is selected
     *  in LSystemSelect
     */
    public double fitness;
    /**
     * Set to true if this LSystem is picked as a parent in guiComp
     */
    public boolean select;
    /**
     * Set to true if this LSystem has been selected to die
     */
    public boolean purge;

    public LSystem2Config
    (   int      index,
        String grammar,
        int      depth,
        float    angle,
        double fitness
    ){  this.indexes.add(index);
        this.grammar = grammar;
        this.depth   = depth;
        this.angle   = angle;
        this.fitness = fitness;
        this.select  = false;
        this.purge   = false;
    }

    public interface SelectAction
    {   public void selectPerformed();
    }

}
