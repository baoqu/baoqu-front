(ns baoqu.services.event
  (:require [httpurr.client :as http]
            [httpurr.client.xhr :refer [client]]
            [promesa.core :as p]
            [baoqu.data :as d]))

(enable-console-print!)

(defn encode
  [data]
  (js/JSON.stringify (clj->js data)))

(defn join-event []
  (let [username (get-in @d/state [:session :username])
        uri "http://localhost:5050/api/events/1/users"]
    (p/branch (http/send! client
                          {:method :post
                           :url uri
                           :body (encode {:username username})
                           :headers {"content-type" "application/json"}})
              #(println (str "[HTTP-RESPONSE] " (js->clj (js/JSON.parse (:body %)))))
              #(println (str "[HTTP-ERROR] " %)))))
