(ns baoqu.services.comment
  (:require [baoqu.data :as d]
            [baoqu.form-utils :as fu]
            [baoqu.api :as api]))

(enable-console-print!)

(defn react-to-comment
  [comment]
  (swap! d/state update :comments merge comment))

(defn add-comment-req
  [body]
  (let [name (get-in @d/state [:me :name])
        circle-id (get-in @d/state [:circle "id"])]
    (if-not (= body "")
      (do
        (api/create-comment circle-id name body)
        (fu/empty-f :comment)))))
