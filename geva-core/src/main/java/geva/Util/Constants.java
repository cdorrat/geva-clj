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

package geva.Util;

/**
 * This class has all the constants.
 * The names for properties.
 */
public final class Constants {

    //Property file fields. Works as command line args as well
    /**
     * Used for redirecting stdOut
     */
    public static final String STDOUT = "stdOut";

    /**
     * Used for redirecting stdErr
     */
    public static final String STDERR = "stdErr";
    /**
     * Class name of fitness function. Reflection used to load
     */
    public static final String FITNESS_FUNCTION = "fitness_function";

    /**
     * Class name of selection operation. Reflection used to load
     */
    public static final String SELECTION_OPERATION = "selection_operation";

    /**
     * Class name of mutation operation. Reflection used to load
     */
    public static final String MUTATION_OPERATION = "mutation_operation";
    
    /**
     * Class name of crossover operation. Reflection used to load
     */
    public static final String CROSSOVER_OPERATION = "crossover_operation";

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
     * this is the number of elements is the set
     */
    public static final String PRC_SETSIZE = "prc_setsize";
    public static final String DEFAULT_PRC_SETSIZE = "0";
    
     /**
     * this is the range that the constants will be generated within
     */
    public static final String PRC_RANGE = "prc_range";
    public static final String DEFAULT_PRC_RANGE = "100";
    
    /**
     * this is the decimal precision of the constants that are generated
     * the number specifies how many decimal places there should be
     */
    public static final String PRC_PRECISION = "prc_precision";
    public static final String DEFAULT_PRC_PRECISION = "0";
    
    
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
     * Default population size
     */
    public static final String DEFAULT_POPULATION_SIZE = "10";

    /**
     * Default chromosome size
     */
    public static final String DEFAULT_CHROMOSOME_SIZE = "10";

    /**
     * GEGrammar to use
     */
    public static final String GEGRAMMAR = "gegrammar";

    /**
     * Defualt GEGrammar
     */
    public static final String DEFAULT_GEGRAMMAR = "geva.Mapper.GEGrammar";

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
    public static final String DEFAULT_PARAM_ROOT = "../param/";
    /**
     * Path to default properties file
     */
    public static final
        String DEFAULT_PROPERTIES = DEFAULT_PARAM_ROOT
                                 + "Parameters/HelloWorld.properties";

    /**
     * Signature of a GE Codon. Indicates special treatment in the parsing
     */
    public static final CharSequence GE_CODON_VALUE = "GECodonValue";

    /**
     * Identifying a GE codon when parsing
     */
    public static final String GE_CODON_VALUE_PARSING = "<"+GE_CODON_VALUE;

    public static final String MAX_DERIVATION_TREE_DEPTH = "max_dt_depth";
    
    public final static String DEFAULT_MAX_DERIVATION_TREE_DEPTH = String.valueOf(Integer.MAX_VALUE);

    /**
     * Only crossover in the codons used
     */
    public static final String CODONS_USED_SENSITIVE = "codons_used_sensitive";

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
     * This specifies the file location for the target picture
     */
    public static final String TARGET_IMAGE = "target_image";
    
    /**
     * This specifies a phenotype string for the target picture
     */
    public static final String TARGET_PHENOTYPE = "target_phenotype";
    
    /**
     * Sets wall height in blocks in ode DamageCalc
     */
    public static final String ODE_DAMAGE_WALL_HEIGHT = "ode_damage_wall_height";
    
    public static final String TARGET_FRACTAL_DIMENSION = "targetFractalDimension";
    public static final String BOX_COUNT_DIVISION = "boxCountDivision";
    
    /**
     * Sets interactive fitness GE on or off
     */
    public static final String INTERACTIVE_GE = "interactive_ge";
    
    /**
     * After how many generations should the user be prompted for interaction
     */
    public static final String INTERACT_GENS = "interact_generations";
    
    /**
     * Specifies the range to test the fitness for symbolic regression.The format
     * is sr_range= x eq [start; stepSize; stop]; and multiple ranges can be
     * specified eg sr_range = x0 eq [0;.5;2]; x1 eq [2;.1;5]. Use rnd(start, cases ,stop)
     * to specify random points
     */
     public static final String SR_RANGE = "sr_range";

     /**
     * the phenotypic target for symbolic regression
     */
     public static final String SR_TARGET = "sr_target";

     /**
     * boolean specifying whether or not phenotype should be drawn
     */
     public static final String DRAW_PHENOTYPE = "draw_phenotype";

    /**
     * Sets the run class to use
     */
    public static final String EXPERIMENT = "main_class";

    //Command line args
    public static final String VERSION_FLAG = "-v";
    public static final String HELP_FLAG = "-h";
    public static String DERIVATION_TREE = "derivation_tree";


    private Constants() {}

}
