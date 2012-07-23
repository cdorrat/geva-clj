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

import geva.Util.Constants;

/**
 * Wrapper class for loading different geva.Main.Run files
 */
public class Experiment {

    public Experiment(){}

    /**
     * Get the class name to load from the commandline and load via reflection
     * @param args commandline arguments
     * @return loaded class
     */
    private State getMain(String[] args) {
        State s = null;
        try {
	    String key = Constants.EXPERIMENT;
	    String className = null;
	    //Get the name of the file to run from the command line
            for(int i=0;i<args.length;i++) {                
                if(key.equals(args[i].substring(1))) {
                    className = args[i+1];
                }
            }
            Class clazz = Class.forName(className);
            s = (State) clazz.newInstance();
            return s;
        }  catch (Exception e) {
            System.out.println(this.getClass().getName()+".getMain():Exception loading experiment: "+e);
            System.out.println("geva.Main class not specified. -main_class flag needed.");
            return s;
        }
    }

    /**
     * Create a new Experiment instance and call the getMain function
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Experiment exp = new Experiment();
        State s = exp.getMain(args);
        s.experiment(args);
	System.exit(0);
    }
}
