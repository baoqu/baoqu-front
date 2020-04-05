(ns baoqu.services.sse
  (:require [clojure.walk :refer [keywordize-keys]]
            [baoqu.config :as cfg]
            [baoqu.data :as d]
            [baoqu.services.idea :as is]
            [baoqu.services.comment :as cs]
            [baoqu.services.circle :as cis]
            [baoqu.services.event :as es]
            [baoqu.repos.user :as ur]
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

(defn should-send-notification-for-comment
  [comment]
  (let [my-circle? (cis/is-my-circle? {:id (:circle-id comment)})
        window-has-focus? (.hasFocus js/document)
        my-comment? (= (:user-id comment) (:id (ur/get-me)))]
    (and my-circle? (not my-comment?) (not window-has-focus?))))

(defmethod process-message :comment
  [{:keys [data]}]
  (println ">> COMMENT")
  (println data)

  (let [comment (keywordize-keys data)
        current-circle-id (get-in @d/state [:circle "id"])]
    (cs/add-comment comment)
    (if (should-send-notification-for-comment comment)
      (let [body (:body comment)
            username (:username comment)]
        (js/Notification. (str username ": " body))))))

(defn end-if-idea-at-27
  [idea]
  (let [ideas (:ideas @d/state)
        ideas-as-list (into [] (map #(second %) ideas))
        any-at-27? (some #(= 27 (get % "votes")) ideas-as-list)
        notification {:title "Se ha llegado a un consenso"
                      :description (str "La idea \"" idea "\" ha sido apoyada por todos los usuarios. El evento ha terminado.")
                      :type :modal}]
    (if (and any-at-27? (not (nr/get-notification)))
      (do
        (println "TERMINAMOS")
        (nr/set-notification notification))
      (println "AUN NO"))))

(defmethod process-message :upvote
  [{:keys [data]}]
  (println ">> UPVOTE")
  (println data)

  (let [{:keys [idea user]} (keywordize-keys data)]
    ;; Find idea, if not exists, create
    (is/add-idea-if-new idea)
    (is/add-vote (:id user) (:id idea))

    (.setTimeout js/window (partial end-if-idea-at-27 (get-in data ["idea" "name"])) 2000)))

(defmethod process-message :downvote
  [{:keys [data]}]
  (println ">> DOWNVOTE")
  (println data)

  ;; Remove vote
  (let [{:keys [idea user]} (keywordize-keys data)]
    (is/remove-vote (:id user) (:id idea))))

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
   (js/EventSource. (str cfg/url "/sse"))
   (add-listeners)))
