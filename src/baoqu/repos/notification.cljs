(ns baoqu.repos.notification
  (:require [baoqu.data :as d]))

(enable-console-print!)

(defn set-notification
  [notification]
  (swap! d/state assoc :notification notification))

(defn get-notification
  []
  (:notification @d/state))

(defn clear-notification
  []
  (swap! d/state assoc :notification {}))

(defn set-permission
  []
  (swap! d/state assoc :notification-permission :granted))

(defn get-permission
  []
  (:notification-permission @d/state))
