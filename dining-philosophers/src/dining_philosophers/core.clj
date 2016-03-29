(ns dining-philosophers.core
  (:require [clojure.tools.logging :as log])
  (:import [java.util.concurrent Executors])
  (:gen-class))

(def p-count 5)
(def max-eating-time 3)
(def max-thinking-time 3)

; forks are either :avail or :in-use
; the more obvious...
; (def forks (into [] (repeat p-count (ref :avail))))
; doesn't create unique references for some reason
(def forks (for [_ (range p-count)] (ref :avail)))
(def eating-time (for [_ (range p-count)] (atom 0)))
(def thinking-time (for [_ (range p-count)] (atom 0)))

;phil -> r l forks
;   0 -> 0 1 
;   1 -> 1 2
;   2 -> 2 3
;   3 -> 3 4
;   4 -> 4 0

(defn think
  [pid]
  (let [secs (inc (rand-int max-thinking-time))]
    (swap! (nth thinking-time pid) #(+ % secs))
    (Thread/sleep (* 100 secs))))


(defn eat
  [pid]
  (let [secs (inc (rand-int max-eating-time))]
    (swap! (nth eating-time pid) #(+ % secs))
    (Thread/sleep (* 100 secs))))


(defn pickup-forks!
  [left-fork right-fork]
  (dosync 
    (if (and (= :avail @left-fork)
             (= :avail @right-fork))
      (do (ref-set left-fork :in-use)
          (ref-set right-fork :in-use)
          true)
      false)))


(defn putdown-forks!
  [left-fork right-fork]
  (dosync (ref-set left-fork :avail)
          (ref-set right-fork :avail)))


(defn make-philosopher
  [pid]
  (log/info "Philospher:" pid "enters.")
  (fn [] 
    (let [left-fork (nth forks pid)
          right-fork (nth forks (mod (inc pid) p-count))]
      (loop []
        (think pid)
        (let [able-to-eat? (pickup-forks! left-fork right-fork)]
          (when able-to-eat?
            (eat pid)
            (putdown-forks! left-fork right-fork)))
        (recur)))))


(defn monitor-philosophers []
  (loop [times-left 10]
    (when (> times-left 0)
      (log/info (str "  Eating times: " (into [] (map deref eating-time))))
      (log/info (str "Thinking times: " (into [] (map deref thinking-time))))
      (Thread/sleep 1000)
      (recur (dec times-left)))))


;; replace the monitor with some kind of eating count down for each philosopher
;; where the philosopher gets up and leaves when they have eaten enough
(defn -main
  [& args]
  (log/info "Setting the table...")
  (let [tasks (map make-philosopher (range p-count))]
    (log/info "Seating the philosophers...")
    (dorun (map (fn [f] (future (f))) tasks))
    (log/info "Starting monitor...")
    @(future (monitor-philosophers))
    (log/info "Pulling the table-cloth...")
    (shutdown-agents)))
  

