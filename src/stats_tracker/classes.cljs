(ns stats-tracker.classes
  (:require [stats-tracker.utils :refer [middle modifier hp-multiplier tier]])
  (:require-macros [stats-tracker.class :refer [class]]))

(def classes (sort-by #(-> % meta :name) [
  (class Barbarian
         ac (-> (middle con dex wis) modifier (+ 12 level))
         pd (-> (middle str con dex) modifier (+ 11 level))
         md (-> (middle int wis cha) modifier (+ 10 level))
         hp (* (+ 7 (modifier con)) (hp-multiplier level))
         recovery-die 10
         atk {:melee  (+ (modifier str) level)
              :ranged (+ (modifier dex) level)}
         hit {:melee  (* (tier level) (modifier str))
              :ranged (* (tier level) (modifier dex))}
         miss {:melee level
               :ranged 0})

  (class Rogue
         ac (-> (middle con dex wis) modifier (+ 12 level))
         pd (-> (middle str con dex) modifier (+ 12 level))
         md (-> (middle int wis cha) modifier (+ 10 level))
         hp (* (+ 6 (modifier con)) (hp-multiplier level))
         recovery-die 8
         atk {:melee  (+ (modifier dex) level)
              :ranged (+ (modifier dex) level)}
         hit {:melee  (* (tier level) (modifier dex))
              :ranged (* (tier level) (modifier dex))}
         miss {:melee level
               :ranged level})

  (class Ranger
         ac (-> (middle con dex wis) modifier (+ 14 level))
         pd (-> (middle str con dex) modifier (+ 11 level))
         md (-> (middle int wis cha) modifier (+ 10 level))
         hp (* (+ 7 (modifier con)) (hp-multiplier level))
         recovery-die 8
         atk {:melee  (+ (modifier (max str dex)) level)
              :ranged (+ (modifier dex) level)}
         hit {:melee  (* (tier level) (modifier str))
              :ranged (* (tier level) (modifier dex))}
         miss {:melee level
               :ranged level})

  (class Druid
         ac (-> (middle con dex wis) modifier (+ 10 level))
         pd (-> (middle str con dex) modifier (+ 11 level))
         md (-> (middle int wis cha) modifier (+ 11 level))
         hp (* (+ 6 (modifier con)) (hp-multiplier level))
         recovery-die 6
         atk {:melee (+ (modifier (max str dex)) level)
              :ranged (+ (modifier dex) level)}
         hit {:melee (* (tier level) (modifier (max str dex)))
              :ranged (* (tier level (modifier dex)))}
         miss {:melee level
               :ranged 0})
  ]))

(defn name->class [name]
  (first (filter #(= name (-> % meta :name)) classes)))
