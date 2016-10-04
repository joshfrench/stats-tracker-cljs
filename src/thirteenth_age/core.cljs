(ns thirteenth-age.core
  (:require [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [thirteenth-age.iklass :refer [ac]]
            [sablono.core :as html :refer-macros [html]])
  (:require-macros [thirteenth-age.macros :refer [klass]]))

(defn middle [a b c] (-> [a b c] sort (nth 1)))

(defn modifier [x] (-> x (- 10) (/ 2) int))

(defmulti read om/dispatch)

(defmethod read :default
  [{:keys [state] :as env} key _]
  (let [st @state]
    (when-let [[_ v] (find st key)]
      {:value v})))

(defmethod read :ac
  [{:keys [state] :as env} key _]
  (let [{:keys [class level str con dex int wis cha]} @state]
    {:value (ac (new class level str con dex int wis cha))}))

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

(defonce app-state
  (atom {:class Barbarian
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
