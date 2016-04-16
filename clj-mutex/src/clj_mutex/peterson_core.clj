(ns clj-mutex.peterson-core
  (:require [clj-mutex.counters :refer [make-safe-counter]]
            [clj-mutex.locks :refer [make-peterson-lock]]
            [clj-mutex.threads :refer [make-indexed-thread]])
  (:gen-class))


(defn run []
  (let [peterson-lock (make-peterson-lock)
        c (make-safe-counter peterson-lock)
        nthreads 2
        iters 5
        f #(dotimes [_ iters] (.getAndIncrement c))
        threads (into [] (map #(make-indexed-thread % f) (range nthreads)))]
    (doseq [t threads]
      (println (format "Starting thread: %d" (.getIndex t)))
      (.start t))
    (doseq [t threads]
      (.join t 2500))
    (println (format "counter should be: %d" (* nthreads iters)))
    (println (format "counter value: %d" (.getValue c)))
    (doseq [t threads]
      (.stop t))))


(defn -main
  [& args]
  (run))
  
