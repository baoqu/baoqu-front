(ns baoqu.components.old-login
  (:require [rum.core :as rum]
            [httpurr.client :as http]
            [httpurr.client.xhr :refer [client]]
            [httpurr.client.xhr :as hc]
            [baoqu.data :as d]
            [baoqu.routes :as routes]
            [baoqu.form-utils :as fu]
            [baoqu.services.event :as event-service]))

(defn login-action []
  (let [username (fu/get-f :username)]
    (event-service/join-event 1 username)
    (routes/go :home)
    (fu/empty-form)

    ;; possible approach
    ; (-> (event-service/join-event 1 username) ;; replace 1 with id
    ;     (p/then #(routes/go :home))) ;; reemplazar :home con :event-list? o whatever
   ))

(rum/defc main < rum/reactive
  []
  (let [state (rum/react d/state)
        username (get-in state [:form :username] "")
        submit-action (comp login-action #(.preventDefault %))]
    [:div.login-wrapper
     [:div#mainHeader
      [:div.logo-icon]
      [:h1.logo "Baoqu"]
      ]
     [:div.login.login-evento
      [:div.event-info
       [:h3 "Evento"]
       [:h2 "Probando Baoqu!"]
       [:p ""]
       ]
      [:form.input-box {:on-submit submit-action}
       [:input {:class "bt input-text" :placeholder "Aquí tu nombre"
                :on-change (fu/change-in-form :username)
                :value username}]
       [:button "Participar"]]]]))
