(ns producer-consumer-pets.core
  (:require [clojure.tools.logging :as log])
  (:gen-class))

; maximum amount of food to be placed by bob at any one time.
(def max-food 5)


(def cans {:alice (ref :up)
           :bob (ref :up)})


; amount of food put out by bob.
(def food (atom 0)) 


(defn release-pets-to-eat []
  "Let the pets into the yard to eat some amount of food up
  to the amount of food put out by bob. I.e. the pets might not
  eat it all."
  (let [amount-eaten (inc (rand-int @food))]
    (Thread/sleep (* amount-eaten 1000))
    (swap! food #(- % amount-eaten))
    (log/info "Pets ate" amount-eaten "portions of food," @food "portions of food left.")))


(defn alice []
  ; wait until alices-can is down
  (while (= :up @(:alice cans))
    (Thread/sleep 1000))

  (log/info "Alice let's the pets out to eat.")
  (release-pets-to-eat)

  ; if food is finished, reset the can
  (if (zero? @food)
    (dosync (ref-set (:alice cans) :up)
            (ref-set (:bob cans) :down)))

  (recur))


(defn put-food []
  (let [amount (inc (rand-int max-food))]
    (log/info "Bob puts" amount "portions of food out.")
    (reset! food amount)))


(defn bob []
  ; wait until alice's can is up
  (while (= :up @(:bob cans))
    (Thread/sleep 1000))
  (put-food)
  (dosync (ref-set (:bob cans) :up)
          (ref-set (:alice cans) :down))
  (recur))


(defn -main
  [& args]
  (let [f-alice (future-call alice)
        f-bob (future-call bob)]
    ; start it off by pulling bob's can.
    (dosync (ref-set (:bob cans) :down))
    @f-alice
    @f-bob
    (shutdown-agents)))
