(ns baoqu.services.sse
  (:require [clojure.walk :refer [keywordize-keys]]
            [baoqu.config :refer [cfg]]
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
  [{:keys [data]}]
  (println ">> COMMENT")
  (println data)

  (let [comment (keywordize-keys data)]
    (cs/add-comment comment)))

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
  (println data)

  (let [{:keys [idea user]} (keywordize-keys data)]
    ;; Find idea, if not exists, create
    (is/add-idea-if-new idea)

    ;; Check that vote doesn't exist, if not:
    (if-not (is/voted? idea)
      ;; Add vote to the votes list
      (do
        (is/add-vote (:id idea))
        (.setTimeout js/window (partial end-if-idea-at-9 (get-in data ["idea" "name"])) 2000)))))

(defmethod process-message :downvote
  [{:keys [data]}]
  (println ">> DOWNVOTE")
  (println data)

  ;; Remove vote
  (let [{:keys [idea user]} (keywordize-keys data)]
    (is/remove-vote (:id idea))))
    ;; Check idea votes, if 0, remove

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
