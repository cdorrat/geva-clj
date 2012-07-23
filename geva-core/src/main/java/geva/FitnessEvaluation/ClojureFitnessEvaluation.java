package geva.FitnessEvaluation;

import geva.Individuals.Phenotype;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import clojure.lang.IFn;
import clojure.lang.RT;
import clojure.lang.Var;


public class ClojureFitnessEvaluation extends InterpretedFitnessEvaluation {
	
	public final static String CLOJURE_FITNESS_FILENAME = "clojure.fitness.file";
	public final static String CLOJURE_FITNESS_NS = "clojure.fitness.namespace";
	public final static String CLOJURE_FITNESS_FUNCTION = "clojure.fitness.function";
	public final static String CLOJURE_IND_FUNCTION = "clojure.expression.function";
	
	PhenotypeEvaluator phenoTypeEval;
	
	private interface PhenotypeEvaluator {
		double getFitness(Phenotype p);
	}
	
	public ClojureFitnessEvaluation() {			
	}
	
	@Override
	public double runFile(Phenotype p) {
		return phenoTypeEval.getFitness(p);
	}

	@Override
	public void setProperties(Properties p) {

		try {
			Reader reader = new InputStreamReader(getClass().getResourceAsStream("/geva.Util/Clojure/geva.clj"));
			clojure.lang.Compiler.load(reader);
			
			RT.var("geva", "*geva-properties*", p); // make the properties available to clojure code as geva/properties
			
			String clojureFileName = p.getProperty(CLOJURE_FITNESS_FILENAME);			
			reader = new FileReader(clojureFileName);
			clojure.lang.Compiler.load(reader);
			 
			String namespace = p.getProperty(CLOJURE_FITNESS_NS);
			String indExpressionFnName = p.getProperty(CLOJURE_IND_FUNCTION);
			String fitnessFnName = p.getProperty(CLOJURE_FITNESS_FUNCTION);
			
			if (indExpressionFnName != null) {
				phenoTypeEval = new GeneratedClojureEvaluator(namespace, indExpressionFnName, fitnessFnName);
			} else {
				phenoTypeEval = new SimpleClojureEvaluator(namespace, fitnessFnName);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Simple Clojure evaluator that passses the pheotype to the fitness function as a string.
	 */
	private static class SimpleClojureEvaluator implements PhenotypeEvaluator {
		private Var fitnessFn;

		public SimpleClojureEvaluator(String namespace, String fitnessFnName) throws Exception {
			fitnessFn =  RT.var(namespace, fitnessFnName);						
		}

		@Override
		public double getFitness(Phenotype p) {
			Double fitnessVal;
			try {
				fitnessVal = (Double) fitnessFn.invoke(p.getStringNoSpace());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return fitnessVal;
		}
	}
	
	/**
	 * An evaluator that uses one function to convert the phenotype to a clojure function 
	 * and another fitness funtion to get the fitness value from the function in created step 1.
	 * 
	 *  This allows us to use macros to perform additional code generation befor eevaluating an individual 
	 *  (or just compile syntactically valid code before evaluation) 
	 */
	private static class GeneratedClojureEvaluator implements PhenotypeEvaluator {
		private Var fitnessFn, expressIndFn, evalFn;
		private String expressFnName;
		
		public GeneratedClojureEvaluator(String namespace, String expressIndFnName, String fitnessFnName) throws Exception {
			expressIndFn = RT.var(namespace, expressIndFnName);
			expressFnName = namespace + "/" + expressIndFnName;
			fitnessFn = RT.var(namespace, fitnessFnName);					
			evalFn = RT.var("clojure.core", "eval");
		}

		@Override
		public double getFitness(Phenotype p) {
			Object indFn;
			try {
				//System.out.println("--" + p.getString());
				indFn = clojure.lang.Compiler.eval(RT.readString(p.getString()));;
				
//				if (expressIndFn.isMacro()) {
//					indFn = expressIndFn.invoke(RT.readString(p.getString()));					
//					indFn = evalFn.invoke(indFn);				
//				} else {
//					indFn = expressIndFn.invoke(p.getString());	
//				}
				return (Double) fitnessFn.invoke(indFn);
			} catch (Exception e) {
				throw  new RuntimeException(e);
			}

		}
		
	}
}
