(ns stats-tracker.class
  (:require [stats-tracker.protocols :refer [ClassStats]]))

(defn formula->fn
  [[name & rest]]
  `(~name [~'_]
          (let [{:keys ~'[level str dex con wis int cha]} ~'stats] ~@rest)))

(defmacro class [name & fns]
  (let [stats (map formula->fn fns)]
    (do `(defrecord ~name [~'stats] ClassStats ~@(map formula->fn fns)))))
