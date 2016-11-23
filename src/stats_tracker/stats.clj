(ns stats-tracker.stats)

(defmacro base-hp [hp]
  `{:hp (* (+ ~hp ~'(modifier con)) ~'(hp-multiplier level))})
