(ns baoqu.core
  (:require [rum.core :as rum]
            [goog.dom :as dom]
            [bidi.router :as bidi]
            [httpurr.client :as http]
            [httpurr.client.xhr :refer client]
            [httpurr.client.xhr :as hc]
            [baoqu.components.home :as home-c]))

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
    (swap! state assoc-in [:session :username] username)
    (empty-form)

    ;; if (error)
    ;;   (swap! state assoc-in [:errors :username] error-message)
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


(defn join-event []
  (http/send! client
              {:method :get
               :url    "https://api.github.com/orgs/funcool"})

  (hc/get "https://api.github.com/orgs/funcool")
  )

(rum/defc home < rum/reactive
  []
  [:div
   [:h2 "Home"]
   [:button#join-event {:on-click join-event} "Join Event"]
    ])

(rum/defc base < rum/reactive
  []
  (let [state (rum/react state)
        username (get-in state [:session :username])]
    (if username
      (home-c/main)
      (login))))

;; MOUNT
(rum/mount (base) (dom/getElement "content"))
