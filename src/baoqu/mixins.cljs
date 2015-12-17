(ns baoqu.mixins
  (:require [baoqu.services.ws :as ws]
            [baoqu.routes :as routes]
            [baoqu.data :as d]))

(defn get-username
  []
  (get-in @d/state [:session :username]))

(def is-authenticated? (comp boolean get-username))

(def secured-mixin
  {:will-mount (fn [own]
                 (let [username (get-username)]
                   (when-not username
                     (routes/go :login)))
                 own)})

(def connect-ws-mixin
  {:will-mount (fn [own]
                 (when (is-authenticated?)
                   (let [websocket (ws/create-ws)]
                     (println "WEBSOCKET CONNECTED  " websocket)
                     (swap! d/state assoc :ws websocket)))
                 own)})
