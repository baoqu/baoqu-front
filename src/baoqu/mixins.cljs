(ns baoqu.mixins
  (:require [baoqu.services.sse :as sse]
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

(def connect-see-mixin
  {:will-mount (fn [own]
                 (when (is-authenticated?)
                   (let [sse (sse/create-sse)]
                     (println "[SSE] CONNECTED > " sse)
                     (swap! d/state assoc :sse sse)))
                 own)})
