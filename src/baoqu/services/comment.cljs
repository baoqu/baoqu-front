(ns baoqu.services.comment
  (:require [baoqu.data :as d]
            [baoqu.repos.comment :as cr]
            [baoqu.form-utils :as fu]
            [baoqu.repos.user :as ur]
            [baoqu.api :as api]))

(enable-console-print!)

(defn react-to-comment
  [comment]
  (swap! d/state update :comments merge comment))

(defn add-comment-req
  [body]
  (let [{name :name} (ur/get-me)
        {circle-id :id} (ur/get-active-circle)]
    (if-not (= body "")
      (do
        (api/create-comment circle-id name body)
        (fu/empty-f :comment)))))

(defn get-all-for-circle
  [{:keys [id]}]
  (cr/get-all-for-circle id))

(defn count-all-for-circle
  [circle]
  (count (get-all-for-circle circle)))

(defn add-comment
  [comment]
  (cr/add-comment comment))
