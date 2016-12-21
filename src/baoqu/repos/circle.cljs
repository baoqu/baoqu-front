(ns baoqu.repos.circle
  (:require [baoqu.data :as d]
            [baoqu.utils :refer [list->map]]))

(enable-console-print!)

(defn get-circles
  []
  (vals (:circles @d/state)))

(defn set-circles
  [circles]
  (swap! d/state assoc :circles (list->map circles)))

(defn get-by-id
  [circle-id]
  (-> (:circles @d/state)
      (get circle-id)))
