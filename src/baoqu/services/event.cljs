(ns baoqu.services.event
  (:require [baoqu.http :as http]
            [promesa.core :as p]
            [baoqu.data :as d]
            [baoqu.utils :refer [->kwrds]]
            [baoqu.api :as api]))

(enable-console-print!)

(defn res->kwrds
  [res]
  (-> (http/decode res)
      (->kwrds)))

(defn get-event-data
  [event-id]
  (-> (api/get-event event-id)
      (p/then (fn [res]
                (let [event (res->kwrds res)
                      user-id (get-in @d/state [:me :id])]
                  (swap! d/state assoc :event event)
                  (api/get-user-circle user-id))))
      (p/then (fn [res]
                (let [my-circle (http/decode res)
                      circle-id (get my-circle "id")
                      user-id (get-in @d/state [:me :id])]
                  (swap! d/state assoc :circle my-circle)
                  (api/get-circle-ideas-for-user circle-id user-id))))
      (p/then (fn [res]
                (let [ideas (http/decode res)
                      circle-id (get-in @d/state [:circle "id"])
                      ideas-map (into {} (for [idea ideas]
                                           [(get idea "id") idea]))]
                  (swap! d/state assoc :ideas ideas-map)
                  (api/get-comments-for-circle circle-id))))
      (p/then (fn [res]
                (let [comments (http/decode res)]
                  (swap! d/state assoc :comments comments)
                  (api/get-event-circles event-id))))
      (p/then (fn [res]
                (let [circles (http/decode res)]
                  (swap! d/state assoc :circles circles))))))

(defn join-event
  [event-id username]
  (-> (api/join-event event-id username)
      (p/then (fn [res]
                  (let [me (res->kwrds res)]
                    (swap! d/state assoc :me me)
                    (get-event-data event-id))))
        (p/catch #(println (str "[HTTP-ERROR]>> " %)))))

(defn reload-event-data
  []
  (let [event-id (get-in @d/state [:event :id])]
    (get-event-data event-id)))
