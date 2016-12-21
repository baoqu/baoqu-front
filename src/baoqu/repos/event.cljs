(ns baoqu.repos.event
  (:require [baoqu.data :as d]))

(defn get-event
  []
  (:event d/state))

(defn set-event
  [id name agreement-factor circle-size]
  (let [event {:id id
               :name name
               :agreement-factor agreement-factor
               :circle-size circle-size}]
   (swap! d/state assoc :event event)))

(defn load-event-status
  [event-status]
  (let [event-circles (:circles event-status)
        circles-ids (map event-circles :id)
        plain-status (update event-status :circles circles-ids)]

    (swap! d/state assoc :event plain-status)
    (swap! d/state assoc :circles event-circles )))

(defn add-circle
  [circle]
  (let [circles (:circles @d/state)]
    (swap! d/state assoc-in :circles (into [] (concat circles circle)))))

(defn add-participant
  [participant]
  (let [participants (:participants @d/state)]
    (swap! d/state assoc-in :participants (into [] (concat participants participant)))))
