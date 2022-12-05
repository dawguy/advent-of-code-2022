(ns day-3.core (:require [advent-reader.core :as adv]))

(def txt (slurp "./resources/data.txt"))
(def data (adv/parse txt {}))
(def line (first data))

(defn split-container [line]
  [(subs line 0 (/ (count line) 2)) (subs line (/ (count line) 2))])
(defn to-set [[left right]]
  [(set left) (set right)]
)
(defn combine-rucksacks [[left right]] (clojure.set/intersection left right))
(defn to-priority [s]
  (let [v (int (first s))]
    (if (< 96 v)                                            ; \A 65, \a 97
      (- v 96)                                              ; (lower-case - 96) = [1,26]
      (- v 38))))                                           ; (upper-case - 38) = [27,52]

;(- (int \a) 96)
;(- (int \z) 96)
;(- (int \A) 38)
;(- (int \Z) 38)
;
;(int \a)
;(int \A)

(defn calc [txt]
  (reduce + 0
          (->>
            (adv/parse txt {})
            (map split-container)
            (map to-set)
            (map combine-rucksacks)
            (map to-priority)
            ))
  )
