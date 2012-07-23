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

package geva.gui.Util;

/**
 * This class has all the constants.
 * The names for properties.
 */
public final class Constants {

    //Property file fields. Works as command line args as well
    /**
     * Class name of fitness function. Reflection used to load
     */
    public static final String FITNESS_FUNCTION = "fitness_function";

    /**
     * Class name of selection operation. Reflection used to load
     */
    public static final String SELECTION_OPERATION = "selection_operation";

    /**
     * Class name of replacement operator. Reflection used to load
     */
    public static final String REPLACEMENT_OPERATOR = "replacement_operator";

    /**
     * Path to properties file
     */
    public static final String PROPERTIES_FILE = "properties_file";

    /**
     * Class name of initialiser operation. Reflection used to load
     */
    public static final String INITIALISER = "initialiser";

    /**
     * Path for output file. Starts in user.dir
     * Output files are appended a timestamp and *.dat
     * If output is set to false no output is written
     */
    public static final String OUTPUT = "output";

    /**
     * Number of iterations of algorithm
     */
    public static final String GENERATION = "generations";

    /**
     * Stop algorithm if global optimum is found before max iterations
     */
    public static final String STOP_WHEN_SOLVED = "stopWhenSolved";

    /**
     * Path to grammar file *.bnf
     */
    public static final String GRAMMAR_FILE = "grammar_file";

    /**
     * Number of times the input is reread from start.
     */
    public static final String MAX_WRAPS = "max_wraps";

    /**
     * Probability of choosing a to grow a full depth tree when rampde full grow
     * initialisation is used.
     */
    public static final String GROW_PROBABILITY = "grow_probability";

    /**
     * Max depth of tree growth for full and grow initialisation
     */
    public static final String MAX_DEPTH = "max_depth";

    /**
     * Number of elites.
     */
    public static final String ELITE_SIZE = "elite_size";

    /**
     * If the elites should be evaluated each iteration
     */
    public static final String EVALUATE_ELITES = "evaluate_elites";

    /**
     * Length of input for random initialisation
     */
    public static final
        String INITIAL_CHROMOSOME_SIZE = "initial_chromosome_size";

    /**
     * Selection size proportion of population used selected.
     * E.g 1.0 generates a selection of population size
     */
    public static final String SELECTION_SIZE = "selection_size";

    /**
     * Size of individual solutions
     */
    public static final String POPULATION_SIZE = "population_size";

    /**
     * Generational or steady state replacment. If mor control is needed use
     * the generation gap and selection size parameters.
     */
    public static final String REPLACEMENT_TYPE = "replacement_type";

    /**
     * If the crossover point is fixed
     */
    public static final String FIXED_POINT_CROSSOVER = "fixed_point_crossover";

    /**
     * Size of tournament for tournament selection
     */
    public static final String TOURNAMENT_SIZE = "tournament_size";

    /**
     * Size of population for user selection
     */
    public static final String USERPICK_SIZE = "userpick_size";

    /**
     * Interval between saved individuals in the population
     */
    public static final
        String INDIVIDUAL_CATCH_INTERVAL = "individual_catch_interval";

    /**
     * Probability of mutating an input
     */
    public static final String MUTATION_PROBABILITY = "mutation_probability";

    /**
     * Probability of crossing over inputs
     */
    public static final String CROSSOVER_PROBABILITY = "crossover_probability";

    /**
     * Probability of duplicating inputs
     */
    public static final
        String DUPLICATION_PROBABILITY = "duplication_probability";

    /**
     * Porportion of new solutions (population) that will be inserted among the
     *  old solutions (population)
     */
    public static final String GENERATION_GAP = "generation_gap";

    /**
     * Word to match when using the wordmatch fitness function
     */
    public static final String WORDMATCH_WORD = "word";

    /**
     * The seed for the random number generator.
     */
    public static final String RNG_SEED = "rng_seed";
    //Property file values
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    /**
     * Sets generation gap to 1/population size.
     * Sets selection size to 2/population size.
     */
    public static final String STEADY_STATE = "steady_state";

