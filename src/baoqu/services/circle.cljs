(ns baoqu.services.circle
  (:require [baoqu.repos.circle :as circle-r]
            [baoqu.repos.user :as user-r]))

(defn get-my-circle
  []
  (-> (user-r/get-my-path)
      (last)
      (circle-r/get-by-id)))
