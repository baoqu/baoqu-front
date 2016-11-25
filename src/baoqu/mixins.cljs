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
                   (let [sse (sse/create-sse)]
                     (println "[SSE] CONNECTED > " sse)
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