    /**
     * Sets generation gap to population size.
     * Sets selection size to population size.
     */
    public static final String GENERATIONAL = "generational";
    //Other values
    /**
     * Path to default properties file
     */
    public static final
        String DEFAULT_PROPERTIES = "Parameters/HelloWorld.properties";

    /**
     * Signature of a GE Codon. Indicates special treatment in the parsing
     */
    public static final CharSequence GE_CODON_VALUE = "GECodonValue";

    /**
     * Identifying a GE codon when parsing
     */
    public static final String GE_CODON_VALUE_PARSING = "<"+GE_CODON_VALUE;

    /**
     * Sets number of step interactions to perform (higher is more accurate)
     */
    public static final String ODE_INTERACTIONS = "ode_interactions";

    /**
     * Sets size of a single step (number of seconds of action per step, lower
     *  is more accurate)
     */
    public static final String ODE_STEPSIZE = "ode_step_size";

    /**
     * Sets number of simulation seconds to run for
     */
    public static final String ODE_STEPS = "ode_steps";

    /**
     * Sets wall width in blocks in ode DamageCalc
     */
    public static final String ODE_DAMAGE_WALL_WIDTH = "ode_damage_wall_width";

    /**
     * Sets wall height in blocks in ode DamageCalc
     */
    public static final String ODE_DAMAGE_WALL_HEIGHT = "ode_damage_wall_height";

    public static final String TARGET_FRACTAL_DIMENSION = "targetFractalDimension";
    public static final String BOX_COUNT_DIVISION = "boxCountDivision";

    //Command line args
    public static final String VERSION_FLAG = "-v";
    public static final String HELP_FLAG = "-h";

    /**
     * Text appended to the window caption when the properties of that window
     *  are dirty
     */
    public static final String DIRTY_SYMBOL = " *";

    /**
     * Image displayed when GUI starts
     */
    public static final
        String IMG_SPLASH = "UI/res/gevasplash400x300.002.001.jpg";
    /**
     * Image displayed on image save button
     */
    public static final String IMG_SAVE = "UI/res/save.png";

    public static final String GRAPH_CATEGORY_GEVA = "GEVA";

    /**
     * Value output by GEVA of how many generations it will execute
     */
    public static final String GRAPH_DATA_GENERATIONS = "generations=";
    /**
     * Value output by GEVA a line before it begins outputting data
     */
    public static final String GRAPH_DATA_BEGIN = "#---Data---";

    /**
     * Format used to display statistics information on the graph run pane
     */
    public static final String GRAPH_FORMAT = "%.3f";

    /**
     * Startup properties file selected in GUI
     */
    public static final String cfgSelectedProps = "selectedPropertiesFile";
    /**
     * Default ui configuration. A configuration script that
     *  describes the property controls to display
     */
    public static final String cfgConfigPath = "configPath";
    /**
     * Default directory to properties files
     */
    public static final String cfgPropertiesPath = "propertiesPath";
    /**
     * Default directory to grammar files
     */
    public static final String cfgGrammarPath = "grammarPath";
    /**
     * Default java executable name
     */
    public static final String cfgJavaName = "javaName";
    /**
     * Default class path for running GEVA
     */
    public static final String cfgClassPath = "classPath";
    /**
     * Default class for running GEVA
     */
    public static final String cfgClassName = "className";
    /**
     * Default working directory; null = current directory
     */
    public static final String cfgWorkingAbsPath = "workingAbsPath";
    /**
     * Expert flag
     */
    public static final String cfgExpert = "expert";
    /**
     * Path where configuration is saved
     */
    public static final String cfgPath = "./gui.config";
    /**
     * FF config filename
     */
    public static final String cfgFFFile = "ff.config";
    /**
     * Graph config filename
     */
    public static final String cfgGraphFile = "graph.config";

