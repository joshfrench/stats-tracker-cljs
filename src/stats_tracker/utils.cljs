(ns stats-tracker.utils)

(defn middle [a b c] (-> [a b c] sort (nth 1)))

(defn modifier [x] (-> x (- 10) (/ 2) int))
