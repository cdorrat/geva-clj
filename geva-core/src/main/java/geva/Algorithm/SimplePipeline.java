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

package geva.Algorithm;


import geva.Operator.Module;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A pipeline containing modules. Extends ArrayList<Module> and implements Pipeline
 * Simple.
 */
public class SimplePipeline extends ArrayList<Module> implements Pipeline {
    
    /**
     * Constructor for a pipeline of set initial size
     * @param size Initial size
     */
    public SimplePipeline(int size) {
        super(size);
    }

    /**
     * Constructor
     */
    public SimplePipeline() {
    }
        
    /**
     * Iterate over the modules in the pipeline
     */
    public void step() {
        for(Module m:this) {
            //long st = System.currentTimeMillis();
            m.perform();
            //st = System.currentTimeMillis() - st;
            //System.out.println(m.getClass().getName()+":");
        }
        //System.out.println("");
    }
    
    /**
     * Add a module to the end of the pipeline
     * @param m Added module
     */
    public void addModule(Module m) {
        this.add(m);
    }

    /**
     * Return element specified by i
     * @param i position in pipeline
     * @return element specified by the position
     */
    public Module getModule(int i) {
        return this.get(i);
    }

    /**
     * Return the entire collection of modules
     * @return the entire collection
     */
    public Collection<Module> getModules() {
        return this;
    }
    
}
