(ns baoqu.services.ws
  (:require [goog.events :as events]
            [cognitect.transit :as t])
  (:import [goog.net WebSocket]
           [goog.net.WebSocket EventType]
           [goog Uri]))

(defn decode
  [data]
  (let [r (t/reader :json {:handlers {"u" ->UUID}})]
    (t/read r data)))

(defn encode
  [data]
  (let [w (t/writer :json)]
    (t/write w data)))

(defn process-message
  [res]
  (let [message (decode (.-message res))]
    (println "=== MESSAGE ===")
    (println (str "==> " message))))

(defn process-error
  [res]
  (println ("--> ERROR: " res)))

(defn process-closed
  [res]
  (println ("--> CLOSED: " res)))

(defn process-tick
  [res]
  (println ("--> TICK: " res)))

(defn create-ws
  []
  (let [ws (WebSocket. false)
        uri "ws://127.0.0.1:5050/ws"]

    (events/listen ws EventType.MESSAGE process-message)
    (events/listen ws EventType.ERROR process-error)
    (events/listen ws EventType.CLOSED process-closed)
    (events/listen ws EventType.TICK process-tick)

    (.open ws uri)
    ws))
