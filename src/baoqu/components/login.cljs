(ns baoqu.components.login
  (:require [rum.core :as rum]
            [httpurr.client :as http]
            [httpurr.client.xhr :refer [client]]
            [httpurr.client.xhr :as hc]
            [baoqu.data :refer [state]]
            [baoqu.routes :as routes]))

(defn empty-form []
  (swap! state assoc :form nil))

(defn login-action []
  (let [username (get-in @state [:form :username])]

    ;; hardcoded
    (swap! state assoc-in [:session :username] username)
    (routes/go :home)
    (empty-form)))

(defn change-field [& path]
  (fn [e]
    (let [new-value (-> e (.-target) (.-value))]
      (swap! state assoc-in path new-value))))

(def change-in-form (partial change-field :form))

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
