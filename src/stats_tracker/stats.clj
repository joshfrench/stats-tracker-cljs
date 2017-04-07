(ns stats-tracker.stats)

(defmacro base-hp [hp]
  `{:hp (* (+ ~hp ~'(modifier con)) ~'(hp-multiplier level))})

(defmacro base-ac [ac]
  `{:ac (-> ~'(middle con dex wis) ~'modifier (+ ~ac ~'level))})

(defmacro base-pd [pd]
  `{:pd (-> ~'(middle str con dex) ~'modifier (+ ~pd ~'level))})

(defmacro base-md [md]
  `{:md (-> ~'(middle int wis cha) ~'modifier (+ ~md ~'level))})

(defmacro recovery-die [d]
  `{:recovery-die ~d})

(defmacro melee-attack [mod]
  `{:attack {:melee (+ (~'modifier ~mod) ~'level)}})

(defmacro ranged-attack [mod]
  `{:attack {:ranged (+ (~'modifier ~mod) ~'level)}})

(defmacro attack [mod]
  `(merge-with merge (~'melee-attack ~mod) (~'ranged-attack ~mod)))

(defmacro melee-hit [mod]
  `{:hit {:melee (* ~'(tier level) (~'modifier ~mod))}})

(defmacro ranged-hit [mod]
  `{:hit {:ranged (* ~'(tier level) (~'modifier ~mod))}})

(defmacro hit [mod]
  `(merge-with merge (~'melee-hit ~mod) (~'ranged-hit ~mod)))

(defmacro melee-miss [dmg]
  `{:miss {:melee ~dmg}})

(defmacro ranged-miss [dmg]
  `{:miss {:ranged ~dmg}})

(defmacro miss [dmg]
  `(merge-with merge (~'melee-miss ~dmg) (~'ranged-miss ~dmg)))
