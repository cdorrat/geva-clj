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

package geva.Main.Tutorials;

import geva.Algorithm.MyFirstSearchEngine;
import geva.Algorithm.Pipeline;
import geva.Algorithm.SimplePipeline;
import geva.FitnessEvaluation.FitnessFunction;
import geva.Main.AbstractRun;
import geva.Mapper.GEGrammar;
import geva.Operator.*;
import geva.Operator.Operations.*;
import geva.Util.Constants;
import geva.Util.Random.MersenneTwisterFast;
import geva.Util.Statistics.IndividualCatcher;
import geva.Util.Statistics.StatCatcher;

/**
 * Tutorial6 main class.
 * In Tutorial5 we had a look at a standard GA algorithm
 * without any fitness evaluation. In this tutorial we will show how to
 * add a simple symbolic regression fitness evaluation to the algorithm which use the Bean Scripting Framework
 *
 * In GEVA fitness evaluation is performed by, you guessed it a fitnessEvaluationOperation that is contained in
 * a fitness evaluator modules. The fitnessEvaluationOperation uses a fitnessFunction to decide the fitness.
 * This fitness function is what you will want to modify to run different types of problems.
 * (The grammar is what you want to modify to bias each problem)
 *
 * The fitness function used in this problem is a simple String matching function.
 *
 * @author erikhemberg
 */
public class Tutorial6 extends AbstractRun {
    
