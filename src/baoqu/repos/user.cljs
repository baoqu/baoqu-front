(ns baoqu.repos.user
  (:require [baoqu.data :as d]
            [baoqu.utils :refer [list->map]]))

(enable-console-print!)

(defn get-me
  []
  (:me @d/state))

(defn set-me
  [id name]
  (let [me {:id id :name name}]
    (swap! d/state assoc :me me)))

(defn set-my-path
  [path]
  (swap! d/state assoc-in [:me :path] path))

(defn get-my-path
  []
  (get-in @d/state [:me :path]))

(defn get-active-circle
  []
  (->> @d/state
      (:active-circle)
      (get (:circles @d/state))))

(defn set-active-circle
  [circle-id]
  (swap! d/state assoc :active-circle circle-id))

(defn get-active-section
  []
  (:active-section @d/state))

(defn set-active-section
  [active-section]
  (swap! d/state assoc :active-section active-section))

(defn get-users
  []
  (vals (:users @d/state)))

(defn set-users
  [users]
  (swap! d/state assoc :users (list->map users)))

(defn get-all-for-circle
  [circle-id]
  (filter #((into #{} (:circles %)) circle-id) (get-users)))
