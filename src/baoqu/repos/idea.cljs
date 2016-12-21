(ns baoqu.repos.idea
  (:require [baoqu.data :as d]
            [baoqu.utils :refer [list->map]]))

(defn get-ideas
  []
  (vals (:ideas @d/state)))

(defn set-ideas
  [ideas]
  (swap! d/state assoc :ideas (list->map ideas)))

(defn get-idea-by-id
  [id]
  (get-in @d/state [:ideas id]))

(defn get-ideas-by-circle
  []
  (vals (:ideas @d/state)))

(defn get-all-ideas-for-user
  [user-id]
  (get-ideas))
