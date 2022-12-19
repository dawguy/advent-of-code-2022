(ns day-7.core
  (:require [advent-reader.core :as adv]))

(def txt (slurp "./resources/data.txt"))
(def txt (slurp "./resources/sample.txt"))
(def data (adv/parse txt {:split-by-characters [" "]
                     :parse-vals true}))

; Copied from assoc-in, but added a summation portion as well.
(defn assoc-and-sum [m [k & ks] v]
  (assoc (if ks
           (assoc m k (assoc-and-sum (get m k) ks v))
           (assoc m k (conj (get m k []) v)))
    :sum
    (+ (get m :sum 0) (:val v))))
(defn add-field [vals levels c]
  (if (= (first c) "dir")
    vals
    (assoc-and-sum vals (conj levels :files) {:name (second c)
                                              :val  (first c)})))
(defn gen-dirs [data]
  (loop [[c & rem-c] data
         levels []
         vals {}]
      (cond (nil? c) vals
            (not (= (first c) "$"))
              (recur rem-c levels (add-field vals levels c))
            (and (= (second c) "cd") (= (nth c 2) ".."))
              (recur rem-c (vec (drop-last levels)) vals)
            (= (second c) "cd")
              (recur rem-c (conj levels (nth c 2)) vals)
            :else (recur rem-c levels vals)
            )))
(defn calc [data]
  (let [dirs (gen-dirs data)]
    (loop [ret []
           stack [dirs]]
      (let [s (first stack)]
        (if (nil? s)
          ret
          (if (< (:sum s) 100000)
            (recur (conj ret (:sum s)) (concat (rest stack) (map second (dissoc (dissoc s :files) :sum))))
            (recur ret (concat (rest stack) (map second (dissoc (dissoc s :files) :sum))))
            ))))))
(defn get-sizes [data]
  (sort (calc data)))

(comment "Dev calls"
         (def data-real data)
         (def data-sample data)
         (add-field {:a {:b 1}} [:a] [1234 "abc"])
         (add-field {:a {:b 1}} [:a] ["dir" "abc"])
         (def dirs (gen-dirs data-sample))
         (def dirs (gen-dirs data-real))
         (def stack [dirs])
         (def s (first stack))
         (reduce + 0 (calc data-real))
         (calc data-sample)
         (def clean-s (dissoc (dissoc s :files) :sum))
         (def levels ["/" "e"])
         (def c (seq ["$" "cd" "e"]))
         (vec (drop 1 [:a :b :c]))
         ,)

