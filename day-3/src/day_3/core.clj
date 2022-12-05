(ns day-3.core (:require [advent-reader.core :as adv]))

(def txt (slurp "./resources/data.txt"))
(def data (adv/parse txt {}))
(def line (first data))

(defn to-set [v]
  (set v)
)
(defn combine-rucksacks
  ([sets]
   (combine-rucksacks (drop 3 sets) (vec [(apply clojure.set/intersection (take 3 sets))])))
  ([sets l]
   (if (empty? sets)
     l
     (combine-rucksacks (drop 3 sets) (conj l (apply clojure.set/intersection (take 3 sets)))))))
(defn to-priority [s]
  (let [v (int (first s))]
    (if (< 96 v)                                            ; \A 65, \a 97
      (- v 96)                                              ; (lower-case - 96) = [1,26]
      (- v 38))))                                           ; (upper-case - 38) = [27,52]

;(int \a)
;(int \A)
;(- (int \a) 96)
;(- (int \z) 96)
;(- (int \A) 38)
;(- (int \Z) 38)

(defn calc [txt]
  (reduce + 0
          (->>
            (adv/parse txt {})
            (map to-set)
            (combine-rucksacks)
            (map to-priority)
            )))
