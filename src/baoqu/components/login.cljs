(ns baoqu.components.login
  (:require [rum.core :as rum]
            [httpurr.client :as http]
            [httpurr.client.xhr :refer [client]]
            [httpurr.client.xhr :as hc]
            [baoqu.data :as d]
            [baoqu.routes :as routes]
            [baoqu.form-utils :as fu]))

(defn login-action []
  (let [username (get-in @d/state [:form :username])]

    ;; hardcoded
    (swap! d/state assoc-in [:session :username] username)
    (routes/go :home)
    (fu/empty-form)))

(rum/defc main < rum/reactive
  []
  (let [state (rum/react d/state)
        username (get-in state [:form :username])]
    [:div.login-wrapper
     [:div.login
      [:h1 "Baoqu"]
      [:h3 "Evento"]
      [:h2 "¿Qué tipo de muerte dolorosa queremos para los ciclistas de La Guindalera?"]
      [:div.input-box
       [:input {:class "bt" :placeholder "Introduce tu nombre"
                :on-change (fu/change-in-form :username)
                :value username}]
       [:button {:on-click login-action} "Participar"]]]]))
