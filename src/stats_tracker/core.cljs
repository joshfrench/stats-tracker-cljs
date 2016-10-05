(ns stats-tracker.core
  (:require [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [sablono.core :as html :refer-macros [html]]
            [stats-tracker.protocols :refer [ac class-name]]
            [stats-tracker.classes :refer [Smasher Sneaker]]))

(defmulti read om/dispatch)

(defmethod read :default
  [{:keys [state] :as env} key _]
  (let [st @state]
    (when-let [[_ v] (find st key)]
      {:value v})))

(defmethod read :ac
  [{:keys [state] :as env} key _]
  (let [{:keys [class] :as stats} @state
        character (new class stats)]
    {:value (ac character)}))

(defmulti mutate om/dispatch)

(defmethod mutate 'level/change
  [{:keys [state]} _ {new-level :level}]
  (when (<= 1 new-level 10)
    {:action #(swap! state assoc :level (int new-level))}))

(def valid-stats [:str :dex :con :int :wis :cha])
(defn valid-stat? [stat] (some #{stat} valid-stats))

(defmethod mutate 'stat/change
  [{:keys [state]} _ params]
  (let [[stat new-value] (first params)]
    (when (valid-stat? stat)
      {:action #(swap! state assoc stat (int new-value))})))

(def classes [Smasher Sneaker])
(def class-map (zipmap ["Smasher" "Sneaker"] classes))

(defmethod mutate 'class/change
  [{:keys [state]} _ {new-class :class}]
  (when (some #{new-class} classes)
    {:action #(swap! state assoc :class new-class)}))

(defonce app-state
  (atom {:class (class-map (-> class-map keys sort first))
         :level 1
         :str   8
         :dex   8
         :con   8
         :int   8
         :wis   8
         :cha   8}))

(def reconciler
  (om/reconciler
    {:state app-state
     :parser (om/parser {:read read :mutate mutate})}))

(defui Tracker
  static om/IQuery
  (query [this]
    [:level :str :dex :con :ac])
  Object
  (render [this]
    (let [{:keys [level ac dex con str]} (om/props this)]
      (html [:div
             [:p "Class "
              [:select {:on-change #(om/transact! reconciler `[(class/change {:class ~(class-map (.. % -target -value))})])}
              (map #(vector :option {:key %} %) (keys class-map))]]
             [:p "Level "
              [:input {:type "number"
                       :on-change #(om/transact! reconciler `[(level/change {:level ~(.. % -target -value)})])
                       :value level
                       :min 1
                       :max 10}]]
             [:p "STR "
              [:input {:type "number"
                       :on-change #(om/transact! reconciler `[(stat/change {:str ~(.. % -target -value)})])
                       :value str}]]
             [:p "DEX "
              [:input {:type "number"
                       :on-change #(om/transact! reconciler `[(stat/change {:dex ~(.. % -target -value)})])
                       :value dex}]]
             [:p "CON "
              [:input {:type "number"
                       :on-change #(om/transact! reconciler `[(stat/change {:con ~(.. % -target -value)})])
                       :value con}]]
             [:p "AC " ac]]))))

(om/add-root! reconciler
  Tracker (gdom/getElement "app"))
