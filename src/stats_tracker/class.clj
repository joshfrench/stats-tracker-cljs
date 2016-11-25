(ns stats-tracker.class)

(defmacro base-hp [hp]
  `{:hp (* (+ ~hp ~'(modifier con)) ~'(hp-multiplier level))})

(defmacro base-ac [ac]
  `{:ac 12})

(defmacro class [name & stats]
  `(def ~name
     (with-meta
       (fn [{:keys ~'[level str con dex wis int cha]}]
         (merge ~@(partition 2 stats)))
       {:name (str '~name)})))
