(ns baoqu.services.sse
  (:require [baoqu.config :refer [cfg]]
            [baoqu.data :as d]
            [baoqu.services.idea :as is]
            [baoqu.services.comment :as cs]
            [baoqu.services.event :as es]
            [baoqu.repos.notification :as nr]))

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

(defn end-if-idea-at-9
  [idea]
  (let [ideas (:ideas @d/state)
        ideas-as-list (into [] (map #(second %) ideas))
        any-at-9? (some #(= 9 (get % "votes")) ideas-as-list)
        notification {:title "Se ha llegado a un consenso"
                      :description (str "La idea \"" idea "\" ha sido apoyada por todos los usuarios. El evento ha terminado.")
                      :type :modal}]
    (if any-at-9?
      (do
        (println "TERMINAMOS")
        (nr/set-notification notification))
      (println "AUN NO"))))

(defmethod process-message :upvote
  [{:keys [data]}]
  (println ">> UPVOTE")
  (println data "")
  (let [current-circle-id (get-in @d/state [:circle "id"])
        message-circle-id (get data "circle-id")
        idea-id (get-in data ["idea" "id"])
        user-id (get-in data ["user" "id"])]
    (if (= current-circle-id message-circle-id)
      (do
        (is/add-idea-if-new (get data "idea"))
        (is/react-to-upvote idea-id user-id)
        (println "[SSE] UPVOTE > " data))
      (println "UPVOTE NOT FOR ME"))

    (.setTimeout js/window (partial end-if-idea-at-9 (get-in data ["idea" "name"])) 2000)))

(defmethod process-message :downvote
  [{:keys [data]}]
  (println ">> DOWNVOTE")
  (println data "")
  (let [current-circle-id (get-in @d/state [:circle "id"])
        message-circle-id (get data "circle-id")
        idea-id (get-in data ["idea" "id"])
        user-id (get-in data ["user" "id"])]
    (if (= current-circle-id message-circle-id)
      (do
        (is/react-to-downvote idea-id user-id)
        (println "[SSE] DOWNVOTE > " data))
      (println "DOWNVOTE NOT FOR ME"))))

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
        (nr/set-notification notification)
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
