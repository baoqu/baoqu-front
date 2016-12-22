(ns baoqu.services.circle
  (:require [baoqu.repos.circle :as cr]
            [baoqu.repos.idea :as ir]
            [baoqu.repos.user :as ur]))

(enable-console-print!)

(defn get-my-circle
  []
  (-> (ur/get-my-path)
      (last)
      (cr/get-by-id)))

(defn is-my-circle?
  [{:keys [id]}]
  (-> (get-my-circle)
      (:id)
      (= id)))

(defn get-active-circle
  []
  (ur/get-active-circle))

(defn is-active-circle?
  [{:keys [id]}]
  (-> (get-active-circle)
      (:id)
      (= id)))

(defn get-circles
  []
  (cr/get-circles))

(defn circle-in-path?
  [{:keys [id]}]
  ((ur/get-my-path) id))

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

(defn get-participants-count
  "Returns the number of participants of a circle"
  [{:keys [size level]}]
  (* size level))

(defn get-inner-circles-for-circle
  [circle]
  (let [inner-ids (into #{} (:inner-circles circle))]
    (when (seq inner-ids)
      (filter (comp inner-ids :id) (cr/get-circles)))))

(defn visit-circle
  [e {:keys [id]}]
  (.preventDefault e)
  (.stopPropagation e)
  (ir/set-voted-filter false)
  (ur/set-active-circle id))
