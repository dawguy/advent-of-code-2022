(ns advent-reader.core)

(def day-1 (slurp "./resources/day-1.txt"))
(def day-2 (slurp "./resources/day-2.txt"))
(def txt day-2)
(def opts {:parse-vals false
           :group-by-empty-line false
           :split-by-character nil})

(defn parse-lines [lines apply-fn?]
  (if (not apply-fn?)
    lines
    (map #(or (try
                (Integer/parseInt %)
                (catch NumberFormatException e nil))
              (try
                (Double/parseDouble %)
                (catch NumberFormatException e nil))
              %)
         lines)))

(defn group-lines [lines apply-fn?]
  (if (not apply-fn?)
    lines
    (filterv #(not (= "" (first %))) (partition-by #(not (= "" %)) lines))))

(defn split-lines [lines split-by-character]
  (if (nil? split-by-character)
    lines
    (let [regex (re-pattern split-by-character)]
      (map #(clojure.string/split % regex) lines))))

(defn parse [txt opts]
  (let [{:keys [parse-vals group-by-empty-line split-by-character]}
        (merge {
                :parse-vals false          ; When input is a int/double
                :group-by-empty-line false ; Creates additional groupings per empty line
                :split-by-character nil
                } opts)]
    (-> txt
        (clojure.string/split #"\n")
        (parse-lines parse-vals)
        (group-lines group-by-empty-line)
        (split-lines split-by-character)
    )))