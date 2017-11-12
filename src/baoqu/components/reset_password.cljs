(ns baoqu.components.reset-password
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
     [:h2 "Crea tu contraseña"]
     [:p ""]
     ]
    [:form.input-box
     [:input {:class "bt input-text" :placeholder "Contraseña" :type "password" :required ""}]
     [:input {:class "bt input-text" :placeholder "Repite la contraseña" :type "password" :required ""}]
     [:button "Registrarse"]
     [:a.bottom-link {:href "#login"} "¿Ya tienes cuenta? Log in"]
     ]
    ]
   ])
