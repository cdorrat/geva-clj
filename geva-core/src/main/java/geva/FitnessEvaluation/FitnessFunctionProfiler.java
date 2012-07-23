package geva.FitnessEvaluation;

import geva.Individuals.Individual;
import geva.Individuals.Populations.Population;
import geva.Main.Run;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import clojure.lang.RT;
import clojure.lang.Var;

public class FitnessFunctionProfiler extends Run {
	private Log logger = LogFactory.getLog(FitnessFunctionProfiler.class);

	
	FitnessFunction fitnessFn;
	private Population population;
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args)  {
		FitnessFunctionProfiler prof = new FitnessFunctionProfiler();
//		prof.init(args);
//		prof.profile();

		try {
			prof.profileClojureEvalLoop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void profile() {
		logger.info("Starting profiling");
		long startTime = System.nanoTime();	
		for(Individual i : population.getAll()) {
			fitnessFn.getFitness(i);
		}
		long endTime = System.nanoTime();
		float elapsedMs = (endTime - startTime) * 1E-6f;
		logger.info("Completed evaluation in " + elapsedMs  + " msecs");
	}
	
	private void profileClojureEvalLoop() throws Exception {
		final String expr = "(fn [x] (* (* 1.0 (+ (- 1.0 1.0) (- (* 1.0 (- (+ x x ) x )) (- x x )))) x))";
		RT.loadResourceScript("geva.FitnessEvaluation/simple_clojure_sr.clj");
		Var fitnessFn = RT.var("symbolic-regression", "fitness-fn");
		
		logger.info("Starting profiling");
		long startTime = System.nanoTime();
		for(int i=0; i < 5000; ++i) {
			Object indFn = clojure.lang.Compiler.eval(RT.readString(expr));;
			fitnessFn.invoke(indFn);
		}
		long endTime = System.nanoTime();
		float elapsedMs = (endTime - startTime) * 1E-6f;
		logger.info("Completed evaluation in " + elapsedMs  + " msecs");		
	}
	

	private void init(String[] args) {		
		commandLineArgs(args);
		setup(args);
		super.init();
		fitnessFn = getFitnessFunction(properties);
		fitnessFn.setProperties(properties);
		
		// load a population of individuals here
		population  = initialiser.getPopulation();
		
		
	}

}
