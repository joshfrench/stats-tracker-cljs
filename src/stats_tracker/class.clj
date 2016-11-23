(ns stats-tracker.class)

;; (defn to-stat [[name & fn]]
;;   `(apply '~name fn))

(defmacro class [name & stats]
  `(def ~name (with-meta (fn [{:keys ~'[level str con dex wis int cha]}]
                           ~(merge (map (fn [[a b]] {a b}) (partition 2 stats)))) {:name (str '~name)})))
