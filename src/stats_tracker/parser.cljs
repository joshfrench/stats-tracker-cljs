(ns stats-tracker.parser
  (:require [om.next :as om]
            [stats-tracker.classes :refer [classes name->class]]))

(defn read [{:keys [state] :as env} key _]
  (let [st @state]
    (when-let [[_ v] (find st key)]
      {:value v})))

(defmulti mutate om/dispatch)

(defmethod mutate 'level/change
  [{:keys [state]} _ {new-level :level}]
  (let [new-level (js/parseInt new-level)]
    (when (<= 1 new-level 10)
      {:action #(swap! state assoc :level new-level)})))

(defmethod mutate 'class/change
  [{:keys [state]} _ {new-class :class}]
  (when (name->class new-class)
    {:action #(swap! state assoc :class new-class)}))

(defn valid-stat? [stat] (some #{stat} [:str :dex :con :int :wis :cha]))

(defmethod mutate 'stat/change
  [{:keys [state]} _ params]
  (let [[stat new-value] (first params)
        new-value (js/parseInt new-value)]
    (when (and (valid-stat? stat)
               (<= 8 new-value 24))
      {:action #(swap! state assoc stat new-value)})))

