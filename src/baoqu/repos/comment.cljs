(ns baoqu.repos.comment
  (:require [baoqu.data :as d]
            [baoqu.utils :refer [list->map]]))

(defn set-comments
  [comments]
  (swap! d/state assoc :comments (list->map comments)))
