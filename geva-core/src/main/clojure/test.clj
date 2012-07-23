(ns example1
   (:require [geva :as g]))
  
; this is the function we're trying to discover
(defn target-fn [x]  
  (+ x (Math/pow x 2) (Math/pow x 3) ))

; our fitness function, fitness is the sum of the difference between our target value 
; and the candidate at 21 points over the range [-1, 1]. 
(defn fitness-fn [ind-fn]
  (reduce + (map #(Math/abs (- (ind-fn %) (target-fn %))) 
              (take 21 (map #(- (* 2 %) 1) (g/rand-double-seq))))))

; We're using the default properties and overriding the population size
; To see what the defaults are type (default-properties) at the REPL   
(def props (g/default-properties "population_size" 500))

; our grammar, each production is a valid clojure function
(def grammar (g/create-grammar
 :code "(fn [x] <expr>)"
 :expr "(<op> <expr> <expr>) | <var> | (<op> <expr> <expr> <expr>)"
 :op   "+|-|*"
 :var "x|1.0"))

(g/defgrammar test-grammar
    :code "(fn [x] <expr>)"
    :expr "(<op> <expr> <expr>) | <var> | (<op> <expr> <expr> <expr>)"
    :op   "+|-|*"
    :var "x|1.0")

 ; Run GEVA, the results are returned in a map
(defn run-test []
  (let [result (g/geva-run grammar props fitness-fn)]
    (println "GEVA evaluation completed in " (:elapsed-ms result) "ms")
    (println "The best fitness was " (apply min (-> result :stats :best-fitness)))
    result))


