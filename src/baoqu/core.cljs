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

(defn empty-form []
  (swap! state assoc :form nil))

(defn login-action []
  (let [username (get-in @state [:form :username])]

    ;; request /login

    ;; if (ok)
    (swap! state assoc-in [:session :username] username)
    (empty-form)

    ;; if (error)
    (swap! state assoc-in [:errors :username] error-message)
    ))



(defn change-field [& path]
  (fn [e]
    (let [new-value (-> e (.-target) (.-value))]
      (enable-console-print!)
      (println "====================")
      (println new-value)
      (println "====================")
      (swap! state assoc-in path new-value))))

(def change-in-form (partial change-field :form))

(rum/defc login < rum/reactive
  []
  (let [state (rum/react state)
        username (get-in state [:form :username])]
    [:div
     [:h2 "Login"]
     [:input {:class "bt" :placeholder "login input"
              :on-change (change-in-form :username)
              :value username}]
     [:button {:on-click login-action} "login"]]))

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
