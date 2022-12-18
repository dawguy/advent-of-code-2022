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
(defn calc [data]
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

(comment "Dev calls"
         (def data-real data)
         (def data-sample data)
         (add-field {:a {:b 1}} [:a] [1234 "abc"])
         (add-field {:a {:b 1}} [:a] ["dir" "abc"])
         (calc data-sample)
         (calc data-real)
         (def levels ["/" "e"])
         (def c (seq ["$" "cd" "e"]))
         (vec (drop 1 [:a :b :c]))
         (prn)
         ,)

