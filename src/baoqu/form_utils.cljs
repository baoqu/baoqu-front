(ns baoqu.form-utils
  (:require [baoqu.data :as d]))

(defn change-field [& path]
  (fn [e]
    (let [new-value (-> e (.-target) (.-value))]
      (swap! d/state assoc-in path new-value))))

(def change-in-form (partial change-field :form))

(defn get-form
  []
  (:form @d/state))

(defn get-f
  [key]
  (get-in @d/state [:form key] ""))

(defn empty-form []
  (swap! d/state dissoc :form))

(defn empty-f
  [key]
  (swap! d/state update :form dissoc key))
