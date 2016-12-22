(ns baoqu.mixins
  (:require [rum.core :as rum]
            [baoqu.services.sse :as sse]
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
                   (if-let [old-sse (:sse @d/state)]
                     (do
                       (println "[SSE] Old connection found, destroying")
                       (.close old-sse)
                       (println "[SSE] Old connection destroyed")))
                   (let [sse (sse/create-sse)]
                     (println "[SSE] Connection established")
                     (swap! d/state assoc :sse sse)))
                 own)})

(def scroll-on-insert
  {:did-update
   (fn [state]
     (let [node (rum/dom-node state)
           scrollHeight (.-scrollHeight node)]
       (set! (.-scrollTop node) scrollHeight)
       state))
   })
