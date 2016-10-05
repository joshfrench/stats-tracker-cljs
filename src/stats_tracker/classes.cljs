(ns stats-tracker.classes
  (:require [stats-tracker.utils :refer [middle modifier]])
  (:require-macros [stats-tracker.class :refer [class]]))

(class Smasher
  (ac (-> (middle str dex con) modifier (+ 10 level))))

(class Sneaker
  (ac (-> (middle con dex wis) modifier (+ 12 level))))
