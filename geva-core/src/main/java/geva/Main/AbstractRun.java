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
 * AbstractRun.java
 *
 * Created as ParameterizedState on June 12, 2007, 2:58 PM
 * Renamed to AbstractRun Nov 27 2008.
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package geva.Main;


import geva.Algorithm.AbstractAlgorithm;
import geva.Exceptions.BadParameterException;
import geva.FitnessEvaluation.FitnessFunction;
import geva.Individuals.Populations.Population;
import geva.Mapper.GEGrammar;
import geva.Operator.*;
import geva.Operator.Operations.*;
import geva.Util.Constants;
import geva.Util.Random.RandomNumberGenerator;
import geva.Util.Random.Stochastic;
import geva.Util.Statistics.IndividualCatcher;
import geva.Util.Statistics.StatCatcher;

import java.io.*;
import java.util.*;
import java.text.MessageFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * AbstractRun class. Read the parameters. Some classes are dynamically loaded.
 * @author erik
 */
public abstract class AbstractRun extends State {

	private Log logger = LogFactory.getLog(AbstractRun.class);
    /**
     * Creates a new instance of AbstractRun
     */
    public AbstractRun() {
    }
    protected String propertiesFilePath;
    protected Initialiser initialiser;
    protected Properties properties;
    protected String stdOut;
    protected String stdErr;
    protected Collector collector;

    /**
     * Setup the algorithm. Read the properties. Create the modules(Operators) and operations
     * @param args arguments
     */
    public abstract void setup(String[] args);

    /**
     * Load the fitness class according to the parameters
     * @param p Properties
     * @return FitnessFunction
     */
    protected FitnessFunction getFitnessFunction(Properties p) {
        FitnessFunction fitnessFunction = null;
        String className;
        String key = Constants.FITNESS_FUNCTION;
        try {
            className = p.getProperty(key);
            if (className == null) {
                throw new BadParameterException(key);
            }
            Class clazz = Class.forName(className);
            fitnessFunction = (FitnessFunction) clazz.newInstance();
            fitnessFunction.setProperties(p);
	    Class[] interfaces = clazz.getInterfaces();
	    for(int i = 0; i<interfaces.length; i++) {
		if(interfaces[i].getName().equals(Stochastic.class.getName())) {
		    ((Stochastic)fitnessFunction).setRNG(this.rng);
		}
	    }
        }  catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return fitnessFunction;
    }

    /**
     * Load a GEGrammar. Default is geva.Mapper.GEGrammar
     * @param p properties file
     * @return GEGrammar, null if getProperty failed
     */
    protected GEGrammar getGEGrammar(Properties p) {
        GEGrammar geg = null;
        String className;
        String key;
        key = Constants.GEGRAMMAR;
        try {
            className = p.getProperty(key, Constants.DEFAULT_GEGRAMMAR);
            Class clazz = Class.forName(className);
	    geg = (GEGrammar)clazz.newInstance();
	    geg.setProperties(p);
        } catch(IllegalAccessException e) {
            System.err.println(this.getClass().getName()+".getGEGrammar() exception: "+e);
        }  catch (ClassNotFoundException e) {
            System.err.println(this.getClass().getName()+".getGEGrammar() exception: "+e);
	    e.printStackTrace();    
        }  catch (InstantiationException e) {
            System.err.println(this.getClass().getName()+".getGEGrammar() exception: "+e);
	    e.printStackTrace();
        }
        return geg;
    }

    /**
     * Load and initialise the initialiser class according to the parameters
     * Defualt initialiser is the RandomInitialiser.
     * To add other initialisers expand the if-statement with another clause
     * @param g GEGrammar
     * @param rng RandomNumberGenerator
     * @param p Properties
     * @return Intialiser
     */
    protected Initialiser getInitialiser(GEGrammar g, RandomNumberGenerator rng, Properties p) {
        String className;
        String key = Constants.INITIALISER;
        try {
            className = p.getProperty(key);
            if (className == null) {
                throw new BadParameterException(key);
            }
            Class clazz = Class.forName(className);
            initialiser = (Initialiser) clazz.newInstance();
            // For RampedFullGrowInitialiser
            if (clazz.getName().equals(RampedFullGrowInitialiser.class.getName())) {
                CreationOperation fullInitialiser = new FullInitialiser(rng, g, p);
                CreationOperation growInitialiser = new GrowInitialiser(rng, g, p);
                ArrayList<CreationOperation> opL = new ArrayList<CreationOperation>();
                opL.add(fullInitialiser);
                opL.add(growInitialiser);
                ((RampedFullGrowInitialiser) initialiser).setOperations(opL);
            } else {
                // The default initialiser
                CreationOperation randomInitialiser;
                randomInitialiser = new RandomInitialiser(rng, g, p);
                initialiser.setOperation(randomInitialiser);

            }
            initialiser.setProperties(p);
            initialiser.setRNG(rng);
            initialiser.init();
        }  catch (Exception e) {
      	  logger.error("Exception: ", e);
        }
        return initialiser;
    }