    private long startTime;
    /** Creates a new instance of Tutorial6 */
    public Tutorial6() {
        this.rng = new MersenneTwisterFast();
        super.propertiesFilePath = Constants.DEFAULT_PARAM_ROOT
                                 + "Parameters/Tutorials/Tutorial6.properties";
        this.startTime = System.currentTimeMillis();
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
         * There are various steps to create an algorithm in GEVA. Here we use a standard GA algorithm.
         */
        
        /*
         * GRAMMAR
         * The grammar for a grammatical evolution problem is currently provided in BNF form. The properties file
         * states where path to where the grammar file can be found. For this tutorial we use the letter_grammar.bnf file
         */
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
         * CROSSOVER
         * To implement crossover. First a crossover operation needs to be chosen. Here it is single point crossover.
         * To the operation a reference to the random number generator is passed as well as the properties. The crossover operation
         * class will identifie which of the properties it will consider for its settings, the rest will be ignored.
         *
         * After the operation is instanciated a CrossoverModule object is created, which takes a reference to the crossover operation
         * and sets it as the operation performed by the module.
         */
        CrossoverOperation singlePointCrossover = new SinglePointCrossover(this.rng, this.properties);
        CrossoverModule crossoverModule = new CrossoverModule(this.rng, singlePointCrossover);
        /*
         * MUTATION
         * The same as for crossover, except that a mutation operation and a mutation module are created.
         */
        IntFlipMutation mutation = new IntFlipMutation(this.rng, this.properties);
        MutationOperator mutationModule = new MutationOperator(this.rng, mutation);
        /*
         * SELECTION
         * A selection operation is created by calling getSelectionOperation, to, via reflection, create a SelectionOperation.
         * The SelectionOperation class to instanciate is specified in the properties file.
         * As with crossover this is passed to the SelectionScheme module.
         */
        SelectionOperation selectionOperation = getSelectionOperation(this.properties, this.rng);
        SelectionScheme selectionScheme = new SelectionScheme(this.rng, selectionOperation);
        /*
         * REPLACEMENT
         * Again similar procedure to crossover when creating the replacement operation.
         * For the module creation reflection is used. This is because there are different ways of joining the new and old populations.
         * Another important thing to notice for the replacement is that it needs to know which population it will join.
         * Here it takes the reference from selectionScheme.getPopulation(), in other words the selected population.
         */
        ReplacementOperation replacementOperation = new ReplacementOperation(this.properties);
        JoinOperator replacementStrategy = this.getJoinOperator(this.properties, this.rng, selectionScheme.getPopulation(), replacementOperation);
        /*
         * FITNESS FUNCTION
         * The fitness function is determined dynamically by getFitnessFunction. Where the fitness function is specified in the properties file.
         * A fitness function implements the interface FitnessFunction, this interface requires it to implement getFitness(Individual i).
         * It is there that the fitness evaluation should be written. In this example the fitness evaluation consists of matching the string
         * that an individual maps to a predifined string. This tutorial demonstrates a simple case which does not involve compiling.
         *
         * The FitnessEvaluationOperation takes the fitness function as an argument and calls the getFitness method in the FitnessFunction.
         * This operation checks if the individual is already evaluated, mapped and valid. In this tutorial only unevaluated individuals with
         * valid mappings will be sent to the fitness function. In GE the individual can be invalid if the there are non-terminals
         * still present in the developing solution after we have read all the codons on the genome.
         *
         * A FitnessEvaluator module contains the operation and is what is attached to the pipeline.
         */
        FitnessFunction fitnessFunction = getFitnessFunction(this.properties);
        FitnessEvaluationOperation fitnessEvaluationOperation = new FitnessEvaluationOperation(fitnessFunction);
        FitnessEvaluator fitnessEvaluator = new FitnessEvaluator(this.rng, fitnessEvaluationOperation);
        

        //Statistics
        StatCatcher stats = new StatCatcher(Integer.parseInt(this.properties.getProperty("generations")));
        IndividualCatcher indCatch = new IndividualCatcher(this.properties);
        stats.addTime(startTime);//Set initialisation time for the statCatcher (Not completly accurate here)
        StatisticsCollectionOperation statsCollection = new StatisticsCollectionOperation(stats, indCatch, this.properties);
        Collector collector = new Collector(statsCollection);

        /*
         * INIT
         * Here we create the initialisation pipeline. Algorithms derived from the base class AbstractAlgorithm have two pipelines,
         * pipelineInit and pipelineRun. pipelineInit is run once at the start of an algorithm, so any initialisation modules need
         * to go on that pipeline.
         */
        Pipeline pipelineInit = new SimplePipeline();
        
        alg.setInitPipeline(pipelineInit);
        /*
         * FitnessEvaluator for the init pipeline. If fitness evaluation is poerformed before the loop pipeline is called
         * a FitnessEvaluator module needs to be attached to the init-pipeline. This module uses the same FitnessEvaluationOperation and
         * FitnessFunction object as the loop-pipeline
         */
        FitnessEvaluator fitnessEvaluatorInit = new FitnessEvaluator(this.rng, fitnessEvaluationOperation);
        fitnessEvaluatorInit.setPopulation(initialiser.getPopulation());
	collector.setPopulation(initialiser.getPopulation());
                //Add modules to pipeline
        pipelineInit.addModule(initialiser);
        pipelineInit.addModule(fitnessEvaluatorInit);
        pipelineInit.addModule(collector); 
        /*
         * LOOP
         * Here we create the loop pipeline, this is what will be run after the initialisation. It is to this pipeline that all
         * The modules should be attached. It is important to consider the execution order of the modules, and which
         * population they will be working on.
         */
        //Pipeline
        Pipeline pipelineLoop = new SimplePipeline();
        alg.setLoopPipeline(pipelineLoop);
        /*
         * POPULATIONS
         * Here the populations which the different modules will work on is set. Remember that we set the population which would be join
         * when we constructed the module.
         * Here the modules work on either the initialised population or the selected population. Finally both populations
         * are in the replacementStrategy module. The selected population will be passed through the FitnessEvaluator.
         */
        selectionScheme.setPopulation(initialiser.getPopulation()); // The selectionScheme takes one population and splits it
        replacementStrategy.setPopulation(initialiser.getPopulation());// The replacement takes to populations and joins them (The population that will be joined is here specified in the constructor.
        crossoverModule.setPopulation(selectionScheme.getPopulation()); // Crossover will be performed on the selected population
        mutationModule.setPopulation(selectionScheme.getPopulation()); // Mutation will be performed on the selected population
        fitnessEvaluator.setPopulation(selectionScheme.getPopulation()); // Fitness evaluation will be performed on the selected population
	collector.setPopulation(initialiser.getPopulation());
        /*
         * PIPELINE
         * Here the modules are added in the desired order to the loop pipeline.
         * First selection will be performed. After that crossover will be performed, remember that
         * the crossover module will operate on the selected population. This was set above, when the setPopulation was called.
         * Then the selected population will be mutated. After the alterations of the individuals in the selected population.
         * It will be evaluated in the FitnessEvaluator module. Finally the replacementStartegy modul will replace the old population
         * with the selected population.
         */
        pipelineLoop.addModule(selectionScheme); //Select the population according to the in the properties file specified criteria, here tournament selection
        pipelineLoop.addModule(crossoverModule); //Perform crossover on the setPopulation, here the selected population will be subjected to single point crossover
        pipelineLoop.addModule(mutationModule); //Mutate the setPopulation. Here int-flip mutation is performed on the selected population
        pipelineLoop.addModule(fitnessEvaluator); //The population will be assigned new fitness
        pipelineLoop.addModule(replacementStrategy); //Replace the old population with the selected population.
        pipelineLoop.addModule(collector);

        // Finally we set the algorithm
        this.algorithm = alg;
    }
    
    /**
     * Run the state
     * @param args The command line arguments
     */
    public static void main(String[] args) { 
        Tutorial6 mainTutorial6 = new Tutorial6();
        mainTutorial6.experiment(args);
	System.exit(0);
    }
    
}
