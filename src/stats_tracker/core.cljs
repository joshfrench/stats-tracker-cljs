(ns stats-tracker.core
   (:require [clojure.string :refer [upper-case]]
            [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [sablono.core :as html :refer-macros [html]]
            [stats-tracker.classes :refer [Smasher Sneaker]]))

(defmulti read om/dispatch)

(defmethod read :default
  [{:keys [state] :as env} key _]
  (let [st @state]
    (when-let [[_ v] (find st key)]
      {:value v})))

(defmulti mutate om/dispatch)

(defmethod mutate 'level/change
  [{:keys [state]} _ {new-level :level}]
  (let [new-level (js/parseInt new-level)]
    (when (<= 1 new-level 10)
      {:action #(swap! state assoc :level new-level)})))

(def classes [Smasher Sneaker])
(def class-map (zipmap ["Smashy Smash" "Sneaky Mofo"] classes))

(defmethod mutate 'class/change
  [{:keys [state]} _ {new-class :class}]
  (when-let [[_ new-class] (find class-map new-class)]
    {:action #(swap! state assoc :class new-class)}))

(defn valid-stat? [stat] (some #{stat} [:str :dex :con :int :wis :cha]))

(defmethod mutate 'stat/change
  [{:keys [state]} _ params]
  (let [[stat new-value] (first params)]
    (when (valid-stat? stat)
      {:action #(swap! state assoc stat (js/parseInt new-value))})))

(defonce app-state
  (atom {:class Smasher
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
     :parser (om/parser {:read read :mutate mutate })}))

(defn stat->name [s]
  (-> s name str upper-case))

(defn stat
  ([s] (stat s 8))
  ([s val]
   [:p (stat->name s) " "
    [:input {:type "number"
             :on-change #(om/transact! reconciler `[(stat/change {~s ~(.. % -target -value)})])
             :default-value val}]]))

(def class-options
  )

(defui Tracker
  static om/IQuery
  (query [this]
    [:level :class :str :con :dex :wis :int :cha])
  Object
  (render [this]
    (let [{:keys [level class str con dex wis int cha] :as stats} (om/props this)
          {:keys [ac pd md hp atk hit miss]} (class stats)]
      (js/console.log (clj->js [level hit miss]))
      (html [:div
             [:p "Class "
              [:select {:on-change #(om/transact! reconciler `[(class/change {:class ~(.. % -target -value)})])}
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
             [:p "MISS " (:melee miss)]
             ]))))

(om/add-root! reconciler
  Tracker (gdom/getElement "app"))
