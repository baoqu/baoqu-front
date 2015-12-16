(ns baoqu.services.event
  (:require [httpurr.client.xhr :as http]
            [promesa.core :as p]))

(defn join-event []
  (p/then
   (http/get "https://api.github.com/orgs/funcool")
   (fn [res]
     (println (js->clj (js/JSON.parse (:body res)))))))
