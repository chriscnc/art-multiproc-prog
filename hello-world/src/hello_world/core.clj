(ns hello-world.core
  (require [clojure.tools.logging :as log])
  (:gen-class))


(defn run []
  (let [fs (for [i (range 1 8)] 
             (future (println (str "Hello world from thread: " i))))]
    (doall fs)))


(defn -main
  "Clojure Hello World"
  [& args]
  (run)
  (shutdown-agents))



