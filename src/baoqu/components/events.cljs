(ns baoqu.components.events
  (:require [rum.core :as rum]
            [baoqu.data :as d]
            [baoqu.repos.event :as event-r]
            [baoqu.services.event :as event-s]))

(rum/defc event-item
  [{:keys [id name circle-size agreement-factor]}]
  [:div {:key id}
   [:hr]
   [:div [:strong "Id :: "] id]
   [:div [:strong "Name :: "] name]
   [:div [:strong "Circle size :: "] circle-size]
   [:div [:strong "Agreement factor :: "] agreement-factor]])


(rum/defc main < rum/reactive
  []
  (let [state (rum/react d/state)
        events (event-r/get-events)]
    (if-not (:events @d/state)
      ;; should be done after login
      (event-s/fetch-events))

    [:div [:h1 "EVENTS!!"]
     (for [event events]
       (event-item event))]))
