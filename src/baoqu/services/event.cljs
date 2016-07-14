(ns baoqu.services.event
  (:require [baoqu.services.http :as http]
            [promesa.core :as p]
            [baoqu.data :as d]
            [baoqu.utils :refer [->kwrds]]))

(enable-console-print!)

(defn join-event
  [event-id username]
  (let [uri (str "http://localhost:3030/api/events/" event-id "/users")]
    (-> (http/post uri {:name username})
        (p/then (fn [res]
                  (let [me (-> (http/decode res) (->kwrds))]
                    (swap! d/state assoc :me me)
                    (http/get (str "http://localhost:3030/api/events/" event-id)))))
        (p/then (fn [res]
                  (let [event (-> (http/decode res) (->kwrds))]
                    (swap! d/state assoc :event event)
                    (http/get (str "http://localhost:3030/api/events/" event-id "/circles")))))
        (p/then (fn [res]
                  (let [circles (http/decode res)]
                    (swap! d/state assoc :circles circles))))
        (p/catch #(println (str "[HTTP-ERROR]>> " %))))))
