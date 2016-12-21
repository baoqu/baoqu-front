(ns baoqu.repos.circle
  (:require [baoqu.data :as d]
            [baoqu.utils :refer [list->map]]))

(enable-console-print!)

(defn set-circles
  [circles]
  (swap! d/state assoc :circles (list->map circles)))
