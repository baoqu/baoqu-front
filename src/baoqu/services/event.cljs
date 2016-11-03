(ns baoqu.services.event
  (:require [baoqu.services.http :as http]
            [promesa.core :as p]
            [baoqu.data :as d]
            [baoqu.utils :refer [->kwrds]]
            [baoqu.config :refer [cfg]]))

(enable-console-print!)

(defn get-event-data
  [event-id]
  (-> (http/get (str (:server cfg) "/api/events/" event-id))
      (p/then (fn [res]
                (let [event (-> (http/decode res) (->kwrds))]
                  (swap! d/state assoc :event event)
                  (http/get (str (:server cfg) "/api/events/" event-id "/circles")))))
      (p/then (fn [res]
                (let [circles (http/decode res)
                      user-id (get-in @d/state [:me :id])]
                  (swap! d/state assoc :circles circles)
                  (http/get (str (:server cfg) "/api/user-circle/" user-id)))))
      (p/then (fn [res]
                (let [my-circle (http/decode res)
                      circle-id (get my-circle "id")
                      user-id (get-in @d/state [:me :id])]
                  (swap! d/state assoc :circle my-circle)
                  (http/get (str (:server cfg) "/api/circles/" circle-id "/ideas?user-id=" user-id)))))
      (p/then (fn [res]
                (let [ideas (http/decode res)
                      circle-id (get-in @d/state [:circle "id"])
                      ideas-map (into {} (for [idea ideas]
                                           [(get idea "id") idea]))]
                  (swap! d/state assoc :ideas ideas-map)
                  (http/get (str (:server cfg) "/api/circles/" circle-id "/comments")))))
      (p/then (fn [res]
                (let [comments (http/decode res)]
                  (swap! d/state assoc :comments comments))))))

(defn join-event
  [event-id username]
  (let [uri (str (:server cfg) "/api/events/" event-id "/users")]
    (-> (http/post uri {:name username})
        (p/then (fn [res]
                  (let [me (-> (http/decode res) (->kwrds))]
                    (swap! d/state assoc :me me)
                    (get-event-data event-id))))
        (p/catch #(println (str "[HTTP-ERROR]>> " %))))))

(defn reload-event-data
  []
  (let [event-id (get-in @d/state [:event :id])]
    (get-event-data event-id)))
