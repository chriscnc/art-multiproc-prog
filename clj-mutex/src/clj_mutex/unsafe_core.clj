(ns clj-mutex.unsafe-core
  (:require [clj-mutex.counters :refer [make-unsafe-counter]]
            [clj-mutex.threads :refer [make-indexed-thread]])
  (:gen-class))


(defn run []
  (let [c (make-unsafe-counter)
        nthreads 2
        iters 1000000
        f #(dotimes [_ iters] (.getAndIncrement c))
        threads (into [] (map #(make-indexed-thread % f) (range nthreads)))]
    (doseq [t threads]
      (println (format "Starting thread: %d" (.getIndex t)))
      (.start t))
    (doseq [t threads]
      (.join t))
    (println (format "counter should be: %d" (* nthreads iters)))
    (println (format "counter value: %d" (.getValue c)))
    (doseq [t threads]
      (.stop t))))


(defn -main
  [& args]
  (run))
