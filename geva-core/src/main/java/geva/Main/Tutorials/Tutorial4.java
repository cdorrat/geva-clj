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
 * Tutorial4.java
 *
 * Created on April 17, 2007, 11:02 AM
 *
 */

package geva.Main.Tutorials;

import geva.Algorithm.MyFirstSearchEngine;
import geva.Algorithm.Pipeline;
import geva.Algorithm.SimplePipeline;
import geva.Main.AbstractRun;
import geva.Mapper.GEGrammar;
import geva.Util.Constants;
import geva.Util.Random.MersenneTwisterFast;

/**
 * Tutorial4 main class. 
 * This class is derived from the class State. State is conceptually the outer level of a program created using GEVA, it is a container class for
 * the algorithm and it used to setup initialise and run the algorithm. AbstractRun provides parameter reading functionality. You should try
 * to familiarise yourself with GEVA's parameterisation mechanism(Covered in a later tutorial)  as it will make your life much easier :)
 *
 * When Implementing a algorithm with GEVA it makes sense to extend AbstractRun, and implement the method setup(String[] args).  
 * @author erikhemberg
 */
public class Tutorial4 extends AbstractRun {
    
    /** Creates a new instance of Tutorial4 */
    public Tutorial4() {
        this.rng = new MersenneTwisterFast();
        super.propertiesFilePath = Constants.DEFAULT_PARAM_ROOT
                                 + "Parameters/Tutorials/Tutorial4.properties";
    }
    
    public void setup() {
    }
    
    @Override
    public void experiment(String[] args) {
        try{

            //Read the command-line arguments
            if(this.commandLineArgs(args)) {
                //Initialize timing the excecution
                long st = System.currentTimeMillis();
                
                //Create the geva.Main object
                //Setup the algorithm
                this.setup(args);
                //Initialize the algorithm
                this.init();
                //Hack for number of iterations!!?? Create a proper method
                int its = this.run();
                //Print collected data
                this.printStuff();
                //Time the excecution
                long et = System.currentTimeMillis();
                System.out.println("Done running: Total time(Ms) for " 
                            + its + " generations was:"+(et-st));

            }
        } catch(Exception e) {
            System.err.println("Exception: "+e);
            e.printStackTrace();
        }
    }
       
    /**
     * Setup the algorithm. Read the properties. Create the modules(Operators) and operations
     * @param args The command line arguments
     */
    public void setup(String[] args) {
        /* Read properties  
	 * You can configure many different aspects of GEVA
	 * through the use of a properties file. While it is possible to configure 
	 * the modules using get/set methods we encourage you to learn how to use the
	 * properties file and associated properties object to configure the system as
	 * this makes it much easier to set up and change parameters for a run. 
	 * These properties can also be specified or overridden using the command line arguments
	 * 
	 * When you pass in an argument or an argument is read from the properties file they are
	 * placed into a properties object. This object is passed to all modules' constructor methods
	 * and each module is responsible for reading its own arguments. This makes parameterising easy
	 * by reducing the need for parameter setting in the code. In fact it is possible to set up and configure
	 * different types of algorithms using the supplied modules using only a properties file.
	 */

	// Creates properties from the command line arguments
        this.readProperties(args);
        
        /*
	 * There are various steps to create an algorithm in GEVA. In this tutoral we only 
	 * intitalise a population. To get a better idea of what is going on, look at the associated properties file
	 * geva.Parameter/Tutorial4.properties
         */
        
        //Grammar
        GEGrammar grammar = new GEGrammar(this.properties);
        //State has a data member geva.Algorithm alg; geva.Algorithm is an interface and MyFirstSearchEngine is a simple but surprisingly flexible implementation.  
        MyFirstSearchEngine alg = new MyFirstSearchEngine();

        /* 
	 * INITIALISER
	 * One of he features of AbstractRun is that you can, by using reflection, specify modules as parameters.
	 * The method getInitialiser will return an initialiser as specified in the properties object.
	 */
        initialiser = getInitialiser(grammar, this.rng, this.properties);

        /*
         * Init 
	 * Here we create the initialisation pipeline. Algorithms derived from the base class AbstractAlgorithm have two pipelines,
	 * pipelineInit and pipelineRun. pipelineInit is run once at the start of an algorithm, so any initialisation modules need
	 * to go on that pipeline. 
	 * In this tutorial we are simply going to create an instance of SimplePipeline(), which as the
	 * suggests is a simple implementation of a pipeline. Pipelines are at the heart of the way GEVA builds algorithms and they will
	 * be covered in more detail in later tutorials. For now we simply need to add our initialiser module to the initialisation pipeline. 
         */
        Pipeline pipelineInit = new SimplePipeline();
        
	alg.setInitPipeline(pipelineInit);
        //Add modules to pipeline
        pipelineInit.addModule(initialiser);

        // Finally we set the algorithm
	this.algorithm = alg;
    }
    
    /**
     * Run the state
     * @param args The command line arguments     
     */
    public static void main(String[] args) {
        Tutorial4 mainTutorial4 = new Tutorial4();
        mainTutorial4.experiment(args);
	System.exit(0);
    }    
}
