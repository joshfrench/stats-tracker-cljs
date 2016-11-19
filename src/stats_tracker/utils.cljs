(ns stats-tracker.utils)

(defn middle [a b c] (-> [a b c] sort (nth 1)))

(defn modifier [x] (-> x (- 10) (/ 2) int (max 0)))

(defn hp-multiplier [level]
  (nth [3 4 5 6 8 10 12 16 20 24] (dec level)))

(defn tier [level]
  (condp >= level
    4  1
    7  2
    10 3))
