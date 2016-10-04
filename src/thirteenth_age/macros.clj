(ns thirteenth-age.macros
  (:require [thirteenth-age.iklass :refer [IKlass]]))

(defn formula->fn [[name & rest]] `(~name [~(symbol '_)] ~@rest))

(defmacro klass [name & fns]
  (let [stats (map formula->fn fns)]
    (do `(defrecord ~name [~(symbol 'hp)] IKlass ~@(map formula->fn fns)))))
