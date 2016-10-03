(ns thirteenth-age.macros)

(defmacro klass [name & fns]
  (do `(defrecord ~name ~(vec ['hp]))))
