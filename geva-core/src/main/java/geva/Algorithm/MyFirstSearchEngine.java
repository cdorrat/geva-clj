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

/**
 * Contains the module pipelines used to perform the algorithm.
 * A simple Implementation of a concrete algorithm. May be good for
 * testing module performance and stability. 
 */
public class MyFirstSearchEngine extends AbstractAlgorithm {
    
    /**
     * Constructor
     */
    public MyFirstSearchEngine() {
        
    }
    
    /**
     * Step the loop pipeline.
     */
    public void step() {
        this.loop.step();
    }
    
    /**
     * Step the init pipeline
     */
    public void init() {
        this.init.step();
    }
    
    /**
     * Iterate <CODE>step()</CODE>
     * @param steps iterations of step()
     */
    public void run(int steps) {
        for(int i=0; i<steps; i++) {
            this.step();
            //System.out.println("--Gen:"+i+"----------");
        }
    }
    
}