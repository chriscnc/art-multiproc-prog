(ns clj-mutex.lock-two-core
  (:require [clj-mutex.counters :refer [make-safe-counter]]
            [clj-mutex.locks :refer [make-lock-two]]
            [clj-mutex.threads :refer [make-indexed-thread]])
  (:gen-class))


(defn run []
  (let [lock-two (make-lock-two)
        c (make-safe-counter lock-two)
        nthreads 2
        iters 1000000
        f #(dotimes [_ iters] (.getAndIncrement c))
        threads (into [] (map #(make-indexed-thread % f) (range nthreads)))]
    (doseq [t threads]
      (println (format "Starting thread: %d" (.getIndex t)))
      (.start t))
    (println "When thread executions are interleaved, this will dead-lock.")
    (println "Time out in 5 seconds")
    (doseq [t threads]
      (.join t 2500))
    (println (format "counter should be: %d" (* nthreads iters)))
    (println (format "counter value: %d" (.getValue c)))
    (doseq [t threads]
      (.stop t))))


(defn -main
  [& args]
  (run))
