(ns day-4.core
  (:require [advent-reader.core :as adv]))

(comment ""
         (def re (re-pattern "-"))
         ,)

(defn pi [[a b]] [(Integer/parseInt a) (Integer/parseInt b)])

(def txt (slurp "./resources/data.txt"))
(def data (let [re (re-pattern "-")]
            (->> (adv/parse txt {:split-by-character ","})
                 (map (fn [[a b]] [(clojure.string/split a re) (clojure.string/split b re)]))
                 (map (fn [[a b]] [(pi a) (pi b)])))
            ))

(defn in-range [l r v]
  (and (<= v r) (>= v l)))
(defn complete-overlap [[[aL aR] [bL bR]]]
  (or
    (and (in-range aL aR bL) (in-range aL aR bR))
    (and (in-range bL bR aL) (in-range bL bR aR))
  ))
(defn partial-overlap [[[aL aR] [bL bR]]]
  (or
    (or (in-range aL aR bL) (in-range aL aR bR))
    (or (in-range bL bR aL) (in-range bL bR aR))
    ))

(defn compute-complete-overlap [data] (reduce #(+ (if %2 1 0) %1) 0 (map complete-overlap data)))
(defn compute-partial-overlap [data] (reduce #(+ (if %2 1 0) %1) 0 (map partial-overlap data)))