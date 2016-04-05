(ns amdahl.core
  (:require [clojure.math.numeric-tower :refer [expt]])
  (:gen-class))


(def p 0.6)

(defn amdahl
  [p n]
  (/ 1 (+ (- 1 p) (/ p n))))

(amdahl p n)

; find 
(map #(amdahl p (expt 10 %)) (range 1 32))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
