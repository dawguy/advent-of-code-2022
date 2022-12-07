(ns day-6.core)

(def txt (slurp "./resources/data.txt"))

(defn ready? [buffer c] (and
                          (= (count (conj (set (rest buffer)) c))
                             (count buffer))))
(defn slide [buffer c] (concat (rest buffer) [c]))

(defn compute [n txt]
  (loop [buffer (take n (seq txt))
         s (drop n txt)
         i n]
    (if (ready? buffer (first s))
      (inc i)
      (recur (slide buffer (first s)) (rest s) (inc i)))
    ))

(compute 4 txt)
(compute 14 txt)

(comment ""
         (def txt "bvwbjplbgvbhsrlpgdmjqwftvncz")
         (def txt "nppdvjthqldpwncqszvftbrmjlhg")
         (def txt "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")
         (def txt "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")
         ,)