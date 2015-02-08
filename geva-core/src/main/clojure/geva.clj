(ns geva
  (:require [clojure [string :as str] 
             [set :as cs]
	     [pprint :as pp]
             [walk :as walk]]
	    [clojure.java [io :as io]])  
  (:import [geva.Individuals Individual GEIndividual]
           [geva.Individuals.FitnessPackage BasicFitness]
           [geva.Util.Random ThreadSafeRandomNumberGenerator]
           [geva.Operator.Operations ClojureFitnessEvaluationOperation$FitnessCache RandomInitialiser]
           [java.util Properties]
           [geva.Mapper GEGrammar]
           [geva.Main ClojureRun])
  )

(def ^{:dynamic true}  *geva-properties*  (java.util.Properties.))
(def ^{:dynamic true} *geva-rng* (geva.Util.Random.ThreadSafeRandomNumberGenerator.))

(defmulti prop-coerce-to-key "create a property key value" class )
(defmethod prop-coerce-to-key clojure.lang.Keyword [val]
	   (.replaceAll (name val) "-" "_"))
(defmethod prop-coerce-to-key :default  [val] (str val))


(defmulti prop-coerce-to-val "create a property value" class )
(defmethod prop-coerce-to-val java.lang.Class [val] (.getName val))
(defmethod prop-coerce-to-val :default  [val] (str val))

 
(defn property 
  "Get or set a property from the current GEVA properties file"
  ([name] (.getProperty *geva-properties* (prop-coerce-to-key name)))
  ([name value] (.setProperty *geva-properties* (prop-coerce-to-key name) (prop-coerce-to-val value))))

(defn read-properties
  "Read properties from file-able."
  [file-able]
  (with-open [f (io/input-stream file-able)]
    (doto (Properties.)
      (.load f))))

