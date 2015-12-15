(ns baoqu.core
  (:require [rum.core :as rum]
            [goog.dom :as dom]
            [bidi.router :as bidi]))

(defonce state (atom {}))

;; ROUTES
(def routes ["/" [["home" :home]
                  ["index" :index]]])

(defn- on-navigate
  [{route :handler}]
  (swap! state assoc :route route))

(defonce +router+
  (bidi/start-router! routes {:on-navigate on-navigate
                              :default-location {:handler :home}}))

(rum/defc base < rum/reactive
  []
  [:h1 "Hello world"])

;; MOUNT
(rum/mount (base) (dom/getElement "content"))
