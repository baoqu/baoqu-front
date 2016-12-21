(ns baoqu.services.comment
  (:require [baoqu.data :as d]
            [baoqu.repos.comment :as comment-r]
            [baoqu.form-utils :as fu]
            [baoqu.repos.user :as user-r]
            [baoqu.api :as api]))

(enable-console-print!)

(defn react-to-comment
  [comment]
  (swap! d/state update :comments merge comment))

(defn add-comment-req
  [body]
  (let [{name :name} (user-r/get-me)
        {circle-id :id} (user-r/get-active-circle)]
    (if-not (= body "")
      (do
        (api/create-comment circle-id name body)
        (fu/empty-f :comment)))))

(defn get-all-for-circle
  [id]
  (comment-r/get-all-for-circle id))
