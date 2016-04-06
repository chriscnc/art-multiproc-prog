(ns amdahl.core
  (:require [clojure.math.numeric-tower :refer [abs expt]])
  (:gen-class))


(defn amdahl
  [p n]
  (/ 1 (+ (- 1 p) (/ p n))))


(defn limit-inf
  "Find the limit numerically of the function f as n goes to infinity"
  [f]
  (let [precision 0.0000000000001
        g (fn [f last-value n]
            (let [new-value (f n)]
              (if (< (abs (- last-value new-value)) precision)
                new-value
                (recur f new-value (* n 2)))))]
    (g f (f 1) 2)))


(defn exercise-6-1 []
  (let [p 0.6
        f (partial amdahl p)]
    (limit-inf f)))



(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
