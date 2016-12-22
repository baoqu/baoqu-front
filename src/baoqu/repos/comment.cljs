(ns baoqu.repos.comment
  (:require [baoqu.data :as d]
            [baoqu.utils :refer [list->map]]))

(defn get-comments
  []
  (-> (:comments d/state)
      (vals)
      (sort-by :date)))

(defn set-comments
  [comments]
  (swap! d/state assoc :comments (list->map comments)))

(defn get-all-for-circle
  [id]
  (->> @d/state
       (:comments)
       (vals)
       (sort-by :date)
       (filter #(= (:circle-id %) id))))

(defn add-comment
  [{:keys [id] :as comment}]
  (swap! d/state update :comments merge {id comment}))
