(ns day-1.core)

(def input (atom {}))
(swap! input #(assoc %1 :file %2) (slurp "./resources/inputs.txt"))

(defn create-groups [v]
  (map #(clojure.string/split % #"\n") (clojure.string/split v #"\n\n"))
)

(defn max-calories [groups n-elves]
  (let [int-groups (map (fn [g] (map #(Integer/parseInt %) g)) groups)]
    (reduce + 0 (take-last n-elves (sort (map (fn [g] (reduce + 0 g)) int-groups)))))
  )

(max-calories (create-groups (:file @input)) 3)