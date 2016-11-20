(ns stats-tracker.parser
  (:require [om.next :as om]
            [stats-tracker.classes :refer [classes]]))

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

(defmethod mutate 'class/change
  [{:keys [state]} _ {new-class :class}]
  (when-let [[new-class _] (find classes new-class)]
    {:action #(swap! state assoc :class new-class)}))

(defn valid-stat? [stat] (some #{stat} [:str :dex :con :int :wis :cha]))

(defmethod mutate 'stat/change
  [{:keys [state]} _ params]
  (let [[stat new-value] (first params)]
    (when (valid-stat? stat)
      {:action #(swap! state assoc stat (js/parseInt new-value))})))

