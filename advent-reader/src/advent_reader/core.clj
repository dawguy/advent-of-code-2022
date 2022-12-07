(ns advent-reader.core)

(defn parse-lines [lines apply-fn?]
  (if (not apply-fn?)
    lines
    (if (sequential? lines)
      (map #(#'parse-lines % apply-fn?) lines)
      (or (try
            (Integer/parseInt lines)
            (catch NumberFormatException e nil))
          (try
            (Double/parseDouble lines)
            (catch NumberFormatException e nil))
          lines))))
(defn group-lines [lines apply-fn?]
  (if (not apply-fn?)
    lines
    (filterv #(not (= (ffirst %) "")) (partition-by #(= (first %) "") lines))))
(defn split-lines [lines split-by-characters]
  (if (empty? split-by-characters)
    lines
    (let [regex (re-pattern (first split-by-characters))]
      (map #(identity (split-lines (clojure.string/split % regex)
                                  (rest split-by-characters)))
           lines))))
(defn parse [txt opts]
  (let [{:keys [parse-vals group-by-empty-line split-by-characters]}
        (merge {
                :parse-vals false          ; When input is a int/double
                :group-by-empty-line false ; Creates additional groupings per empty line
                :split-by-characters []
                } opts)]
    (-> txt
        (clojure.string/split #"\n")
        (split-lines split-by-characters)
        (parse-lines parse-vals)
        (group-lines group-by-empty-line)
    )))

(comment "Helper defs for development"
         (def lines ["abc def ghj" "abc def-ghj hik"])
         (def split-by-characters [" " "-"])
         (def split-by-characters ["-" " "])
         (def txt (slurp "./resources/advent-reader.txt"))
         (def opts {:parse-vals          true
                    :split-by-characters [" "]
                    :group-by-empty-line false})
         (parse txt opts)
         (def txt (slurp "./resources/advent-reader-2.txt"))
         (def opts {:parse-vals          true
                    :split-by-characters ["," "-"]
                    :group-by-empty-line false})
         (parse txt opts)
         (def lines [["1" "2" "3"] ["3" "4" "5"] ["6" "-" "9"] ["9" "9" "9"]])
         (def apply-fn? true)
         ,)