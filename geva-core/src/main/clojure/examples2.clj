(ns examples2
  (:use [geva]
	[incanter core stats charts]))
    
; Run geva 50 times and calculate the average number of generations
; required to find a solution. 
; Assumes that the properties contains stopWhenSolved=true
(defn mean-gen-for-pop-size [props pop-size]
  (let [new-props (set-properties props "population_size" pop-size)
	result-seq (map #(count (-> % :stats :best-fitness))
			(take 50 (repeatedly #(geva-run new-props))))]
    (assoc {} :pop-size pop-size :mean-gen (mean result-seq))))

; Plot the mean number of generations required to find a solution 
; over a series of series of population sizes
(defn plot-gen-sizes [props]
  (let [pop-sizes [10 25 50 100 150 200 250 500]
	data (map #(mean-gen-for-pop-size props %) pop-sizes)]
    (view (xy-plot (map :pop-size data) (map :mean-gen data) 
		   :title "HelloWorld mean soluton gen" 
		   :x-label "pop size" :y-label "mean gen to solution"))))

;; load the HelloWorld properties file and override the grammar location then plot data

(defn  run-example2 []
  (plot-gen-sizes (load-properties  "src/main/resources/param/Parameters/HelloWorld.properties" 
				    "grammar_file" "src/main/resources/param/Grammar/letter_grammar.bnf")))