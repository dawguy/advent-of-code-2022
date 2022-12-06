(ns day-5.core
  (:require [advent-reader.core :as adv]))
(comment ""
         (def line (first (adv/parse txt {})))
         ,)
(def n 9)
(def max-initial-height 8)
(def txt (slurp "./resources/data.txt"))
(def n 3)
(def max-initial-height 3)
(def txt (slurp "./resources/simple-case-data.txt"))
(def moves (->> (adv/parse txt {:split-by-character " "})
                (drop (+ 2 max-initial-height))             ; This might be wrong, as its height dependent not columns.
                (map (fn [l] [(Integer/parseInt (get l 1))
                              (Integer/parseInt (get l 3))
                              (Integer/parseInt (get l 5))]))
                ))

; Seems like each crate is 3 spaces wide with 1 space as a gap
; Turning into a constant so I don't accidentally crash my computer with a misplaced cursor :')
; (take n (range 1 (* n 4) 4))
(defn line-to-crates [n]
  (fn [line]
    (map #(if (= " " %) nil %)
         (map #(if (< % (count line))
                  (subs line % (+ % 1))
                  nil
                  ) (take n (range 1 (* n 4) 4))))
    ))
(defn crates [n max-initial-height] "Generates the crates, with n being the highest number shown on the crates"
  (->> (adv/parse txt {})
       (take max-initial-height)
       (map (line-to-crates n))
       (apply interleave)
       (partition max-initial-height)
       (map #(drop-while nil? %))
       (zipmap (interleave (range 1 (inc n))))
       ))

(defn move-crates-step [crates qty from to]
  (let [moving-crates (take qty from)
        from-crates (get crates from)
        to-crates (get crates to)]
    (-> crates
        (assoc from (drop qty from-crates))
        (assoc to (concat (take qty from-crates) to-crates)) ; Part 2 answer
        ; (assoc to (apply conj to-crates (take qty from-crates))) ; Part 1 answer
        )))

; Useful seq joining tips
;(flatten (conj (seq [1 2 3]) (seq [4 5])))
;(concat (seq [4 5]) (seq [1 2 3]))
;(apply conj (seq [1 2 3]) (seq [4 5]))

(defn move-crates [crates moves]
  (loop [c crates
         [[qty from to] & rem] moves]
    ;(clojure.pprint/write (into (sorted-map) c))
    ;(prn (str "qty: " qty ", from: " from ", to:" to))
    ;(prn "\n")
    (if (nil? qty)
      c
      (recur (move-crates-step c qty from to) rem))))

; conj works exactly how we want for seq, but not vecs
;(apply conj (seq [1 2 3 4]) [5 6])

(comment ""
         (into (sorted-map) (move-crates (crates n max-initial-height) (take 0 moves)))
         (into (sorted-map) (move-crates (crates n max-initial-height) (take 1 moves)))
         (into (sorted-map) (move-crates (crates n max-initial-height) (take 2 moves)))
         (into (sorted-map) (move-crates (crates n max-initial-height) (take 3 moves)))
         (into (sorted-map) (move-crates (crates n max-initial-height) (take 4 moves)))
         (into (sorted-map) (move-crates (crates n max-initial-height) moves))
         ,)

(defn compute [crates moves] (clojure.string/join (map first (vals (into (sorted-map) (move-crates crates moves))))))

(compute (crates n max-initial-height) moves)