    /**
     * Default selected properties file
     */
    public static final String defSelectedProps = "HelloWorld.properties";
    /**
     * Default ui configuration. A configuration script that
     *  describes the property controls to display
     */
    public static final String defConfigPath = "./param/";
    /**
     * Default directory to properties files
     */
    public static final String defPropertiesPath = "./param/Parameters/";
    /**
     * Default directory to grammar files
     */
    public static final String defGrammarPath = "./param/Grammar/";
    /**
     * Default java executable name
     */
    public static final String defJavaName = "java";
    /**
     * Default class path for running GEVA
     */
    public static final String defClassPath = "GEVA.jar";
    /**
     * Default class for running GEVA
     */
    public static final String defClassName = "Main.Run";
    /**
     * Default working directory; null = current directory
     */
    public static final String defWorkingAbsPath = ".";
    /**
     * Specifiy maximum size of memory allocation pool for GEVA
     */
    public static final String cfgHeapSize = "heapSize";
    /**
     * Set the JVM flag to server.
     */
    public static final String cfgServer = "server";
    /**
     * The file extension for properties files. These are filtered when scanning
     *  the properties directory
     */
    public static final String txtPropertiesExt = ".properties";
    /**
     * The file extension for grammar files. These are filtered when scanning
     *  the grammar directory
     */
    public static final String txtGrammarExt = ".bnf";
    /**
     * Extension for PostScript
     */
    public static final String txtPostScriptExt = ".ps";
    /**
     * Global parser extension name
     */
    public static final String extGlobal = "global";
    /**
     * Graph parser extension name
     */
    public static final String extGraph = "graph";
    /**
     * Command line flag synonyms for config path.
     * Format is, first element is the id of the command: this is used by
     *  GEVAConfig.parseFlag as the return value when a flag is encountered. The
     *  second through second-last elements all make up the synonyms for the
     *  flag: if parseFlag encounters any one of these, it returns the related
     *  id. The last element is a description of the flag, and is output when
     *  the help flag is set, or an unknown flag is encountered
     */
    public static final String[][] cmdFlags =
    {   {"1",
            "--configpath", "-c",
            "[-c <" + I18N.get("ui.gui.args.p") + ">] - "
          + I18N.get("ui.gui.args.1", Constants.defConfigPath)},
        {"2",
             "--propertiespath", "-p",
             "[-p <" + I18N.get("ui.gui.args.p") + ">] - "
           + I18N.get("ui.gui.args.2", Constants.defPropertiesPath)},
        {"3",
             "--grammarpath", "-g",
             "[-g <" + I18N.get("ui.gui.args.p") + ">] - "
           + I18N.get("ui.gui.args.3", Constants.defGrammarPath)},
        {"4",
             "--java", "-j",
             "[-j <" + I18N.get("ui.gui.args.n") + ">] - "
           + I18N.get("ui.gui.args.4", Constants.defJavaName)},
        {"5",
             "--classpath", "-cp",
             "[-cp <" + I18N.get("ui.gui.args.p") + ">] - "
           + I18N.get("ui.gui.args.5", Constants.defClassPath)},
        {"6",
             "--classname", "-cn",
             "[-cn <" + I18N.get("ui.gui.args.n") + ">] - "
           + I18N.get("ui.gui.args.6", Constants.defClassName)},
        {"7",
             "--workingpath", "-wp", "-cd",
             "[-wp <" + I18N.get("ui.gui.args.p") + ">] - "
             + I18N.get("ui.gui.args.7")},
        {"8",
             "--heapsize", "--heap", "-hs", "-xmx",
             "[-hs <" + I18N.get("ui.gui.args.h") + ">] - "
           + I18N.get("ui.gui.args.8")},
        {"-3",
             "--server", "-s",
             "[--server - "
           + I18N.get("ui.gui.args.-3")},
        {"-2",
             "--expert",
             "[--expert] - "
           + I18N.get("ui.gui.args.-2")},
        {"-1",
             "--help", "-h", "-?", "/?",
             "[-?] - "
           + I18N.get("ui.gui.args.-1")}
    };

    private Constants() {}

}
