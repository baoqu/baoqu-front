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

(defn get-active-circle
  []
  (:active-circle d/state))

(defn set-active-circle
  [circle-id]
  (swap! d/state assoc :active-circle circle-id))

(defn set-users
  [users]
  (swap! d/state assoc :users (list->map users)))
