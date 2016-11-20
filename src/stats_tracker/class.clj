(ns stats-tracker.class)

(defn to-stat [[name fn]]
  {(keyword name) fn})

(defmacro class [name & fns]
  `(defn ~name [{:keys ~'[level str con dex wis int cha]}]
     ~(apply merge {:level 'level} (map to-stat (partition 2 fns)))))
