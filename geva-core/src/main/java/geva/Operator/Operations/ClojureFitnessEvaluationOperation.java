package geva.Operator.Operations;

import geva.FitnessEvaluation.FitnessFunction;
import geva.Individuals.Individual;

import java.io.FileReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import clojure.lang.IFn;
import clojure.lang.RT;


public class ClojureFitnessEvaluationOperation extends FitnessEvaluationOperation {
	private static Log logger = LogFactory.getLog(ClojureFitnessEvaluationOperation.class); 

	public final static String CACHE_FITNESS_VALS_PARAM = "cache_fitness";
	public final static String CLOJURE_FITNESS_FILENAME_PARAM = "clojure.fitness.file";
	public final static String CLOJURE_FITNESS_NS_PARAM = "clojure.fitness.namespace";
	public final static String CLOJURE_FITNESS_FUNC_PARAM = "clojure.fitness.function";
	public final static String CLOJURE_IND_FUNC_PARAM = "clojure.expression.function";
	public final static String CLOJURE_GROUP_EVAL_FUNC_PARAM = "clojure.group.fitness.function";
	public final static String CLOJURE_PARALELL_EVAL = "clojure.paralell_eval";
	
	final static String DEFAULT_CLOJURE_NAMESPACE = "geva";
	final static String DEFAULT_GROUP_PEVAL_FN = "pset-group-fitness!";
	final static String DEFAULT_GROUP_EVAL_FN = "set-group-fitness!";
	final static String DEFAULT_IND_FUNC = "default-create-ind";
	
 
	private IFn groupEvalFn; 	// clojure function used to evaluate populations
	private IFn fitnessFn;    // clojure function used to calculate fitness of a single individual (usually user provided) 
	private IFn createIndFn;  // function used to map an Individual to the form accepted by the fitness function, default is eval(read-string(ind.getString()))
        private boolean isUserGroupEvalFunction;
	private FitnessCache cache;
	
	public ClojureFitnessEvaluationOperation(IFn groupEvalFn) {
		// fitness calculatiosn are done by the clojure fitness function,
		// just pass a dummy value to our parent, it checks canCache in several places
                super(new FitnessFunction() {
                    public void setProperties(Properties p) {}
                    public void getFitness(Individual i) {}
                    public boolean canCache() {return true;}
		});
            this.isUserGroupEvalFunction = true;
            this.groupEvalFn = groupEvalFn;

        }
	public ClojureFitnessEvaluationOperation(IFn fitnessFunction, IFn createIndFn) {
            this(null);
            fitnessFn = fitnessFunction;
            this.createIndFn = createIndFn;
            this.isUserGroupEvalFunction = false;
	}

	
	@Override
	public void setProperties(Properties p) {
		super.setProperties(p);
		try {
                    if (! isUserGroupEvalFunction) {
			String clojureFileName = p.getProperty(CLOJURE_FITNESS_FILENAME_PARAM);
			if (clojureFileName != null) {
				Reader reader = new FileReader(clojureFileName);
				clojure.lang.Compiler.load(reader);
				// TODO determine namespace from loaded file
			}

			String namespace = p.getProperty(CLOJURE_FITNESS_NS_PARAM);
			String indExpressionFnName = p.getProperty(CLOJURE_IND_FUNC_PARAM);
			String groupEvalFnName = p.getProperty(CLOJURE_GROUP_EVAL_FUNC_PARAM);
			String fitnessFnName = p.getProperty(CLOJURE_FITNESS_FUNC_PARAM);
			
		
			if (indExpressionFnName != null) {
				createIndFn = RT.var(namespace, indExpressionFnName);
			}
			if (createIndFn == null) {
				createIndFn = RT.var(DEFAULT_CLOJURE_NAMESPACE, DEFAULT_IND_FUNC);
			}
			
			
			if (groupEvalFnName != null) {
				groupEvalFn = RT.var(namespace, groupEvalFnName);
			}
			if (groupEvalFn == null) {
				boolean parEval = "YES".equalsIgnoreCase(p.getProperty(ClojureFitnessEvaluationOperation.CLOJURE_PARALELL_EVAL, "YES"));
				if (parEval) {
					groupEvalFn = RT.var(DEFAULT_CLOJURE_NAMESPACE, DEFAULT_GROUP_PEVAL_FN);
					logger.info("Using paralell population evaluation");
				} else {
					groupEvalFn = RT.var(DEFAULT_CLOJURE_NAMESPACE, DEFAULT_GROUP_EVAL_FN);
					logger.info("Using single threaded population evaluation");
				}
			}
		
			if (fitnessFn == null && fitnessFnName != null) {			
				fitnessFn =  RT.var(namespace, fitnessFnName);
			}
                    }
                        boolean canCacheFitness = Boolean.parseBoolean(p.getProperty(CACHE_FITNESS_VALS_PARAM, "true"));
			if (canCacheFitness) {
				cache = new FitnessCache() {
					@Override
					public boolean getFitnessFromCache(Individual operand) {
						return ClojureFitnessEvaluationOperation.this.getFitnessFromCache(operand);
					}
					
					@Override
					public void addFitnessToCache(Individual operand) {
						ClojureFitnessEvaluationOperation.this.addFitnessToCache(operand);
					}
				};
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public interface FitnessCache {
		boolean getFitnessFromCache(Individual operand);
		void addFitnessToCache(Individual operand);
	}
	
	@Override
	public void doOperation(Individual operand) {
		doOperation(Arrays.asList(operand));
	}

	@Override
	public void doOperation(List<Individual> operands) {
		try {
                    if (isUserGroupEvalFunction) {
                        groupEvalFn.invoke(operands, cache);
                    } else {
			groupEvalFn.invoke(operands, cache, createIndFn, fitnessFn);
                    }
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

		
	
}
