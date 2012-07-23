(ns symbolic-regression)

; We're expecting a valid expression from the phenotype like "(+ x (* x x))"
; we'll just wrap the phenotype with a fn that expects an x argument 
(defmacro express-ind [expr]
  `(fn [~'x] ~expr))

; this is the function we're trying to discover
(defn target-fn [x] 
  (let [x0 (double x)] 
    (+ (+ (+ (+ x0 (* x0 x0)) (* x0 (* x0 x0))) (* (* x0 x0) (* x0 x0))) (* (* x0 x0) (* x0 (* x0 x0)))))
;  (+ x (* x x) (* x x x) (* x x x x) (* x x x x x))
)

; Fitness is the sum of the difference between our target value 
; and the candidate at 21 points over the range [-1, 1]
(defn fitness-fn [ind-fn]
  (reduce + (map #(Math/abs (- (ind-fn %) (target-fn %))) (range -1 1.01 0.1)))
)


(defn benchmark []
  (time (dotimes [_ 5000]
	  (let [test-fn (eval 
			 (read-string 
			  "(fn [x] (* (* 1.0 (+ (- 1.0 1.0) (- (* 1.0 (- (+ x x ) x )) (- x x )))) x))"))]
	    (fitness-fn test-fn)))))
		