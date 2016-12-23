(ns baoqu.repos.idea
  (:require [baoqu.data :as d]
            [baoqu.utils :refer [list->map]]))

(defn get-ideas
  []
  (vals (:ideas @d/state)))

(defn set-ideas
  [ideas]
  (swap! d/state assoc :ideas (list->map ideas)))

(defn get-idea-by-id
  [id]
  (get-in @d/state [:ideas id]))

(defn get-idea-by-name
  [name]
  (->> (get-ideas)
       (filter #(= name (:name %)))
       (first)))

(defn get-all-ideas-for-user
  [user-id]
  (get-ideas))

(defn set-votes
  [votes]
  (swap! d/state assoc :votes votes))

(defn get-votes
  []
  (:votes @d/state))

(defn get-voted-filter
  []
  (:voted-filter @d/state))

(defn set-voted-filter
  [active?]
  (swap! d/state assoc :voted-filter active?))

(defn get-sorted-filter
  []
  (:sorted-filter @d/state))

(defn set-sorted-filter
  [active?]
  (swap! d/state assoc :sorted-filter active?))

(defn add-idea
  [id name]
  (let [idea {id {:id id :name name}}]
    (swap! d/state update :ideas merge idea)))

(defn add-vote
  [user-id idea-id]
  (let [vote {:user-id user-id :idea-id idea-id}]
    (swap! d/state update :votes merge vote)))

(defn remove-vote
  [user-id idea-id]
  (swap! d/state
         (fn [state]
           (let [votes (:votes @d/state)
                 votes (into [] (remove #(and (= user-id (:user-id %)) (= idea-id (:idea-id %))) votes))]
             (assoc state :votes votes)))))
