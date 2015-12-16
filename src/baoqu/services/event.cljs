(ns baoqu.services.event
  (:require [httpurr.client.xhr :as http]
            [promesa.core :as p]))

(defn encode
  [request]
  (update request :body #(js/JSON.stringify (clj->js %))))

(defn join-event []
  (p/branch (http/post "http://localhost:5050/api/events/1/users" (encode {:body {:user_id 123}}))
            (fn [res]
              (println (js->clj (js/JSON.parse (:body res)))))
            (fn [err]
              (println err))))
