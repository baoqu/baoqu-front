(ns baoqu.services.comment
  (:require [baoqu.data :as d]
            [baoqu.form-utils :as fu]))

(defn add-comment
  []
  (let [username (get-in @d/state [:session :username])
        author (first (filter #(= username (:name %)) (:participants @d/state)))
        body (fu/get-f :comment)
        new-comment {:id "x" :body body :author (:id author) :date "xy"}]
    (swap! d/state update :comments conj new-comment)
    (fu/empty-f :comment)))
