(ns stats-tracker.class)

(defmacro class [name & stats]
  `(def ~name
     (with-meta
       (fn [{:keys ~'[level str con dex wis int cha]}]
         (merge-with merge ~@(partition 2 stats)))
       {:name (str '~name)})))
