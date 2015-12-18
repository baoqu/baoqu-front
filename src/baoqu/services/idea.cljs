(ns baoqu.services.idea
  (:require [baoqu.data :as d]
            [baoqu.form-utils :as fu]))

(enable-console-print!)

(defn add-idea
  []
  (let [body (fu/get-f :idea)
        ideas (:ideas @d/state)
        new-id (inc (apply max (map first ideas)))
        new-idea {:id new-id :body body :votes 1 :is-voted true}]
    (swap! d/state update :ideas merge {new-id new-idea})
    (fu/empty-f :idea)))

(defn toggle-idea-vote
  [id]
  (fn []
    (let [is-voted (get-in @d/state [:ideas id :is-voted])]
      (if is-voted
        (swap! d/state update-in [:ideas id :votes] dec)
        (swap! d/state update-in [:ideas id :votes] inc))
      (swap! d/state update-in [:ideas id :is-voted] not))))
