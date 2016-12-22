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

(defn get-all-ideas-for-user
  [user-id]
  (get-ideas))

(defn set-votes
  [votes]
  (swap! d/state assoc :votes votes))

(defn get-votes
  []
  (:votes @d/state))

(defn get-voted-filter
  []
  (:voted-filter @d/state))

(defn set-voted-filter
  [active?]
  (swap! d/state assoc :voted-filter active?))
