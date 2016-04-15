(ns clj-mutex.threads)


(definterface Indexable
  (^long getIndex []))


(defn make-indexed-thread
  "Return a object a subclass of Thread that implements the
  Indexable interface."
  [^long index ^Runnable target]
  (proxy [Thread Indexable] [target]
    (getIndex [] index)))