(defn load-properties!
  "load a set the current geva properties file, useful for testing fitness functions at the repl"
  [filename]
  (alter-var-root #'*geva-properties* (fn [_] (read-properties filename)))
  *geva-properties*
  )

(defn rand-double-seq []
  (repeatedly #(.nextDouble *geva-rng*)))

(defn rand-int-seq []
  (repeatedly #(.nextInt *geva-rng*)))

(defn rand-bool-seq []
  (repeatedly #(.nextBoolean *geva-rng*)))

(defn rand-char-seq []
  (repeatedly #(.nextChar *geva-rng*)))

(defn rand-short-seq []
  (repeatedly #(.nextShort *geva-rng*)))

(defn ifn-from-fitness-fn
  "Creates a wrapper that calls a java fitness function when invoked. Used to let clojure code call java fitness functions.
  This should be used with an identity create-ind function." 
  [#^geva.FitnessEvaluation.FitnessFunction java-fitness-fn]
    (fn [#^Individual ind]
      (.getFitness java-fitness-fn ind)
      (.. ind getFitness getDouble)       
    ))
 
(defn default-create-ind 
  "Default method of creating functions from GEVA geva.Individuals.
   Assumes that the grammer create valid clojure functions like:
  (fn [x] (+ x 1)) and that the fitness function accepts a function argument"
  [#^Individual ind]   
  (eval (read-string (.getPhenotypeString ind 0)))
  )

(defn set-ind-fitness! [#^GEIndividual ind #^ClojureFitnessEvaluationOperation$FitnessCache cache create-ind-fn fitness-fn]  
  (if (.isValid ind)    
      (let [fitness-val (fitness-fn (create-ind-fn ind))    
            fitness (.getFitness ind)]
        (.setDouble fitness (double fitness-val)))
      (let [#^BasicFitness fitness (.getFitness ind)]
        (.setDouble fitness (.getDefaultFitness fitness)))) 
  (.setEvaluated ind true)
  (when cache (.addFitnessToCache cache ind)))

(defn- needs-evaluation? [#^Individual ind fitness-cache] 
   (if (.isEvaluated ind)
     false
     (if (not fitness-cache) 
          true
          (do
            ;(when-not (.isMapped ind) (.map ind 0))
            (.map ind 0)              
            (not (.getFitnessFromCache fitness-cache ind))))))
           
(defn set-fitness-from-cache!
  "Set fitness values from the cache and return a lazy sequence of individuals with no cached value"  
  [#^java.util.List inds #^ClojureFitnessEvaluationOperation$FitnessCache fitness-cache]
      (lazy-seq 
        (when-let [[f & r] inds]
          (if (needs-evaluation? f fitness-cache)
            (cons f (set-fitness-from-cache! r fitness-cache))
            (set-fitness-from-cache! r fitness-cache))))                      
 )

(defn pset-group-fitness!
  "Evaluate a population in paralell, retrieving values from the cache if possible"
  [#^java.util.List inds fitness-cache create-ind-fn fitness-fn]  
  (dorun
   (pmap #(set-ind-fitness! % fitness-cache create-ind-fn fitness-fn)          
	 (set-fitness-from-cache! inds fitness-cache))))

                            
(defn set-group-fitness!
  "Evaluate a population, retrieving values from the cache if possible"
  [#^java.util.List inds fitness-cache create-ind-fn fitness-fn]  
  (doseq [ind (set-fitness-from-cache! inds fitness-cache)]
    (set-ind-fitness! ind fitness-cache create-ind-fn fitness-fn)))

(declare expand-grammar-map)

(defn- create-grammar-line [[sym val]]
  (if (map? val)
    (expand-grammar-map sym val)
    (str "<" (name sym) "> ::= "
	 (if (coll? val)           
	   (str/join "|" (map str val))
	   (.replaceAll (str val) "\n" " ")))))

(defn- concat-grammar-keys [parent child]
  (keyword (str (name parent) "-" (name child))))


(defn- expand-grammar-map
  "Expand grammar elements like :a {:b 1 :c {:d 2}} to [\"<a-b> ::= 1\" \"<a-c-d> ::= 2\").
   Returns a list of grammar rules"
  [parent-key m]
  (if (not (contains? m :root))
    (throw (RuntimeException. "All map grammar elements must have a :root element")))
	    
  (flatten (map (fn [[key val]]
		  (let [elem-key (if (= :root key) (name parent-key) (concat-grammar-keys parent-key key))]
		    (if (map? val)
		      (expand-grammar-map elem-key val)
		      (.replaceAll (create-grammar-line [elem-key val]) "<::" (str "<" (name parent-key) "-" ) ))))
		  m)))
	   
(defn- create-grammar-str 
  "Creates a GEGrammar from a list of :symbol \"expr\" pairs.
   eg. (create-grammar :word \"<letter>|<letter><word>\"
                       :letter \"a|b|c|..\")"
  [forms]
  (let [new-line (System/getProperty "line.separator")
	sb (StringBuilder.)]
    (doseq [l (flatten (map create-grammar-line (partition 2 forms)))]
      (.append sb l)
      (.append sb new-line))
    sb
    )
  )

(defn create-grammar [& forms]
  (let [grammar-str (create-grammar-str forms)]    
    (proxy [GEGrammar] [grammar-str]
      (toString [] (str grammar-str)))))


(defmacro defgrammar [name & schema]
  `(def ~name (create-grammar ~@schema)))

 (defn load-grammar [#^String filename]
  (GEGrammar. filename)
  )

 
(defn- set-properties! [#^java.util.Properties props & new-vals]
  (doseq [p (partition 2 new-vals)]
    (.setProperty props (prop-coerce-to-key (first p)) (prop-coerce-to-val (second p))))
  props)

(defn load-properties 
  "Load a java properties file and optionally set values specified as key value pairs
  eg. (load-properties \"test.properties\" \"prop-name1\" 23 \"prop-name2\" \"fred\""
  [filename & new-vals]
  (let [props (java.util.Properties.)]
    (.load props (java.io.FileInputStream. filename))
    (set-properties! props new-vals)
    ))

(defn set-properties 
  "Copies a set of properties and replaces keys specified as key/value pairs. 
   eg. (set-properties props \"my.key\" \"my-value\")"
  [#^java.util.Properties props & new-vals]
  (set-properties! (.clone props) new-vals))



(defn default-properties [& overrides]
  (apply set-properties!  (java.util.Properties.)	 
	 :mutation-probability 0.02
	 :initialiser geva.Operator.Initialiser
	 :generations 100
	 :replacement-type geva.Util.Constants/GENERATIONAL
	 :crossover-operation geva.Operator.Operations.SinglePointCrossover
	 :derivation-tree geva.Mapper.DerivationTree
	 :userpick-size 20
	 :grow-probability 0.5
	 :population-size 100
	 :output ""
	 :max-wraps 3
	 :selection-operation geva.Operator.Operations.TournamentSelect
	 :crossover-probability 0.9
	 :mutation-operation geva.Operator.Operations.IntFlipMutation
	 :max-depth 10
	 :elite-size 10
	 :tournament-size 3
	 :fixed-point-crossover true
	 :stopWhenSolved false
	 :initial-chromosome-size 200
	 :individual-catch-interval 2147483647
	 overrides))

(defn geva-run
  "Run a GEVA based search.
   If no grammar and fitnerss functions are provided they will be read from the properties file.
   The fitness function will be called for each individual in the population"
  ([#^java.util.Properties properties] (ClojureRun/run nil properties nil)) 
  ([#^GEGrammar grammar #^java.util.Properties properties fitness-fn] (ClojureRun/run grammar properties fitness-fn))) 

(defn geva-run-group
  "Run a GEVA based search specifying a group evaluation function.
   The group eval function will be called once for each generation with a List<Individual> and a fitness cache.
   See set-fitness-from-cache! to use cached values and return individuals whose fitness needs to be calculated."
  ([#^GEGrammar grammar #^java.util.Properties properties group-eval-fn]
     (ClojureRun/runWithGroupEval grammar properties group-eval-fn)))

(defn create-inds
  "Create an infinite sequence of individuals based on the specified grammar and properties.
   This is useful for validating grammars during development"
  [grammar properties]
  (let [ri (RandomInitialiser. *geva-rng* grammar properties)]
    (.setProperties grammar properties false)
    
    (repeatedly
     #(let[ind  (.createIndividual ri)]
	(.doOperation ri ind) ;; initialise the genotype to a random value
	(doto grammar
	  (.setPhenotype (.getPhenotype ind))
	  (.setGenotype (first (.getGenotype ind)))
	  (.genotype2Phenotype true))
	(.map ind 0)
	ind))))

;;;; functions to help with grammar production


(defn repeats
  "return a lazy sequence of strings with symbol repeated from min-times to max-times inclusive.
   A prefix and suffix for the pattern may be optionally specified.
   eg. (repeats \"<foo>\" 2 3) -> [\"<foo><foo>\" \"<foo><foo><foo>\"]
       (repeats \"(and\" \" <test>\" \")\" 2 3) -> [\"(and <test> <test>)\" \"(and <test> <test> <test>)\"] "
  ([symbol min-times max-times]
     (repeats "" symbol "" min-times max-times))
  ([prefix symbol suffix min-times max-times]
     (map #(str prefix (str/join (repeat % symbol)) suffix)
	  (range min-times (inc max-times)))))

(defmacro constant
  "A macro that evaluates all it's arguments.
   This is used to calculate constants once during model compilation and then re-use the value during fitness evaluations.
   Usage: (constant (+ 7 (* 2 5))) => 17"
  [form]
  (eval form))

(defmacro to-integer
  "Create an integer constant from a set of digits.
   Usage: (to-integer 1 2 3) -> 123"
  [& digits]
  (Integer/parseInt (apply str digits)))


(defmacro to-double
  "Create a double constant from a set of digits.
   Usage: (to-double 1 2 3 \".\" 7 8 9 ) -> 123.789"
  [& digits]
  (Double/parseDouble (apply str digits)))


(defn constant-int
  "Create a geva grammar rule for an integer in the range [min-max]"
  ([min max] (constant-int min max (- max min)))
  ([min max num-steps]
     (let [step-size (-> (- max min) (/ num-steps) Math/ceil int);  (int (Math/ceil (/ (- max min) num-steps)))
	   digits  (count (str num-steps))]
       {:root (format "(geva/constant (int (+ %d (* %d  (mod (geva/to-integer <::digits>) %d)))))" min step-size  (inc num-steps))
	:digits (str/join  (repeat digits "<::digit>")) ;; (str/join "|" (geva/repeats "<::digit>" 1 digits)) 
	:digit (range 10)})))
     
(defn constant-double
  "Create a geva grammar rule for a double in the range [min-max) with precision digits after the decimal place"
  [min max precision]
  {:pre [(zero? (rem (* 10(- max min)) 1))]} ;; doesn't currently work correctly when want more that 0.1 precision on the range, eg. (1.1  1.11) wont work propery
  (let [raw-range (- max min)
	range-rem (rem raw-range 1)
	const-range (if (> range-rem 0) (inc (long raw-range)) (long raw-range))
	digits  (count (str const-range))]
    {:root (format "(geva/constant (double (+ %f (mod (geva/to-integer <::int-const>) %d) (geva/to-double 0 \\\".\\\" <::first-frac-const>) (geva/to-double 0 \\\".0\\\" <::frac-const>))))"
		   (float min)
		   (clojure.core/max 1 (long const-range)))
     :int-const  (str/join "|" (geva/repeats "<::digit>" 1 digits))
     :first-frac-const (if (> range-rem 0) (take (int (* 10 range-rem)) (range 10)) "<::digit>")
     :frac-const (if (> precision 1) (geva/repeats "<::digit>" 1 (dec precision)) "0")
     :digit (range 10)}
    ))

(defn constant-double-step
  "Create a geva grammar rule for a double in the range [min-max] with steps + 1 discrete values.
   eg. (constant-double-step 0 1 10) is equivalent to (range 0 1 0.1), ie. has 11 discrete values"
  [min max steps]
  (let [step-size (/ (- max min) (double steps))
        int-rules (constant-int 0 steps steps)]
    (assoc (cs/rename-keys int-rules {:root :num-steps})
      :root (format "(geva/constant (+ %f (* %f <::num-steps>)))" (double min) step-size))))



;;;; ===================================================================================================
;; a couple of utility functions for use in grammars (since < and > are not legal characters
(defn nan? [x]
  (java.lang.Double/isNaN x))

(defn lt [x y]
  (if (or (nil? x) (nil? y) (nan? x) (nan? y))
    false
    (< x y)))

(defn gte [x y]
  (if (or (nil? x) (nil? y) (nan? x) (nan? y))
    false
    (>= x y)))



;; ====================================================================================================
;; helpers for testing grammars
(defn ind-to-str
  "return a string representation of an individuals phenotype"
  [ind]
  (if (instance? geva.Individuals.GEIndividual ind)
    (.. ^geva.Individuals.GEIndividual ind getPhenotype getString)
    (str ind)))

(defn- simplify-ind
  "simplify a model for printing, returns a string of the phenotype"
  [ind]
  (walk/prewalk (fn [x]
		  (if (and (seq? x) (= 'geva/constant (first x)))
		    (walk/macroexpand-all x)
		    x))
		(read-string (ind-to-str ind))))

(defn pprint-ind
  "Create a pretty printed version of ind as a string. All Geva constants in ind will be calculated"
  [ind]
  (pp/with-pprint-dispatch pp/code-dispatch
    (pp/pprint (simplify-ind ind))))

(defn pprint-ind-str
  "Create a pretty printed version of ind as a string. All Geva constants in ind will be calculated"
  [ind]
  (with-out-str
    (pprint-ind ind)))

(defn print-sample-inds 
  ([model-grammar] (print-sample-inds model-grammar 3))
  ([model-grammar n]
     (doseq [ind (take n (create-inds model-grammar (default-properties)))]
       (println)
       (println "----====== <Individual> =====----")
       (pprint-ind ind))))
