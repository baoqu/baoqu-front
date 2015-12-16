(ns baoqu.routes
  (:require [bidi.router :as bidi]
            [baoqu.data :as d]))

(enable-console-print!)

(def routes ["/" [["home" :home]
                  ["login" :login]]])

(defn- on-navigate
  [{route :handler}]
  (println "ON NAVIGATE: " route)
  (swap! d/state assoc :route route))

(defonce +router+
  (bidi/start-router! routes {:on-navigate on-navigate
                              :default-location {:handler :home}}))

(defn go
  ([name]
   (go name nil))
  ([name params]
   (let [loc (merge {:handler name}
                    (when params
                      {:route-params params}))]
     (bidi/set-location! +router+ loc))))
