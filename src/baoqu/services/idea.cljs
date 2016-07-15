(ns baoqu.services.idea
  (:require [baoqu.data :as d]
            [baoqu.form-utils :as fu]
            [baoqu.services.http :as http]
            [baoqu.config :refer [cfg]]))

(enable-console-print!)

(defn add-idea-if-new
  [idea]
  (let [ideas (:ideas @d/state)
        idea-id (get idea "id")
        ideas-ids (into #{} (for [[k v] ideas] k))
        my-id (get-in @d/state [:me :id])
        mine? (= my-id (get-in idea ["user" "id"]))]
    (if-not (boolean (ideas-ids idea-id))
      (swap! d/state update :ideas merge {idea-id idea}))))

(defn add-idea-req
  []
  (let [body (fu/get-f :idea)
        user-id (get-in @d/state [:me :id])
        data {:user-id user-id :idea-name body}]
    (http/post (str (:server cfg) "/api/ideas/upvote") data)
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

(defn toggle-idea-vote-req
  [id]
  (fn []
    (let [voted? (get-in @d/state [:ideas id "voted?"])
          user-id (get-in @d/state [:me :id])
          idea-name (get-in @d/state [:ideas id "name"])
          data {:user-id user-id :idea-name idea-name}]
      (if voted?
        (http/post (str (:server cfg) "/api/ideas/downvote") data)
        (http/post (str (:server cfg) "/api/ideas/upvote") data)))))
