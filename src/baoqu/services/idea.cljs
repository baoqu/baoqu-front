(ns baoqu.services.idea
  (:require [baoqu.data :as d]
            [baoqu.form-utils :as fu]
            [baoqu.api :as api]))

(enable-console-print!)

(defn idea-exists?
  [idea-body]
  (let [ideas (:ideas @d/state)]
    (some #(= (get (second %) "name") idea-body) ideas)))

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
        user-id (get-in @d/state [:me :id])]
    (if-not (empty? body)
      (do
        (if-not (idea-exists? body)
          (api/upvote-idea user-id body))
        (fu/empty-f :idea)))))

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
          idea-name (get-in @d/state [:ideas id "name"])]
      (if voted?
        (api/upvote-idea user-id idea-name)
        (api/downvote-idea user-id idea-name)))))
