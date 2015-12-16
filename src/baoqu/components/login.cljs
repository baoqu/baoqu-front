(ns baoqu.components.login
  (:require [rum.core :as rum]
            [httpurr.client :as http]
            [httpurr.client.xhr :refer [client]]
            [httpurr.client.xhr :as hc]
            [baoqu.data :refer [state]]))

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

(defn join-event []
  (http/send! client
              {:method :get
               :url    "https://api.github.com/orgs/funcool"})

  (hc/get "https://api.github.com/orgs/funcool"))

(rum/defc main < rum/reactive
  []
  (let [state (rum/react state)
        username (get-in state [:form :username])]
    [:div.login-wrapper
     [:div.login
      [:h1 "Baoqu"]
      [:h3 "Evento"]
      [:h2 "¿Qué tipo de muerte dolorosa queremos para los ciclistas de La Guindalera?"]
      [:div.input-box
       [:input {:class "bt" :placeholder "Introduce tu nombre"
                :on-change (change-in-form :username)
                :value username}]
       [:button {:on-click login-action} "Participar"]]]]))
