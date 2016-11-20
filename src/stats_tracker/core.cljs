(ns stats-tracker.core
   (:require [om.next :as om :refer-macros [defui]]
             [sablono.core :as html :refer-macros [html]]
             [clojure.string :refer [upper-case]]
             [goog.dom :as gdom]
             [stats-tracker.classes :refer [classes]]
             [stats-tracker.ui :refer [Tracker]]
             [stats-tracker.parser :as p]))

(defonce app-state
  (atom {:class (-> classes keys first)
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
     :parser (om/parser {:read p/read :mutate p/mutate })}))

(def tracker (om/factory Tracker))

(defui Root
  static om/IQuery
  (query [_] [:level :class :str :con :dex :wis :int :cha])

  Object
  (render [this]
    (tracker (om/computed (om/props this)
                          {:change-class   #(om/transact! reconciler `[(class/change {:class ~%})])
                           :change-level   #(om/transact! reconciler `[(level/change {:level ~%})])
                           :change-stat    (fn [stat val] (om/transact! reconciler `[(stat/change {~stat ~val})]))}))))

(om/add-root! reconciler
  Root (gdom/getElement "app") {:reconciler reconciler})
