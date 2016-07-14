(ns baoqu.services.event
  (:require [baoqu.services.http :as http]
            [promesa.core :as p]
            [baoqu.data :as d]
            [baoqu.utils :refer [->kwrds]]))

(enable-console-print!)

(defn join-event
  [event-id username]
  (let [uri (str "http://localhost:3030/api/events/" event-id "/users")]
    (p/branch (http/post uri {:name username})
              (fn [res]
                (->> res
                     (http/decode)
                     (->kwrds)
                     (swap! d/state assoc :me)))
              #(println (str "[HTTP-ERROR]>> " %)))))
