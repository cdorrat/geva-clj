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
 * An Abstract algorithm class that should be useful for most algorithms.
 * Contains two pipelines:  
 * One pipeline for initialization. 
 * One pipeline for looping.
 */
public abstract class AbstractAlgorithm implements Algorithm {

    Pipeline init;
    Pipeline loop;

    /**
     * Constructor
     */
    AbstractAlgorithm() {
    }

    public abstract void step();
    
    public abstract void init();
    
    public abstract void run(int steps);

    public void setLoopPipeline(Pipeline loop) {
	this.loop = loop;
    }
    
    public Pipeline getLoopPipeline() {
	return this.loop;
    }

    public void setInitPipeline(Pipeline init) {
	this.init = init;
    }

    public Pipeline getInitPipeline() {
	return this.init;
    }
}