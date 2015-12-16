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

(defn create-ws
  []
  (let [ws (WebSocket. false)
        uri "ws://127.0.0.1:5050/ws"]

    (events/listen ws EventType.MESSAGE #(println (decode (.-message %))))
    (events/listen ws EventType.ERROR #(println %))
    (events/listen ws EventType.CLOSED #(println %))
    (events/listen ws EventType.TICK #(println %))

    (.open ws uri)
    ws))