    /**
     * Load the fitness class according to the parameters
     * @param rng RandomNumberGenerator
     * @param p Properties
     * @return SelectionOperation
     */
    protected SelectionOperation getSelectionOperation(Properties p, RandomNumberGenerator rng) {
        SelectionOperation selectionOperation = null;
        String className;
        String key;
        key = Constants.SELECTION_OPERATION;
        try {
            className = p.getProperty(key);
            if (className == null) {
                throw new BadParameterException(key);
            }
            Class clazz = Class.forName(className);
            selectionOperation = (SelectionOperation) clazz.newInstance();
            selectionOperation.setProperties(p);
            if (selectionOperation instanceof Stochastic) {
                ((Stochastic) selectionOperation).setRNG(rng);
            }
        } catch (Exception e) {
      	  logger.error("Exception: ", e);
        }
        return selectionOperation;
    }

    /**
     * Load the fitness class according to the parameters
     * @param p Properties
     * @param rng RandomNumberGenerator
     * @param incPop incomming population
     * @param rO replacement operation
     * @return SelectionOperation
     */
    protected JoinOperator getJoinOperator(Properties p, RandomNumberGenerator rng, Population incPop, ReplacementOperation rO) {
        JoinOperator joinOperator = null;
        String className = "";
        String key;
        key = Constants.REPLACEMENT_TYPE;
        if (p.getProperty(key) != null) {
            if (p.getProperty(key).equals(Constants.STEADY_STATE)) {
                className = "geva.Operator.MeritReplacementStrategy";
            } else {
                if (p.getProperty(key).equals(Constants.GENERATIONAL)) {
                    className = "geva.Operator.SimpleReplacementStrategy";
                }
            }
        }
        key = Constants.REPLACEMENT_OPERATOR;
        try {
            if (p.getProperty(key) != null) {
                className = p.getProperty(key);
            }
            Class clazz = Class.forName(className);
            joinOperator = (JoinOperator) clazz.newInstance();
            joinOperator.setIncomingPopulation(incPop);
            joinOperator.setOperation(rO);
            joinOperator.setRNG(rng);
        }  catch (Exception e) {
      	  logger.error("Exception: ", e);
        }
        return joinOperator;
    }

    /**
     * Runs the init pipeline.
     * Prints statistics from the init pipeline
     **/
    protected void init() {
        long st = System.currentTimeMillis();
        //StatCatcher stats = getStatisticsCollectionOperation().getStats();
        // Call init() in the algorithm
        // Header for the column output
        this.algorithm.init();
        long et = System.currentTimeMillis();
        this.collector = this.getCollector();
        if (this.collector != null) {
            ((StatisticsCollectionOperation) this.collector.getOperation()).printHeader();
            ((StatisticsCollectionOperation) this.collector.getOperation()).printStatistics(et - st);

        }

    }

    /**
     * Prints the collected statistics
     **/
    @SuppressWarnings({"ConstantConditions"})
    protected void printStuff() {
        boolean printToFile = false;
        try {
            if (this.properties.getProperty(Constants.OUTPUT) != null) {
                if (!this.properties.getProperty(Constants.OUTPUT).equals("") && !this.properties.getProperty(Constants.OUTPUT).equals("false")) {
                    printToFile = true;
                }
            }
        } catch (Exception e) {
            logger.warn("No output option specified! Not printing to file");
        }
        if (collector == null) {
            logger.warn("collector is null!");
        } else {
            this.collector.print(printToFile);
        }

    }

    protected IndividualCatcher getBestIndiv() {
        IndividualCatcher indivCatch = this.collector.getBest();
        return indivCatch;
    }

