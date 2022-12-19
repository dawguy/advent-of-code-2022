(ns day-8.core
  (:require [advent-reader.core :as adv]))

(def sample-txt (slurp "./resources/sample.txt"))
(def txt (slurp "./resources/data.txt"))
(defmacro vmap [c]
  `(vec (map vec ~c))
)

(def sample-data (vmap (adv/parse sample-txt {:split-by-characters [""]
                                                      :parse-vals          true})))
(def data (vmap (adv/parse txt {:split-by-characters [""]
                                        :parse-vals          true})))

(defn invert [grid] (vmap (partition (count grid) (apply interleave grid))))

(defn evaluate-row [row]
  (loop [m -1
         [v & rem-vals] row
         ret []]
    (if (nil? v)
      ret
      (recur (max m v) rem-vals (conj ret (< m v))))
    ))
(defn evaluate-grid [grid]
  (let [inverted-grid (invert grid)]
    {
     :r (vec (map evaluate-row grid))
     :l (vmap (map reverse (map evaluate-row (vmap (map reverse grid)))))
     :u (vec (invert (vmap (map reverse (map evaluate-row (vmap (map reverse inverted-grid)))))))
     :d (vec (invert (map evaluate-row inverted-grid)))
     }))
(defn count-trues [evaluated-grid]
  (let [counter (atom 0)
        n (inc (count (:r evaluated-grid)))]
    (doseq [x (range n)
            y (range n)]
      (let [v (or (get (get (:r evaluated-grid) y) x)
                  (get (get (:l evaluated-grid) y) x)
                  (get (get (:u evaluated-grid) y) x)
                  (get (get (:d evaluated-grid) y) x)
                  )]
        (if v
          (swap! counter inc)
          ))) counter))

(comment ""
         sample-data
         data
         (def grid sample-data)
         (evaluate-row [3 2 6 3 3])
         (evaluate-row [3 7 3 0 3])
         (evaluate-grid sample-data)
         (def evaluated-grid (evaluate-grid sample-data))
         (get (get (:r evaluated-grid) 1) 0)
         (get evaluated-grid 1)
         (def evaluated-grid (evaluate-grid data))
         (deref (count-trues (evaluate-grid data)))
         ,)

