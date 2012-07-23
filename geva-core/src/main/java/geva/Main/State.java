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

package geva.Main;


import geva.Algorithm.Algorithm;
import geva.Util.Random.RandomNumberGenerator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * State allows loading, saving, and setup of the algorithm's state.
 **/
public abstract class State implements Serializable {
	private static Log logger = LogFactory.getLog(State.class);
    protected Algorithm algorithm;
    protected RandomNumberGenerator rng;
    
    public State() {
    }

    public abstract void setup(String[] args);
    
    public abstract void experiment(String[] args);

    public Algorithm getAlgorithm() {
        return algorithm;
    }
    
    /**
     * Save the State
     */
    @SuppressWarnings({"IOResourceOpenedButNotSafelyClosed"})
    public void save() {
        FileOutputStream fOut ;
        ObjectOutputStream oOut;
        try{
            fOut= new FileOutputStream("saveFile");
            oOut = new ObjectOutputStream(fOut);
            oOut.writeObject(this);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    /**
     * Load a State
     * @param fileName Name of file to load
     **/
    @SuppressWarnings({"IOResourceOpenedButNotSafelyClosed"})
    public void load(String fileName) {
        FileInputStream fIn;
        ObjectInputStream oIn;
        try{
            fIn= new FileInputStream(fileName);
            oIn = new ObjectInputStream(fIn);
            
            State emp = (State) oIn.readObject();
            logger.info(emp);
        }catch(IOException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    
}