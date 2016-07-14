(ns baoqu.services.comment
  (:require [baoqu.data :as d]
            [baoqu.form-utils :as fu]
            [baoqu.services.http :as http]))

(defn add-comment-req
  [username circle-id body]
  (let [url (str "http://localhost:3030/api/circles/" circle-id "/comments")
        data {:name username :comment-body body}]
    (http/post url data)))

(defn add-comment
  []
  (let [username (get-in @d/state [:session :username])
        circle-id (:circle @d/state)
        author (first (filter #(= username (:name %)) (:participants @d/state)))
        body (fu/get-f :comment)
        new-comment {:id "x" :body body :author (:id author) :date "xy"}]
    (add-comment-req username circle-id body)
    (swap! d/state update :comments conj new-comment)
    (fu/empty-f :comment)))
