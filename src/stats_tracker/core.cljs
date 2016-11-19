(ns stats-tracker.core
   (:require [clojure.string :refer [upper-case]]
            [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [sablono.core :as html :refer-macros [html]]
            [stats-tracker.protocols :refer [ac pd md hp atk hit miss]]
            [stats-tracker.classes :refer [Smasher Sneaker]])
   (:require-macros [stats-tracker.class :refer [class]]))

(defn character [state]
  (let [{:keys [class] :as stats} @state]
    (new class stats)))

(defmulti read om/dispatch)

(defmethod read :default
  [{:keys [state] :as env} key _]
  (let [st @state]
    (when-let [[_ v] (find st key)]
      {:value v})))

(defmethod read :ac
  [{:keys [state]} _ _]
  {:value (ac (character state))})

(defmethod read :pd
  [{:keys [state]} _ _]
  {:value (pd (character state))})

(defmethod read :md
  [{:keys [state]} _ _]
  {:value (md (character state))})

(defmethod read :hp
  [{:keys [state]} _ _]
  {:value (hp (character state))})

(defmethod read :atk
  [{:keys [state]} _ _]
  {:value (atk (character state))})

(defmethod read :hit
  [{:keys [state]} _ _]
  {:value (hit (character state))})

(defmethod read :miss
  [{:keys [state]} _ _]
  {:value (miss (character state))})

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
(def class-map (zipmap ["Smashy Smash" "Sneaky Mofo"] classes))

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

(defn stat->name [s]
  (-> s name str upper-case))

(defn stat-input [stat val]
  [:p (stat->name stat) " "
   [:input {:type "number"
            :on-change #(om/transact! reconciler `[(stat/change {~stat ~(.. % -target -value)})])
            :value val}]])

(defui Tracker
  static om/IQuery
  (query [this]
    [:level :str :dex :con :int :wis :cha :ac :pd :md :hp :atk :hit :miss])
  Object
  (render [this]
    (let [{:keys [level ac pd md hp atk hit miss]} (om/props this)
          stat #(stat-input % (% (om/props this)))]
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
             [:p "MISS " (:melee miss)]]))))

(om/add-root! reconciler
  Tracker (gdom/getElement "app"))
