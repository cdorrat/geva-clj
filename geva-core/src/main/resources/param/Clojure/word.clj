(ns word-count)

(defn fitness-fn [word] 
  (let [to-match "geva";(geva/property "word")
	initial-fitness (max (.length word) (.length to-match))]
    (double (- initial-fitness (reduce + (map #(if (= %1 %2) 1 0) to-match word))))))