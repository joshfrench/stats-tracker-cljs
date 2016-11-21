(ns stats-tracker.classes
  (:require [stats-tracker.utils :refer [middle modifier hp-multiplier tier]])
  (:require-macros [stats-tracker.class :refer [class]]))

(def classes [
  (class Barbarian
          ac (-> (middle con dex wis) modifier (+ 12 level))
          pd (-> (middle str con dex) modifier (+ 11 level))
          md (-> (middle int wis cha) modifier (+ 10 level))
          hp (* (+ 7 (modifier con)) (hp-multiplier level))
          recovery-die 10
          atk {:melee  (+ (modifier str) level)
              :ranged (+ (modifier dex) level)}
          hit {:melee  (* (tier level) (modifier str))
              :ranged (* (tier level) (modifier dex))} ;; double-check this
          miss {:melee level
                :ranged 0})

  (class Rogue
          ac (-> (middle con dex wis) modifier (+ 12 level))
          pd (-> (middle str con dex) modifier (+ 12 level))
          md (-> (middle int wis cha) modifier (+ 10 level))
          hp (* (+ 6 (modifier con)) (hp-multiplier level)))])

(defn name->class [name]
  (first (filter #(= name (-> % meta :name)) classes)))
