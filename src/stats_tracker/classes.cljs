(ns stats-tracker.classes
  (:require [stats-tracker.utils :refer [middle modifier hp-multiplier tier]])
  (:require-macros [stats-tracker.class :refer [class]]
                   [stats-tracker.stats :refer [base-hp base-ac base-pd base-md recovery-die
                                                attack melee-attack ranged-attack
                                                hit melee-hit ranged-hit
                                                miss melee-miss ranged-miss]]))

(def classes (sort-by #(-> % meta :name) [
  (class Barbarian
         base-hp 7
         base-ac 12
         base-pd 11
         base-md 10
         recovery-die 10
         melee-attack str
         ranged-attack dex
         melee-hit str
         ranged-hit dex
         melee-miss level
         ranged-miss 0)

  (class Rogue
         base-hp 6
         base-ac 12
         base-pd 12
         base-md 10
         recovery-die 8
         attack dex
         hit dex
         miss level)

  (class Ranger
         base-hp 7
         base-ac 14
         base-pd 11
         base-md 10
         recovery-die 8
         melee-attack (max str dex)
         ranged-attack dex
         melee-hit str
         ranged-hit dex
         miss level)

  (class Druid
         base-hp 6
         base-ac 10
         base-pd 11
         base-md 11
         recovery-die 6
         melee-attack (max str dex)
         ranged-attack dex
         melee-hit (max str dex)
         ranged-hit dex
         melee-miss level
         ranged-miss 0)
  ]))

(defn name->class [name]
  (first (filter #(= name (-> % meta :name)) classes)))
