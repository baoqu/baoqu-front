(ns baoqu.services.idea
  (:require [baoqu.data :as d]
            [baoqu.form-utils :as fu]))

(defn add-idea
  []
  (let [body (get-in @d/state [:form :idea])
        new-idea {:id "x" :body body :votes 0}]
    (swap! d/state update :ideas conj new-idea)
    (fu/empty-form)))
