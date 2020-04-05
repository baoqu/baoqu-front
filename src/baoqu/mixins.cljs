(ns baoqu.mixins
  (:require [rum.core :as rum]
            [promesa.core :as p]
            [baoqu.services.sse :as sse]
            [baoqu.routes :as routes]
            [baoqu.data :as d]
            [baoqu.services.security :as sec]
            [baoqu.repos.notification :as nr]))

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
                 own)
   :did-mount (fn [own]
                (if (= (.-permission js/Notification) "granted")
                  (nr/set-permission)
                  (-> (.requestPermission js/Notification)
                      (p/then (fn [permission]
                                (if (= permission "granted")
                                  (nr/set-permission))))))
                own)})

(def scroll-on-insert
  {:did-update
   (fn [state]
     (let [node (rum/dom-node state)
           scrollHeight (.-scrollHeight node)]
       (set! (.-scrollTop node) scrollHeight)
       state))
   })
