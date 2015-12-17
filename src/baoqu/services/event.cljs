(ns baoqu.services.event
  (:require [httpurr.client.xhr :as http]
            [promesa.core :as p]
            [baoqu.data :as d]))

(enable-console-print!)

(defn encode
  [request]
  (update request :body #(js/JSON.stringify (clj->js %))))

(defn join-event []
  (let [username (get-in @d/state [:session :username])
        uri "http://localhost:5050/api/events/1/users"]
    (p/branch (http/post uri (encode {:body {:username username}}))
              #(println (js->clj (js/JSON.parse (:body %))))
              #(println %))))
