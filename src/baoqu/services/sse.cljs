(ns baoqu.services.sse
  (:require [baoqu.config :refer [cfg]]
            [baoqu.services.idea :as is]
            [baoqu.services.comment :as cs]))

(enable-console-print!)

(defn keys->keywords
  [m]
  (into {}
        (for [[k v] m]
          [(keyword k) v])))

(defn get-payload
  [msg]
  (->> msg
       (.-data)
       (.parse js/JSON)
       (js->clj)
       (keys->keywords)))

(defn process-error
  [msg]
  (println "[SSE] ERROR")
  (js/console.log msg))

(defmulti process-message
  (fn [msg]
    (keyword (:type msg))))

(defmethod process-message :comment
  [msg]
  (let [data (:data msg)
        milis (get data "date")
        date (str (js/Date. milis))
        new-data (assoc data "date" date)]
    (cs/react-to-comment new-data)
    (println "[SSE] COMMENT > " new-data)))

(defmethod process-message :upvote
  [msg]
  (let [data (:data msg)
        idea-id (get-in data ["idea" "id"])
        user-id (get-in data ["user" "id"])]
    (is/add-idea-if-new (get data "idea"))
    (is/react-to-upvote idea-id user-id)
    (println "[SSE] UPVOTE > " data)))

(defmethod process-message :downvote
  [msg]
  (let [data (:data msg)
        idea-id (get-in data ["idea" "id"])
        user-id (get-in data ["user" "id"])]
    (is/react-to-downvote idea-id user-id)
    (println "[SSE] DOWNVOTE > " data)))

(defmethod process-message :default
  [msg]
  (println "[SSE] DEFAULT MESSAGE > " msg))

(defn add-listeners
  [sse]
  (.addEventListener sse "error" process-error)
  (.addEventListener sse "message" (comp process-message get-payload))
  sse)

(defn create-sse
  "Connects the frontend with the backend sse. Returns the SSE connection"
  []
  (->>
   (js/EventSource. (str (:server cfg) "/sse"))
   (add-listeners)))
