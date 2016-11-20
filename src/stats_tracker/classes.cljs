(ns stats-tracker.classes
  (:require [stats-tracker.utils :refer [middle modifier hp-multiplier tier]])
  (:require-macros [stats-tracker.class :refer [class]]))

(class Smasher
  ac (-> (middle con dex wis) modifier (+ 12 level))
  pd (-> (middle str con dex) modifier (+ 11 level))
  md (-> (middle int wis cha) modifier (+ 10 level))
  hp (* (+ 7 (modifier con)) (hp-multiplier level))
  atk {:melee (+ (modifier str) level)
       :ranged (+ (modifier dex) level)}
  hit {:melee (* (tier level) (modifier str))}
  miss {:melee level})

(class Sneaker
  ac (-> (middle con dex wis) modifier (+ 12 level))
  pd (-> (middle str con dex) modifier (+ 12 level))
  md (-> (middle int wis cha) modifier (+ 10 level))
  hp (* (+ 6 (modifier con)) (hp-multiplier level))
  atk)
