(ns baoqu.repos.idea
  (:require [baoqu.data :as d]
            [baoqu.utils :refer [list->map]]))

(defn set-ideas
  [ideas]
  (swap! d/state assoc :ideas (list->map ideas)))
