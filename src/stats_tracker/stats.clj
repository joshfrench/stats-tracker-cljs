(ns stats-tracker.stats)

(defmacro base-ac [ac]
  `{:ac 12})

(defmacro base-hp [hp]
  `{:hp (* (+ ~hp ~'(modifier con)) ~'(hp-multiplier level))})
