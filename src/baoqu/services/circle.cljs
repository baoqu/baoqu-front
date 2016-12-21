(ns baoqu.services.circle
  (:require [baoqu.repos.circle :as cr]
            [baoqu.repos.user :as ur]))

(defn get-my-circle
  []
  (-> (ur/get-my-path)
      (last)
      (cr/get-by-id)))

(defn is-my-circle?
  [{circle-id :id}]
  (-> (get-my-circle)
      (:id)
      (= circle-id)))

(defn get-highest-level
  []
  (->> (cr/get-circles)
       (into #{} (map :level))
       (sort)
       (last)))

(defn get-level-range
  "Returns a range from 1 to the highest level of all the circles"
  []
  (->> (get-highest-level)
       (+ 1)
       (range 1)
       (reverse)))

(defn get-parent-circles-for-level
  "Returns the circles without a parent of a given level"
  [level]
  (let [circles (cr/get-circles)]
    (filter #(and (= (:level %) level) (nil? (:parent-circle-id %))) circles)))

(defn get-inner-circles-for-circle
  [circle]
  (let [inner-ids (into #{} (:inner-circles circle))]
    (when (seq inner-ids)
      (filter (comp inner-ids :id) (cr/get-circles)))))

(defn visit-circle
  [e {:keys [id]}]
  (.preventDefault e)
  (.stopPropagation e)
  (ur/set-active-circle id))
