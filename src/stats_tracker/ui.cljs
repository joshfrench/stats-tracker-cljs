(ns stats-tracker.ui
  (:require [clojure.string :refer [upper-case]]
            [om.next :as om :refer-macros [defui]]
            [sablono.core :as html :refer-macros [html]]
            [stats-tracker.classes :refer [classes]]))

(defn stat->name [s]
  (-> s name str upper-case))

(defui Tracker
  Object
  (render [this]
    (let [{:keys [level class str con dex wis int cha] :as stats} (om/props this)
          {:keys [change-class change-level change-stat]} (om/get-computed this)
          {:keys [ac pd md hp atk hit miss]} ((classes class) stats)
          stat (fn [s] [:p (stat->name s) " "
                  [:input {:type "number"
                           :on-change #(change-stat s (.. % -target -value))
                           :default-value 8}]])]
      (html [:div
             [:p "Class "
              [:select {:on-change #()}
              (map #(vector :option {:key %} %) (keys classes))]]
             [:p "Level "
              [:input {:type "number"
                       :on-change #(change-level (.. % -target -value))
                       :value level
                       :min 1
                       :max 10}]]
             (stat :str)
             (stat :con)
             (stat :dex)
             (stat :int)
             (stat :wis)
             (stat :cha)
             [:p "AC " ac]
             [:p "PD " pd]
             [:p "MD " md]
             [:p "HP " hp]
             [:p "ATTACK +" (:melee atk) " melee"]
             [:p "HIT (" level " WEAPON) +" (:melee hit)]
             [:p "MISS " (:melee miss)]
             ]))))
