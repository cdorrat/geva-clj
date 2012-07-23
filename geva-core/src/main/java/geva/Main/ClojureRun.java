package geva.Main;

import geva.FitnessEvaluation.FitnessFunction;
import geva.Mapper.GEGrammar;
import geva.Operator.Operations.ClojureFitnessEvaluationOperation;
import geva.Operator.Operations.FitnessEvaluationOperation;
import geva.Operator.Operations.StatisticsCollectionOperation;
import geva.Util.Constants;
import geva.Util.Random.RandomNumberGenerator;
import geva.Util.Random.ThreadSafeRandomNumberGenerator;
import geva.Util.Statistics.StatCatcher;

import java.util.Properties;
import java.util.logging.Level;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import clojure.lang.Agent;
import clojure.lang.IFn;
import clojure.lang.IPersistentMap;
import clojure.lang.Keyword;
import clojure.lang.PersistentHashMap;
import clojure.lang.PersistentList;
import clojure.lang.RT;
import clojure.lang.Symbol;
import clojure.lang.Var;
import geva.FitnessEvaluation.ClojureFitnessEvaluation;
import geva.Individuals.Individual;
import java.util.List;
import java.util.ListIterator;

public class ClojureRun extends Run {

    private static Log logger = LogFactory.getLog(ClojureRun.class);
    private static final long serialVersionUID = 1L;
    private GEGrammar grammar;
    private IFn fitnessFuncton;
    private IFn createIndFuncton;
    private IFn groupEvalFn;

    @Override
    protected FitnessEvaluationOperation createFitnessEvalOperation(FitnessFunction fitnessFunction) {
        if (groupEvalFn != null) {
            return new ClojureFitnessEvaluationOperation(groupEvalFn);
        } else {
            return new ClojureFitnessEvaluationOperation(fitnessFuncton, createIndFuncton);
        }
    }

    @Override
    protected RandomNumberGenerator createRNG() {
        return new ThreadSafeRandomNumberGenerator();
    }

    @Override
    protected GEGrammar getGEGrammar(Properties p) {
        return grammar == null ? super.getGEGrammar(p) : grammar;
    }

    @Override
    public GEGrammar getGrammar() {
        return grammar == null ? super.getGrammar() : grammar;
    }

    public void setGrammar(GEGrammar grammar) {
        this.grammar = grammar;
    }



    public void setProperties(Properties props) {
        this.properties = props;
    }

    public void setFitnessFunction(IFn fitnessFn) {
        this.fitnessFuncton = fitnessFn;
    }

