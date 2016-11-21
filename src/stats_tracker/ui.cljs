(ns stats-tracker.ui
  (:require [clojure.string :refer [upper-case]]
            [om.next :as om :refer-macros [defui]]
            [sablono.core :as html :refer-macros [html]]
            [stats-tracker.classes :refer [classes]]
            [stats-tracker.utils :refer [modifier tier]]))

(defn stat->name [s]
  (-> s name str upper-case))

(defui Tracker
  Object
  (render [this]
    (let [{:keys [level class str con dex wis int cha] :as stats} (om/props this)
          {:keys [change-class change-level change-stat]} (om/get-computed this)
          {:keys [ac pd md hp atk hit miss recovery-die]} ((classes class) stats)]
      (html [:form {:class "container"}
             [:h1 "Stats Tracker"]
             ;; Class & Level
             [:div {:class "row form-inline"}
              [:div {:class "form-group col-md-4"}
               [:label {:class "col-md-2 control-label" :for "class-select"} "Class"]
               [:div {:class "col-md-2"}
                [:select  {:on-change #(change-class (.. % -target -value))
                           :class "form-control"
                           :id "class-select"}
                 (map #(vector :option {:key %} %)  (keys classes))]]]
              [:div {:class "form-group col-md-4"}
               [:label {:class "col-md-2 control-label":for "level-select"} "Level "]
               [:div {:class "col-md-2"}
                [:input  {:type "number"
                          :class "form-control"
                          :id "level-select"
                          :on-change #(change-level  (.. % -target -value))
                          :value level
                          :min 1
                          :max 10}]]]]
             ;; Core stats
             [:hr]
             [:div {:class "row"}
              [:div {:class "col-md-12"}
              [:table {:class "table table-responsive"}
               [:thead
                [:tr
                 [:th]
                 (map #(vector :th [:label {:for %} (upper-case %)])
                      ["str" "con" "dex" "int" "wis" "cha"]) ]]
               [:tbody
                [:tr {:class "form-group"}
                 [:th {:scope "row"}]
                 (let [stat (fn [s] [:td
                                     [:input {:type "number"
                                              :class "form-control input-lg"
                                              :id s
                                              :on-change #(change-stat s (.. % -target -value))
                                              :default-value 8
                                              :min 8
                                              :max 24}]])]
                  (map stat [:str :con :dex :int :wis :cha]))]
                [:tr
                 [:th {:scope "row"} "Mod"]
                 (map #(vector :td {:class "text-center"} (modifier %)) [str con dex int wis cha])]
                [:tr
                 [:th {:scope "row" :class "text-nowrap"} "Mod + Lvl"]
                 (map-indexed (fn [i stat] (vector :td
                                                   {:class "text-center"}
                                                   (+ level (modifier stat))
                                                   (when (= 2 i) [:p [:span {:class "label label-default"} "Initiative"]])))
                              [str con dex int wis cha])]]]]]
             ;; Computed stats
             [:hr]
             [:div {:class "row"}
              [:div {:class "col-md-6"}
               [:table {:class "table"}
                [:thead
                 [:tr
                  (map #(vector :th %) ["AC" "PD" "MD"])]]
                [:tbody
                 [:tr
                  (map #(vector :td %) [ac pd md])]]]]
              [:div {:class "col-md-6"}
               [:table {:class "table"}
                [:thead
                 [:tr
                  (map #(vector :th {:class "col-md-6"} %) ["HP" "Recovery"])]]
                [:tbody
                 [:tr
                  (let [recovery (clojure.core/str level "d" recovery-die " + "
                                                    (* (tier level) (modifier con)))]
                    (map #(vector :td {:class "col-md-6"} %) [hp recovery]))]]]]]
             [:div {:class "row"}
              [:div {:class "col-md-6"}
               [:table {:class "table"}
                [:thead
                 [:tr
                  (map #(vector :th %) ["" "ATTACK" "HIT" "MISS"])]]
                [:tbody
                 [:tr
                  [:th {:scope "row"} "Melee"]
                  (map-indexed (fn [i stat] (vector :td (when (= 0 i) "+") (:melee stat))) [atk hit miss])]
                 [:tr
                  [:th {:scope "row"} "Ranged"]
                  (map-indexed (fn [i stat] (vector :td (when (= 0 i) "+") (:ranged stat))) [atk hit miss])]]]]]]))))