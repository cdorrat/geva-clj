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

import java.util.Collection;

/**
 * Interface for a pipline. Pipelines store an ordered list of modules
 * which are to executed in sequence in which they are entered. Step runs
 * the pipeline once, that is it executes all modules in the order that
 * they occur in the pipeline. 
 */
public interface Pipeline {

/**
 * Step through all the modules in the pipeline once
 */
    public void step();

    /**
     * Adds a module to the pipeline
     * @param m Module to add
     */
    public void addModule(Module m);

    /**
     * Gets module number i from the pipeline
     * @param i Number of module to get from the pipeline
     * @return A module from the pipeline
     */
    public Module getModule(int i);

    /**
     * Gets a Collection<Module> from the pipeline
     * @return the collection of modules in the pipeline
     */
    public Collection<Module> getModules();
}