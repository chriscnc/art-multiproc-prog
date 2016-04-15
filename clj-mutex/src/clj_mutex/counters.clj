(ns clj-mutex.counters)


(definterface Counter 
  (^long getAndIncrement [])
  (^long getValue []))


(defn make-unsafe-counter
  "Create an unsafe counter instance that supports the 
  Counter interface."
  []
  (let [value (volatile! 0)]
    (proxy [Counter] []
      (getAndIncrement [] (vswap! value inc))
      (getValue [] @value))))


(defn make-safe-counter
  "Create an safe counter instance that supports the 
  Counter interface."
  [lock]
  (let [value (volatile! 0)]
    (proxy [Counter] []
      (getAndIncrement [] 
        (.acquire lock)
        (try 
          (vswap! value inc)
          (finally 
            (.release lock))))
      (getValue [] @value))))


; An alternative to the proxy implementation above. Seems to me
; the proxy solution is cleaner, and deftype imposes it's will
; on the constructor semantics forcing us to pass values for
; every piece of state, even if we don't need it. By default
; deftypes fields are immutable so having direct access it not 
; a problem, but in this case we want mutable and encapsulated
; state. Proxy fulfills this better.
;(defprotocol ICounter
;  (^long get-and-increment [this])
;  (^long get-value [this]))
;
;(deftype UnsafeCounter [^{:volatile-mutable true} value]
;  ICounter
;  (get-and-increment [this] (vswap! value inc))
;  (get-value [this] @value))
;
;(defn get-t-unsafe-counter []
;  (UnsafeCounter. (volatile! 0)))
