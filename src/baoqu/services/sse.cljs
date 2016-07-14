(ns baoqu.services.sse)

(enable-console-print!)

(def sse-url "http://localhost:3030/sse")

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
    (println "[SSE] COMMENT > " new-data)))

(defmethod process-message :new-idea
  [msg]
  (->> msg
       (:data)
       (println "[SSE] NEW-IDEA > ")))

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
   (js-obj "withCredentials" true)
   (js/EventSource. sse-url)
   (add-listeners)))