    /**
     * Run the loop pipeline.
     * @return int number of iterations
     **/
    protected int run() {
        long st = System.currentTimeMillis();
        // Get the number of generations
        int its = 100;// Default
        boolean stopWhenSolved = false;

        /*
         * How many fitness evaluations should be done in order to
         * count a generation, in the generational sense.
         */
        int fitnessEvaluationsPerGeneration = 1;
        try {
            String key = Constants.GENERATION;
            String value = this.properties.getProperty(key);
            if (value != null) {
                if (!value.equals("")) {
                    its = Integer.parseInt(value);
                }
            }
            key = Constants.STOP_WHEN_SOLVED;
            value = this.properties.getProperty(key);
            if (value != null) {
                if (!value.equals("")) {
                    if (value.equals(Constants.TRUE)) {
                        stopWhenSolved = true;
                    }
                }
            }
            /*
             * If Steady state is used out each generation in a
             * generational sense only has 2 fitness evaluations. Set
             * the iterations to GENERATIONS*POPULATION_SIZE/2
             */
            if (this.properties.getProperty(Constants.REPLACEMENT_TYPE, "").equals(Constants.STEADY_STATE)) {
                fitnessEvaluationsPerGeneration = Integer.parseInt(this.properties.getProperty(Constants.POPULATION_SIZE, Constants.DEFAULT_POPULATION_SIZE)) / 2;
                its = its * fitnessEvaluationsPerGeneration;
            }
        } catch (Exception e) {
            logger.warn(e + " default generations:" + its, e);
        }
        for (int i = 1; i <= its; i++) {
            if (this.collector != null) {
                if (!foundOptimum(stopWhenSolved, ((StatisticsCollectionOperation) this.collector.getOperation()).getStats())) {
                    this.algorithm.step();
                    long et = System.currentTimeMillis();
                    /* Only collect output after enough fitness
                     * evaluations to repalce the population
                     */
                    if (i % fitnessEvaluationsPerGeneration == 0) {
                        ((StatisticsCollectionOperation) this.collector.getOperation()).printStatistics(et - st);
                        st = System.currentTimeMillis();
                    }
                }
            }
        }
        return its;
    }

    /**
     * Check if the global optimum value has been found
     * @param stopWhenSolved if algorithm should terminate when the global optimum is found
     * @param stats stataistics collection
     * @return if global optimum has been found
     */
    @SuppressWarnings({"BooleanMethodIsAlwaysInverted"})
    protected boolean foundOptimum(boolean stopWhenSolved, StatCatcher stats) {
        boolean ret = false;
        if (stopWhenSolved) {
            if (stats.getCurrentBestFitness() == 0.0) {
                ret = true;
            }
        }
        return ret;
    }

