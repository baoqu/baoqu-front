(ns baoqu.services.comment
  (:require [baoqu.data :as d]
            [baoqu.form-utils :as fu]
            [baoqu.services.http :as http]
            [baoqu.config :refer [cfg]]))

(defn react-to-comment
  [comment]
  (swap! d/state update :comments merge comment))

(defn add-comment-req
  []
  (let [name (get-in @d/state [:me :name])
        circle-id (get-in @d/state [:circle "id"])
        body (fu/get-f :comment)
        data {:name name :comment-body body}]
    (http/post (str (:server cfg) "/api/circles/" circle-id "/comments") data)
    (fu/empty-f :comment)))
