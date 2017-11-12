(ns baoqu.components.register
  (:require [rum.core :as rum]))

(enable-console-print!)

(rum/defc main
  []
  [:div.login-wrapper
   [:div#mainHeader
    [:div.logo-icon]
    [:h1.logo "Baoqu"]
    ]
   [:div.login.login-general
    [:div.event-info
     [:h2 "Regístrate en Baoqu"]
     [:p "Te enviaremos un email con instrucciones"]
     ]
    [:form.input-box
     [:input {:class "bt input-text" :placeholder "Nombre de usuario" :required ""}]
     [:input {:class "bt input-text" :placeholder "Email" :type "email" :required ""}]
     [:button "Registrarse"]
     [:a.bottom-link {:href "/#/login"} "¿Ya tienes cuenta? Entra"]
     ]
    ]
   ])
