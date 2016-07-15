(ns baoqu.services.idea
  (:require [baoqu.data :as d]
            [baoqu.form-utils :as fu]
            [baoqu.services.http :as http]
            [baoqu.config :refer [cfg]]))

(enable-console-print!)

(defn add-idea
  []
  (let [body (fu/get-f :idea)
        ideas (:ideas @d/state)
        new-id (inc (apply max (map first ideas)))
        new-idea {:id new-id :body body :votes 1 :is-voted true}]
    (swap! d/state update :ideas merge {new-id new-idea})
    (fu/empty-f :idea)))

(defn react-to-upvote
  [id user-id]
  (let [my-id (get-in @d/state [:me :id])]
    (if (= my-id user-id)
      (swap! d/state update-in [:ideas id "voted?"] not))
    (swap! d/state update-in [:ideas id "votes"] inc)))

(defn react-to-downvote
  [id user-id]
  (let [my-id (get-in @d/state [:me :id])]
    (if (= my-id user-id)
      (swap! d/state update-in [:ideas id "voted?"] not))
    (swap! d/state update-in [:ideas id "votes"] dec)))

(defn toggle-idea-vote
  [id]
  (fn []
    (let [voted? (get-in @d/state [:ideas id "voted?"])
          user-id (get-in @d/state [:me :id])
          idea-name (get-in @d/state [:ideas id "name"])
          data {:user-id user-id :idea-name idea-name}]
      (if voted?
        (http/post (str (:server cfg) "/api/ideas/downvote") data)
        (http/post (str (:server cfg) "/api/ideas/upvote") data)))))
