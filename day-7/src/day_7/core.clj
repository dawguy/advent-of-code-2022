(ns day-7.core
  (:require [advent-reader.core :as adv]))

(def txt (slurp "./resources/data.txt"))
(def txt (slurp "./resources/sample.txt"))
(def data (adv/parse txt {:split-by-characters [" "]
                     :parse-vals true}))

(defn ls [commands cursor] "Should have the ($, ls) command pre-dropped. Returns a map of the contents of the directory"
  ;(prn (first commands))
  (assoc cursor :files
                (loop [t {}
                       [c & rem-c] (rest commands)]
                  ;(prn c)
                  (if (or (= "$" (first c)) (nil? c))
                    t
                    (recur (assoc t (second c) {:val  (first c)
                                                :name (second c)})
                           rem-c)))))
(defn cd [commands cursor]
    (if (= ".." (second (first commands)))
      (let [parent (get-in cursor [".."])]
        (assoc parent :cursors (conj (:cursors parent) cursor)))
      {".."  cursor
       :name (nth (first commands) 2)}))
(defn parse-command [command]
  (if (not (= (first command) "$"))
    #(identity %2)
    (cond                                                   ; is a command
      (= (second command) "ls") ls
      (= (second command) "cd") cd
      :default #(identity %2)
      )))
(defn process [data]
  (loop [commands data
         cursor {}]
    (if (empty? commands)
      cursor
      (recur (rest commands)
             ((parse-command (first commands)) commands cursor)))))

(comment "Helper funcs for debugging"
         (ls sample-ls-data {".." nil
                             :name "/"})
         (def command (seq ["$" "cd" "a"]))
         (def commands data)
         (def commands (take 9 data))
         (process commands)
         (def cursor {".." {:cursors [{:a 1}]}
                      :name "123"
                      :files [{:b 1} {:b 2}]})
         (process data)
         ,)