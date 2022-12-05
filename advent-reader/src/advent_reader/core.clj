(ns advent-reader.core)

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