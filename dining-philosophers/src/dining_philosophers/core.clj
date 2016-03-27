(ns dining-philosophers.core
  (:require [clojure.tools.logging :as log])
  (:import [java.util.concurrent Executors])
  (:gen-class))

(def p-count 5)

(def forks (into [] (repeat p-count (ref false))))

;phil -> r l forks
;   0 -> 0 1 
;   1 -> 1 2
;   2 -> 2 3
;   3 -> 3 4
;   4 -> 4 0

(defn do-activity
  [id activity]
  (let [secs (inc (rand-int 3))
        secs 3]
  (log/info "philospher" id "-" activity "for" secs "seconds...")
  (Thread/sleep (* 1000 secs))))


(defn think
  [pid]
  (do-activity pid "thinking"))


(defn eat
  [pid]
  (do-activity pid "eating"))


(defn make-philosopher
  [pid]
  (fn [] (let [left-fork (nth forks pid)
               right-fork (nth forks (mod (inc pid) p-count))]
           (loop []
             (log/info (map deref forks))
             (while (not (compare-and-set! left-fork false true))
               (think pid))
             (log/info (map deref forks))
             (while (not (compare-and-set! right-fork false true))
               (think pid))

             (log/info (map deref forks))
             (eat pid)

             (reset! left-fork false)
             (reset! right-fork false)
             (log/info (map deref forks))
             (recur)))))


(defn -main
  [& args]
  (log/info "Setting the table...")
  (let [pool (Executors/newFixedThreadPool p-count)
        tasks (map make-philosopher (range p-count))]
    (log/info "Seating the philosophers...")
    (doseq [future (.invokeAll pool tasks)]
      (.get future))
    (log/info "Pulling the table-cloth...")
    (.shutdown pool)))

