(ns baoqu.mixins
  (:require [baoqu.services.sse :as sse]
            [baoqu.routes :as routes]
            [baoqu.data :as d]
            [baoqu.services.security :as sec]))

(def secured-mixin
  {:will-mount (fn [own]
                 (let [username (sec/get-username)]
                   (when-not username
                     (routes/go :login)))
                 own)})

(def connect-see-mixin
  {:will-mount (fn [own]
                 (when (sec/is-authenticated?)
                   (let [sse (sse/create-sse)]
                     (println "[SSE] CONNECTED > " sse)
                     (swap! d/state assoc :sse sse)))
                 own)})
