(ns stats-tracker.class
  (:require [stats-tracker.protocols :refer [ClassStats]]))

(defn formula->fn
  [[name & rest]]
  `(~name [~(symbol '_)]
          (let [{:keys [~(symbol 'hp)]} ~(symbol 'stats)] ~@rest)))

(defmacro class [name & fns]
  (let [stats (map formula->fn fns)]
    (do `(defrecord ~name [~(symbol 'stats)] ClassStats ~@(map formula->fn fns)))))
