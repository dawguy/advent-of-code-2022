(ns day-7.core
  (:require [advent-reader.core :as adv]))

(def txt (slurp "./resources/data.txt"))
(def txt (slurp "./resources/sample.txt"))
(def data (adv/parse txt {:split-by-characters [" "]
                     :parse-vals true}))

(defn ls-files [m] "Should have the ($, ls) command pre-dropped. Returns a map of the contents of the directory"
  (loop [cursor (:cursor m)
         [c & rem-c] (rest (:commands m))]
    ;(prn "ls ---- start")
    (if (or (= "$" (first c)) (nil? c))
      (do
          ;(prn "ls return")
          ;(prn (-> m (assoc :cursor cursor) (assoc :commands (cons c rem-c))))
          (-> m
              (assoc :cursor cursor)
              (assoc :commands (if (nil? c) (seq [])
                                            (cons c rem-c)))))
      (recur (assoc cursor (second c) {:val  (first c)
                                       :name (second c)})
             rem-c))))
(declare process)
(defn cd [m]
  (loop [commands (:commands m)
         cursor (:cursor m)]
    (prn "CD ---- start")
    (prn cursor)
    (if (or (= (nth (first commands) 2) "..") (empty? commands))
      (do
          (prn "CD return")
          (prn cursor)
          {:commands commands
           :cursor   cursor})
      (let [resp (process {:commands (rest commands)
                           :cursor {}})]
        (recur (:commands resp)
               (assoc (:cursor m) (nth (first commands) 2) (:cursor resp)))
      ))))

(defn process [data]
  (loop [m data]
    ;(prn "Process")
    ;(prn m)
    (let [commands (:commands m)
          cursor (:cursor m)
          command (first commands)]
      ;(prn (str "PROCESS" command (not (= (first command) "$"))))
      (if (empty? commands)
        {:commands (seq [])
         :cursor   cursor}
        (if (not (= (first command) "$"))
          (recur (assoc m :commands (rest commands)))       ; Generally shouldn't be hit ever, as LS should take care of non-commands.
          (cond                                             ; is a command
            (and (= (second command) "cd") (= (nth command 2) "..")) {:commands (rest commands)
                                                                      :cursor cursor}
            (= (second command) "cd") (recur (cd m))
            (= (second command) "ls") (recur (ls-files m))
            :default (recur (assoc m :commands (rest commands)))       ; Generally shouldn't be hit ever, as LS should take care of non-commands.
            ))))))

; Basic ideas here
; cd "dir"-> adds current cursor to parents list and starts to process remaining commands.
; ls -> adds next set of files to current cursor
; cd ".." -> adds the cursor to the parent's children, pops the current parent off

; How hard would it be to treat cd "dir" as creating a new vec which has the first element as the name "", then every following element be the commands
; and cd ".." as ending the current vec

; Change of strat.
; parse-command needs to be inside process
; process needs to handle the ".." vs "dir" case of cd.
; all commands need to know nothing about their parents and return a list of the cursor and all remaining commands (used by the next iteration of process's loop recur)


(comment "Helper funcs for debugging"
         (def m {:cursor   {}
                 :commands (drop 1 commands)})
         (ls-files m)
         (def m {:cursor {}
                 :commands data})
         (def command (seq ["$" "cd" "a"]))
         (def commands (take 6 data))
         (process {:cursor {}
                   :commands commands})
         (def real-m m)
         (def sample-m m)
         (process m)
         (process real-m)
         (process sample-m)
         (def cursor {".." {:cursors [{:a 1}]}
                      :name "123"
                      :files [{:b 1} {:b 2}]})
         (process data)
         ,)