    @Override
    protected FitnessFunction getFitnessFunction(Properties p) {
        if (p.containsKey(Constants.FITNESS_FUNCTION)) {
            // We're using a java fitness function but we want to be able to evaluate populations with clojure
            // this creates a wrapper function
            createIndFuncton = RT.var("clojure.core", "identity"); // no mapping required for individuals
            FitnessFunction fn = super.getFitnessFunction(p);
            Var helperFn = RT.var("geva", "ifn-from-fitness-fn");
            try {
                fitnessFuncton = (IFn) helperFn.invoke(fn);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // Unless explitly asked for we force single threaded fitness evaluation
            // most java code isn't thread safe by default.
            if (!p.containsKey(ClojureFitnessEvaluationOperation.CLOJURE_PARALELL_EVAL)) {
                p.setProperty(ClojureFitnessEvaluationOperation.CLOJURE_PARALELL_EVAL, "no");
            }
            return fn;
        } else {
            // The fitness function used is in the user supplied clojure code.
            // @see geva.FitnessEvaluation.ClojureFitnessEvaluation for details
            return null;
        }
    }

    @Override
    public void setup(String[] args) {

        super.setup(args);
        try {
            // we're using AOT for geva classes, shoudn't need the source
            RT.loadResourceScript("geva.clj", false);
            // make the properties and rng available to clojure code as geva/*geva-properties* & geva/*geva-rng* respectively
            RT.var("geva", "*geva-properties*", this.properties);
            RT.var("geva", "*geva-rng*", this.rng);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public static void main(String[] args) {
        initJavaLogging(Level.ALL);
        try {
            long st = System.currentTimeMillis();
            Run mfs = new ClojureRun();

            if (mfs.commandLineArgs(args)) {
                mfs.setup(args);
                mfs.init();
                int its = mfs.run();
                mfs.printStuff();
                long et = System.currentTimeMillis();
                logger.info("Done running: Total time(Ms) for " + its + " generations was:" + (et - st));
                Agent.shutdown();
            }
        } catch (Exception e) {
            logger.error("Exception caught runnung main:", e);
        }
    }

    @Override
    protected void readProperties(String[] args) {
        if (this.properties == null) {
            super.readProperties(args);
        }
    }

    public IPersistentMap runWithStats() {
        long st = System.currentTimeMillis();
        super.run();
        long elapsedMs = (System.currentTimeMillis() - st);
        
        IPersistentMap res = PersistentHashMap.create(createKeyword("elapsed-ms"), elapsedMs,
                createKeyword("stats"), getStatsForClojure(),
                createKeyword("best-individual"), getBestIndividual(getStats().getBestIndividualOfGenerations()),
                createKeyword("final-population"), initialiser.getPopulation().getAll());
        return res;
    }

    private Individual getBestIndividual(List<Individual> inds) {
        ListIterator<Individual> iter = inds.listIterator();
        Individual bestInd = iter.hasNext() ? iter.next() : null;
        while (iter.hasNext()) {
            Individual ind = iter.next();
            if (ind.compareTo(bestInd) < 0) {
                bestInd = ind;
            }
        }
        return bestInd;
    }

    private StatCatcher getStats() {
        return ((StatisticsCollectionOperation) this.collector.getOperation()).getStats();
    }

    private IPersistentMap getStatsForClojure() {
        StatCatcher stats = getStats();

        IPersistentMap m = PersistentHashMap.create(createKeyword("best-fitness"), stats.getCurrentBestFitness(),
                createKeyword("all-fitness"), PersistentList.create(stats.getAllFitness()),
                createKeyword("mean-used-genes"), PersistentList.create(stats.getMeanUsedGenes()),
                createKeyword("number-invalids"), PersistentList.create(stats.getInvalids()),
                createKeyword("best-fitness"), PersistentList.create(stats.getBestFitness()),
                createKeyword("fitness-variance"), PersistentList.create(stats.getVarFitness()),
                createKeyword("mean-fitness"), PersistentList.create(stats.getMeanFitness()),
                createKeyword("avg-length"), PersistentList.create(stats.getAveLength()),
                createKeyword("max-length"), PersistentList.create(stats.getMaxLength()),
                createKeyword("min-length"), PersistentList.create(stats.getMinLength()),
                createKeyword("length-variance"), PersistentList.create(stats.getVarLength()),
                createKeyword("generation-times"), PersistentList.create(stats.getTime()));
        return m;
    }

    private Keyword createKeyword(String name) {
        Symbol s = Symbol.create(name);
        return Keyword.intern(s);
    }

    public static clojure.lang.IPersistentMap run(GEGrammar grammar, Properties props, IFn fitnessFn) {
        initJavaLogging(Level.WARNING);

        ClojureRun mfs = new ClojureRun();

        if (fitnessFn != null) {
            // we can safely ignore any finess functions in the properties file since we'e explicitly passing one in.
            props.remove(Constants.FITNESS_FUNCTION);
            mfs.setFitnessFunction(fitnessFn);
        }

        mfs.setProperties(props);

        if (grammar != null) {
            mfs.setGrammar(grammar);
            grammar.setProperties(props, false);
        }

        mfs.setup(null);
        mfs.init();
        clojure.lang.IPersistentMap m = mfs.runWithStats();

        return m;
    }

    /**
     * Run an evolutionary search with a user provided group evaluation function
     * @param grammar the grammar to use
     * @param props the properties to use
     * @param groupEvalFn a clojure function that will be called with a List<Individual> and a cache. It should set the
     * @return
     */
    public static clojure.lang.IPersistentMap runWithGroupEval(GEGrammar grammar, Properties props, IFn groupEvalFn) {
        initJavaLogging(Level.WARNING);
        ClojureRun mfs = new ClojureRun();

        if (groupEvalFn != null) {
            mfs.groupEvalFn = groupEvalFn;
            props.remove(Constants.FITNESS_FUNCTION);
        } else {
            throw new RuntimeException("A group evaluation function must be specified");
        }

        mfs.setProperties(props);

        if (grammar != null) {
            mfs.setGrammar(grammar);

            grammar.setProperties(props, false);
        } else {
            throw new RuntimeException("A grammar must be specified");
        }

        mfs.setup(null);
        mfs.init();
        clojure.lang.IPersistentMap m = mfs.runWithStats();
        return m;
    }
}
