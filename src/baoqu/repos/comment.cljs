(ns baoqu.repos.comment
  (:require [baoqu.data :as d]
            [baoqu.utils :refer [list->map]]))

(defn get-comments
  []
  (vals (:comments d/state)))

(defn set-comments
  [comments]
  (swap! d/state assoc :comments (list->map comments)))

(defn get-all-for-circle
  [id]
  (->> @d/state
       (:comments)
       (vals)
       (filter #(= (:circle-id %) id))))
