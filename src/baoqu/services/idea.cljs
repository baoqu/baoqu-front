(ns baoqu.services.idea
  (:require [baoqu.data :as d]
            [baoqu.form-utils :as fu]))

(defn add-idea
  []
  (let [body (fu/get-f :idea)
        new-idea {:id "x" :body body :votes 1 :is-voted true}]
    (swap! d/state update :ideas conj new-idea)
    (fu/empty-f :idea)))

(defn toggle-idea-vote
  [id]
  (let [ideas (:ideas @d/state)]
    ;; (swap! d/state update-in :ideas ??)
    ))
