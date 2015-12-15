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

(rum/defc login < rum/reactive
  []
  [:h2 "Login"])

(rum/defc home < rum/reactive
  []
  [:h2 "Home"])

(rum/defc base < rum/reactive
  []
  (let [state (rum/react state)
        username (get-in state [:session :username])]
    (if username
      (home)
      (login))))

;; MOUNT
(rum/mount (base) (dom/getElement "content"))
