(ns thirteenth-age.class
  (:require [thirteenth-age.protocols :refer [ClassStats]]))

(defn formula->fn
  [[name & rest]]
  `(~name [~(symbol '_)]
          (let [{:keys [~(symbol 'hp)]} ~(symbol 'stats)] ~@rest)))

(defmacro class [name & fns]
  (let [stats (map formula->fn fns)]
    (do `(defrecord ~name [~(symbol 'stats)] ClassStats ~@(map formula->fn fns)))))
