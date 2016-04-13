(ns clj-mutex.core
  (:gen-class))


(defn get-thread-index
  "Get the index of the current thread. Assumes the thread
  implements the Indexable interface."
  []
  (.getIndex (Thread/currentThread)))


(definterface Indexable
  (^long getIndex []))


(defn make-indexed-thread
  "Return a object a subclass of Thread that implements the
  Indexable interface."
  [^long index ^Runnable target]
  (proxy [Thread Indexable] [target]
    (getIndex [] index)))


(definterface Lock
  (acquire [])
  (release []))


(defn make-lock-one
  []
  (let [flag (volatile! (make-array Boolean/TYPE 2))]
    (proxy [Lock] []
      (acquire [] (let [i (get-thread-index)
                        j (- 1 i)]
                    (aset @flag i true)
                    (while (aget flag j))))
      (release [] (let [i (get-thread-index)]
                    (aset @flag i false))))))


(definterface Counter 
  (^long getAndIncrement [])
  (^long getValue []))

(defn make-unsafe-counter
  []
  (let [value (volatile! 0)]
    (proxy [Counter] []
      (getAndIncrement [] (vswap! value inc))
      (getValue [] @value))))


(defprotocol ICounter
  (^long get-and-increment [this])
  (^long get-value [this]))
  

(deftype UnsafeCounter [^{:volatile-mutable true} value]
  ICounter
  (get-and-increment [this] (vswap! value inc))
  (get-value [this] @value))

(defn get-t-unsafe-counter []
  (UnsafeCounter. (volatile! 0)))

;(let [c (get-t-unsafe-counter)]
;  (get-and-increment c)
;  (get-value c))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
