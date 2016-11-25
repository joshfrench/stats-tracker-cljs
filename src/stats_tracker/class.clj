(ns stats-tracker.class)

(defmacro class [name & stats]
  `(def ~name
     (-> (fn [{:keys ~'[level str con dex wis int cha]}]
           (merge-with merge ~@(partition 2 stats)))
         (with-meta {:name (str '~name)}))))
