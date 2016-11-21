(ns stats-tracker.class)

(defn to-stat [[name fn]]
  {(keyword name) fn})

(defmacro class [name & fns]
  `(def ~name (with-meta (fn [{:keys ~'[level str con dex wis int cha]}]
     ~(apply merge {:level 'level} (map to-stat (partition 2 fns)))) {:name (str '~name)})))
