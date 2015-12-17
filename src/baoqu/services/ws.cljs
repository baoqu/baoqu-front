(ns baoqu.services.ws
  (:require [goog.events :as events]
            [cognitect.transit :as t]
            [baoqu.repos.event :as event-repo])
  (:import [goog.net WebSocket]
           [goog.net.WebSocket EventType]
           [goog Uri]))

(enable-console-print!)

(defn decode
  [data]
  (let [r (t/reader :json {:handlers {"u" ->UUID}})]
    (t/read r data)))

(defn response->message
  [res]
  (decode (.-message res)))

(defn encode
  [data]
  (let [w (t/writer :json)]
    (t/write w data)))

(defn process-error
  [res]
  (println "--> ERROR: " res))

(defn process-closed
  [res]
  (println "--> CLOSED: " res))

(defn process-tick
  [res]
  (println "--> TICK: " res))

(defmulti process-message
  (fn [message]
    (:topic message)))

(defmethod process-message :events/create-circle
  [message]
  (js/alert message))

(defmethod process-message :events/join-user
  [message]
  (js/alert message))

(defmethod process-message :events/add-participant
  [message]
  (js/alert message))

(defmethod process-message :events/status
  [message]
  (event-repo/load-event-status (:payload message)))

(defmethod process-message :default
  [message]
  (println "-> Mensaje inválido: " message))

(def process-decoded-message (comp process-message response->message))

(defn create-ws
  []
  (let [ws (WebSocket. false)
        uri "ws://127.0.0.1:5050/ws"]
    (events/listen ws EventType.MESSAGE process-decoded-message)
    (events/listen ws EventType.ERROR process-error)
    (events/listen ws EventType.CLOSED process-closed)
    (events/listen ws EventType.TICK process-tick)
    (.open ws uri)
    ws))
