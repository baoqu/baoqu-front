(ns baoqu.routes
  (:require [bidi.router :as bidi]
            [baoqu.data :refer [state]]))

(def routes ["/" [["home" :home]
                  ["index" :index]]])

(defn- on-navigate
  [{route :handler}]
  (swap! state assoc :route route))

(defonce +router+
  (bidi/start-router! routes {:on-navigate on-navigate
                              :default-location {:handler :home}}))
