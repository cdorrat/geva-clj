(ns example1
  (:use [geva]))
  
; this is the function we're trying to discover
(defn target-fn [x]  
  (+ x (Math/pow x 2) (Math/pow x 3) (Math/pow x 4) (Math/pow x 5)))

; Fitness is the sum of the difference between our target value 
; and the candidate at 20 points over the range [-1, 1)
(defn fitness-fn [ind-fn]
  (reduce + (map #(Math/abs (- (ind-fn %) (target-fn %))) (range -1 1 0.1))))  

; we'll use the default properties with the population size set to 200
(def props (default-properties "population_size" 200))

(def grammar (create-grammar
 :code "(fn [x] <expr>)"
 :expr "(<op> <expr> <expr>) | <var>"
 :op   "+|-|*"
 :var "x|1.0"))
 
; run the evolution
(def stats 
  (geva-run grammar props fitness-fn))

; print some stats
(println "The GEVA run completed in " (:completion-time stats))
(pritnln "The best individual has a fitness of " (:best-fitness stats))

 