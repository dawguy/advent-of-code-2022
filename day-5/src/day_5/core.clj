(ns day-5.core
  (:require [advent-reader.core :as adv]))
(comment ""
         (def line (first (adv/parse txt {})))
         ,)

(def txt (slurp "./resources/data.txt"))
(def moves (->> (adv/parse txt {:split-by-character " "})
                (drop 10)
                (map (fn [l] [(Integer/parseInt (get l 1))
                              (Integer/parseInt (get l 3))
                              (Integer/parseInt (get l 5))]))
                ))

; Seems like each crate is 3 spaces wide with 1 space as a gap
; Turning into a constant so I don't accidentally crash my computer with a misplaced cursor :')
; (take 9 (range 1 (* 9 4) 4))
(defn line-to-crates [line] (map #(if (= " " %) nil %) (map #(subs line % (+ % 1)) [1 5 9 13 17 21 25 29 33])))
(def crates (->> (take 8 (adv/parse txt {}))
                 (map line-to-crates)
                 (apply interleave)
                 (partition 8)
                 (map #(drop-while nil? %))
                 (zipmap (interleave (range 1 10)))
                 ))

