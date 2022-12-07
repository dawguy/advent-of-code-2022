(ns day-6.core)

(def txt (slurp "./resources/data.txt"))

(defn ready? [buf-map c]
  (not (contains? buf-map c)))
(defn update-buf-map [buf-map buffer c]
  (let [drop-c (first buffer)]
    (-> buf-map
        (assoc drop-c (drop 1 (get buf-map drop-c)))
        (assoc c (concat (get buf-map c) c)))))
(defn buf-take [m s])

(defn slide [buffer c]
  (concat (rest buffer) c))

(take 1 [1 2 3])

(defn compute [txt buf-size]
  (loop [i buf-size
         buffer (take buf-size)
         buf-map {}
         s (drop buf-size)]
    (let [c (first s)]
      (if (or (empty? s) (ready? buf-map c))
        i
        (recur (inc i)
               (slide buffer c)
               (update-buf-map buf-map buffer (rest s)))
        ))
    ))

(def a "bvwbjplbgvbhsr")
(def buffer (take 4 (seq a)))
(def buf-map {
              "b" (seq [0 3])
              "v" (seq [1])
              "w" (seq [2])
              })
(slide buffer "j")
(ready? buf-map "z")