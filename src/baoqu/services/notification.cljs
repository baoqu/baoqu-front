(ns baoqu.services.notification
  (:require [baoqu.data :as d]))

(enable-console-print!)

(defn set-notification
  [notification]
  (swap! d/state assoc :notification notification))

(defn clear-notification
  []
  (swap! d/state assoc :notification {}))
