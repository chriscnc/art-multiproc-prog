(ns dining-philosophers.core
  (:require [clojure.tools.logging :as log])
  (:import [java.util.concurrent Executors])
  (:gen-class))

(def p-count 10)
(def max-eating-time 20)

(def forks (into [] (take p-count (repeatedly #(ref :avail)))))
(def eating-time (into [] (take p-count (repeatedly #(atom 0)))))
(def thinking-time (into [] (take p-count (repeatedly #(atom 0)))))


(defn think
  "Think for 1 to 3 seconds"
  [pid]
  (let [secs (inc (rand-int 3))]
    (swap! (nth thinking-time pid) #(+ % secs))
    (Thread/sleep (* 1000 secs))))


(defn eat
  "Eat for a 1 to 3 seconds"
  [pid]
  (let [secs (inc (rand-int 3))]
    (swap! (nth eating-time pid) #(+ % secs))
    (Thread/sleep (* 1000 secs))))


(defn pickup-forks!
  "In a transaction, see if both forks are available and if so,
  pick them up. Returns true if successfull, false otherwise."
  [left-fork right-fork]
  (dosync 
    (if (and (= :avail @left-fork)
             (= :avail @right-fork))
      (do (ref-set left-fork :in-use)
          (ref-set right-fork :in-use)
          true)
      false)))


(defn putdown-forks!
  "Just like the function name says..."
  [left-fork right-fork]
  (dosync (ref-set left-fork :avail)
          (ref-set right-fork :avail)))


(defn make-philosopher
  "Constructs a philospher function."
  [pid]
  (log/info "Philospher:" pid "sits...")
  (fn [] 
    (let [left-fork (nth forks pid)
          right-fork (nth forks (mod (inc pid) p-count))]
      (loop []
        (think pid)
        (let [able-to-eat? (pickup-forks! left-fork right-fork)]
          (when able-to-eat?
            (eat pid)
            (putdown-forks! left-fork right-fork)))

        (if (< @(nth eating-time pid) max-eating-time)
          (recur)
          :full)))))


(defn wait-tables []
  "Monitors the philosphers while they are eating"
  (loop []
    (log/info (str "Time eating:   " (into [] (map deref eating-time))))
    (Thread/sleep 2000)
    (recur)))


(defn -main
  [& args]
  (log/info "Setting the table...")
  (log/info "Seating the philosophers...")
  (let [philosophers (into [] (map make-philosopher (range p-count)))
        waiter (future-call wait-tables)
        _ (log/info "Commence thinking and eating...")
        eating-philosophers (into [] (map #(future-call %) philosophers))]

    ; wait for all the philosphers to get full
    (dorun (map #(deref %) eating-philosophers))

    (doseq [pid (range p-count)]
      (log/infof "Philospher %d - ate for: %d secs, and thought for: %d secs" 
                 pid @(nth eating-time pid) @(nth thinking-time pid)))

    (future-cancel waiter)
    (log/info "Busing the table...")
    (shutdown-agents)))
  

