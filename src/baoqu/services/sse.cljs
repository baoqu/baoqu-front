(ns baoqu.services.sse
  (:require [baoqu.config :refer [cfg]]
            [baoqu.data :as d]
            [baoqu.services.idea :as is]
            [baoqu.services.comment :as cs]
            [baoqu.services.event :as es]
            [baoqu.services.notification :as ns]))

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
        current-circle-id (get-in @d/state [:circle "id"])
        message-circle-id (get data "circle-id")
        milis (get data "date")
        date (str (js/Date. milis))
        new-data (assoc data "date" date)]
    (if (= current-circle-id message-circle-id)
      (do
        (cs/react-to-comment new-data)
        (println "[SSE] COMMENT > " new-data))
      (println "COMMENT NOT FOR ME"))))

(defmethod process-message :upvote
  [msg]
  (let [data (:data msg)
        current-circle-id (get-in @d/state [:circle "id"])
        message-circle-id (get data "circle-id")
        idea-id (get-in data ["idea" "id"])
        user-id (get-in data ["user" "id"])]
    (if (= current-circle-id message-circle-id)
      (do
        (is/add-idea-if-new (get data "idea"))
        (is/react-to-upvote idea-id user-id)
        (println "[SSE] UPVOTE > " data)))
    (println "UPVOTE NOT FOR ME")))

(defmethod process-message :downvote
  [msg]
  (let [data (:data msg)
        current-circle-id (get-in @d/state [:circle "id"])
        message-circle-id (get data "circle-id")
        idea-id (get-in data ["idea" "id"])
        user-id (get-in data ["user" "id"])]
    (if (= current-circle-id message-circle-id)
      (do
        (is/react-to-downvote idea-id user-id)
        (println "[SSE] DOWNVOTE > " data))
      (println "UPVOTE NOT FOR ME"))))

(defmethod process-message :notification
  [msg]
  (let [data (:data msg)
        current-circle-id (get-in @d/state [:circle "id"])
        message-circle-id (get data "circle-id")
        title (get data "title")
        description (get data "description")
        notification {:title title
                      :description description
                      :type :modal}]
    (if (= current-circle-id message-circle-id)
      (do
        (ns/set-notification notification)
        (println "[SSE] NOTIFICATION> (" title ") " description))
      (println "NOTIFICATION NOT FOR ME"))))

(defmethod process-message :grow
  [msg]
  (es/reload-event-data))

(defmethod process-message :shrink
  [msg]
  (es/reload-event-data))

(defmethod process-message :new-user
  [msg]
  (es/reload-event-data))

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
