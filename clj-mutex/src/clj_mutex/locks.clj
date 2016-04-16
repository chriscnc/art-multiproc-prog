(ns clj-mutex.locks)


(definterface Lock
  (acquire [])
  (release []))


(defn- get-thread-index
  "Get the index of the current thread. Assumes the thread
  implements the Indexable interface."
  []
  (.getIndex (Thread/currentThread)))


(defn make-lock-one
  "Create a lock-one instance that implements the 
  Lock interface."
  []
  (let [flag (volatile! (make-array Boolean/TYPE 2))]
    (proxy [Lock] []
      (acquire [] (let [i (get-thread-index)
                        j (- 1 i)]
                    (aset @flag i true)
                    (while (aget @flag j)
                      ;; wait
                      )))
      (release [] (let [i (get-thread-index)]
                    (aset @flag i false))))))


(defn make-lock-two
  "Create a lock-two instance that implements the 
  Lock interface."
  []
  (let [victim (volatile! 0)]
    (proxy [Lock] []
      (acquire [] (let [i (get-thread-index)]
                    (vreset! victim i)
                    (while (= @victim i)
                      ;; wait
                      )))
      (release [] ))))


;; this dead locks for some reason, and it shouldn't
(defn make-peterson-lock
  "Create a peterson lock instance that implements the 
  Lock interface."
  []
  (let [flag (volatile! (make-array Boolean/TYPE 2))
        victim (volatile! 0)]
    (proxy [Lock] []
      (acquire [] (let [i (get-thread-index)
                        j (- 1 i)]
                    (aset @flag i true) ; I'm interested
                    (vreset! victim i) ; you go first
                    (while (and (aget @flag j) (= @victim i))
                      (println (format "flag[j]=%s, victim=%d" (aget @flag j) @victim))
                      ;; wait
                      )))
      (release [] (let [i (get-thread-index)]
                    (aset @flag i false))))))

