(ns examples
  (:use [geva]))
  
; this is the function we're trying to discover
(defn target-fn [x]  
  (+ x (Math/pow x 2) (Math/pow x 3) (Math/pow x 4) (Math/pow x 5)))

; our fitness function, fitness is the sum of the difference between our target value 
; and the candidate at 21 points over the range [-1, 1]. 
(defn fitness-fn [ind-fn]
  (reduce + (map #(Math/abs (- (ind-fn %) (target-fn %))) (range -1 1 0.1))))  
  
;(def props (load-properties "../param/Parameters/SymbolicRegressionClojure.properties"))
;(def props (default-properties "rng_seed" 0))

; We're using the default properties and overriding the pop'ulation size
; To see what the defaults are type (default-properties) at the REPL   
(def props (default-properties "population_size" 200))

; our grammar, each production is a valid clojure function
(def grammar (create-grammar
 :code "(fn [x] <expr>)"
 :expr "(<op> <expr> <expr>) | <var>"
 :op   "+|-|*"
 :var "x|1.0"))
 
;; Run GEVA, the results are returned in a map
(defn run-example1 []
  (let [result  (geva-run grammar props fitness-fn)]
    (println "GEVA evaluation completed in " (:elapsed-ms result) "ms")
    (println "The best fitness was " (apply min (-> result :stats :best-fitness)))))


  
 

 