    protected MutationOperation getMutationOperation(RandomNumberGenerator rng, Properties p) {
        MutationOperation mutationOperation = null;
        String className;
        String key;
        key = Constants.MUTATION_OPERATION;
        try {
            className = p.getProperty(key);
            if (className == null || className.trim().length() == 0) {
                className = "geva.Operator.Operations.IntFlipMutation";
                System.out.println("Using default mutation [IntFlipMutation]");
            }
            Class clazz = Class.forName(className);
            mutationOperation = (MutationOperation) clazz.getConstructor(RandomNumberGenerator.class,
                    Properties.class).newInstance(rng, p);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        return mutationOperation;
    }

    protected CrossoverOperation getCrossoverOperation(RandomNumberGenerator rng, Properties p) {
        CrossoverOperation crossoverOperation = null;
        String className;
        String key;
        key = Constants.CROSSOVER_OPERATION;
        try {
            className = p.getProperty(key);
            if (className == null || className.trim().length() == 0) {
                className = "geva.Operator.Operations.SinglePointCrossover";
                System.out.println("Using default crossover [SinglePointCrossover]");
            }
            Class clazz = Class.forName(className);
            crossoverOperation = (CrossoverOperation) clazz.getConstructor(RandomNumberGenerator.class,
                    Properties.class).newInstance(rng, p);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        return crossoverOperation;
    }

    /**
     * helper method for getting the StatisticsCollectionOperation
     * @return Collector collector module
     **/
    protected Collector getCollector() {
        if (((AbstractAlgorithm) this.getAlgorithm()).getLoopPipeline() != null) {
            Collection<Module> m = ((AbstractAlgorithm) this.getAlgorithm()).getLoopPipeline().getModules();
            Iterator<Module> iM = m.iterator();
            Module mo;
            while (iM.hasNext()) {
                mo = iM.next();
                if (mo.getClass().getName().equals(Collector.class.getName())) {
                    if (((Collector) mo).getOperation() instanceof StatisticsCollectionOperation) {
                        return (Collector) mo;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Read the commandline arguments. Find if -h is called. Find if properties field is given
     * Prints the usage help
     * @param args Command-line arguments
     * @return booelan value of success
     **/
    @SuppressWarnings({"IOResourceOpenedButNotSafelyClosed"})
    protected boolean commandLineArgs(String[] args) {
        boolean ret = true;
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                if (args[i].equals(Constants.HELP_FLAG)) { //print program help
                    printProgramHelp();
                    ret = false;
                } else {
                    if (args[i].equals("-" + Constants.PROPERTIES_FILE) && ((i + 1) < args.length)) { //properties file
                        this.propertiesFilePath = args[i + 1];
                    } else {
                        if (args[i].equals(Constants.VERSION_FLAG)) {
                            printVersion();
                            if (args.length == 1) {
                                ret = false;
                            }
                        }
                    }
                }
            }
        }
        return ret;
    }

    void printVersion() {
        StringBuffer sb = new StringBuffer();
        sb.append("GEVA - Grammatical evolution in JAVA");
        sb.append(System.getProperty("line.separator"));
        sb.append("Version: 1");
        sb.append(System.getProperty("line.separator"));
        sb.append("Authors: Erik Hemberg, Conor Gilligan");
        sb.append(System.getProperty("line.separator"));
        sb.append("Developed by UCD Natural Computing Research & Applications group");
        sb.append(System.getProperty("line.separator"));
        sb.append("http://ncra.ucd.ie");
        logger.info(sb.toString());
    }

    void printProgramHelp() {
        InputStream is;
        try {
            File f = new File(this.propertiesFilePath);
            is = new FileInputStream(f);
            this.properties = new Properties();
            this.properties.load(is);
            logger.info("Commandline arguments");
            logger.info("	"+ Constants.HELP_FLAG+" for help");
            logger.info("	"+ Constants.VERSION_FLAG+" for version");
        } catch (IOException e) {
            logger.warn(e + " no default properties file at "+this.propertiesFilePath, e);
        }
        for (Enumeration e = properties.keys(); e.hasMoreElements();) {
            logger.info("	-" + e.nextElement());
        }
    }

    /**
     * Read the default properties. Replace or add properties from the command line.
     * If the file is not found on the file system class loading from the jar is tried
     * @param args arguments
     */
	@SuppressWarnings( { "IOResourceOpenedButNotSafelyClosed" })
	protected void readProperties(String[] args) {
		ClassLoader loader;
		InputStream in;
		try {
			this.properties = new Properties();
			File f = new File(this.propertiesFilePath);
			if (!f.exists()) { // try classloading
				loader = ClassLoader.getSystemClassLoader();
				in = loader.getResourceAsStream(this.propertiesFilePath);
				this.properties.load(in);
				logger.info("Loading properties from ClassLoader and: " + this.propertiesFilePath);
			} else {
				FileInputStream is = new FileInputStream(f);
				this.properties.load(is);
				logger.info("Loading properties from file system: " + this.propertiesFilePath);
			}
			if (args != null) {
				for (int i = 0; i < args.length; i++) {
					if (args[i].startsWith("-") && args.length > (i + 1) && !args[i].equals(Constants.VERSION_FLAG)) {
						this.properties.setProperty(args[i].substring(1), args[i + 1]);
						i++;
					}
				}
			}
			for (Enumeration e = properties.keys(); e.hasMoreElements();) {
				String key = (String) e.nextElement();
				logger.info(key + "=" + properties.getProperty(key, "Not defined"));
			}
		} catch (IOException e) {
			loader = ClassLoader.getSystemClassLoader();
			in = loader.getResourceAsStream(this.propertiesFilePath);
			try {
				this.properties.load(in);
			} catch (Exception ex) {
				System.err.println("Properties reading output caught:" + ex);
			}
			System.err.println(MessageFormat.format("Using default: {0} Bad:{1} Could not load:{2}", this.propertiesFilePath, this.propertiesFilePath, e));
		} catch (Exception e) {
			System.err.println("Could not commandline argument:" + e + " properties path:" + this.propertiesFilePath);
		}
	}

    protected void setOutput() {
        String stdOut = "";
        String stdErr = "";
        try {
            String key = Constants.STDOUT;
            String value = this.properties.getProperty(key);
            if (value != null) {
                if (!value.equals("")) {
                    stdOut = System.getProperty("user.dir") + System.getProperty("file.separator") + value + ".out";
                }
            }
            key = Constants.STDERR;
            value = this.properties.getProperty(key);
            if (value != null) {
                if (!value.equals("")) {
                    stdErr = System.getProperty("user.dir") + System.getProperty("file.separator") + value + ".err";
                }
            }
            if (!stdOut.equals("")) {
                logger.info("Redirecting stdOut to " + stdOut);
                FileOutputStream fos = new FileOutputStream(stdOut, true);
                PrintStream ps = new PrintStream(fos);
                System.setOut(ps);
            }
            if (!stdErr.equals("")) {
                logger.info("Redirecting stdErr to " + stdErr);
                FileOutputStream fos = new FileOutputStream(stdErr, true);
                PrintStream ps = new PrintStream(fos);
                System.setErr(ps);
            }
        } catch (Exception e) {
            logger.error(" std output exception", e);
        }
    }
}
