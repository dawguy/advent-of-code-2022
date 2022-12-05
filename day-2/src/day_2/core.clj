(ns day-2.core
  (:require [advent-reader.core :as adv]))

(def txt (slurp "./resources/data.txt"))

; A, X - Rock
; B, Y - Paper
; C, Z - Scissors
(def scoring-p-1 {
              "A" {"X" 3 "Y" 6 "Z" 0}
              "B" {"X" 0 "Y" 3 "Z" 6}
              "C" {"X" 6 "Y" 0 "Z" 3}
})
(def scoring-p-2 {
                  "A" {"X" 3 "Y" 4 "Z" 8}
                  "B" {"X" 1 "Y" 5 "Z" 9}
                  "C" {"X" 2 "Y" 6 "Z" 7}
                  })
(defn points-for-selection-p1 [_ b] (get {"X" 1 "Y" 2 "Z" 3} b))
(defn points-for-selection-p2 [_ _] 0)

(defn scoring [] scoring-p-1)
(defn points-for-selection [a b] (points-for-selection-p1 a b))

(defn points-per-round [[a b]] (+ (#'points-for-selection a b) (get-in (#'scoring) [a b])))

(defn calc [txt] (let [data (adv/parse txt {:split-by-character " "})
                       points (map points-per-round data)]
                   (reduce + 0 points)
              ))