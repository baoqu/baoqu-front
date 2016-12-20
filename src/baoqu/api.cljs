(ns baoqu.api
  (:require [baoqu.http :as http]
            [promesa.core :as p]
            [baoqu.utils :refer [->url]]))

(enable-console-print!)

;;--------------------
;; EVENT
;;--------------------
(defn join-event
  [event-id username]
  (let [path (str "/api/events/" event-id "/users")
        url (->url path)
        body {:name username}]
    (http/post url body)))

(defn get-event
  [event-id]
  (let [path (str "/api/events/" event-id)
        url (->url path)]
    (http/get url)))

;;--------------------
;; CIRCLE
;;--------------------
(defn get-user-circle
  [user-id]
  (let [path (str "/api/user-circle/" user-id)
        url (->url path)]
    (http/get url)))

(defn get-circle-ideas-for-user
  [circle-id user-id]
  (let [params {:user-id user-id}
        path (str "/api/circles/" circle-id "/ideas")
        url (->url path params)]
    (http/get url)))

(defn get-comments-for-circle
  [circle-id]
  (let [path (str "/api/circles/" circle-id "/comments")
        url (->url path)]
    (http/get url)))

(defn get-event-circles
  [event-id]
  (let [path (str "/api/events/" event-id "/circles")
        url (->url path)]
    (http/get url)))

;;--------------------
;; IDEA
;;--------------------
(defn upvote-idea
  [user-id body]
  (let [url (->url "/api/ideas/upvote")
        data {:user-id user-id :idea-name body}]
    (http/post url data)))

(defn downvote-idea
  [user-id body]
  (let [url (->url "/api/ideas/downvote")
        data {:user-id user-id :idea-name body}]
    (http/post url data)))

;;--------------------
;; COMMENT
;;--------------------
(defn create-comment
  [circle-id name body]
  (let [path (str "/api/circles/" circle-id "/comments")
        url (->url path)
        data {:name name :comment-body body}]
    (http/post url data)))
