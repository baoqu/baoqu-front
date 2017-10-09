(ns baoqu.components.fakelogin
  (:require [rum.core :as rum]))

(enable-console-print!)

(rum/defc main
  []
  ; [:div [:h1 "LOGIN!!"]
  ;  [:p "aquí irían el form, los inputs y el button... dejo rienda suelta a tu creatividad :)"]])
  [:div.login-wrapper
    [:div#mainHeader
      [:div.logo-icon]
      [:h1.logo "Baoqu"]
    ]
   [:div.login.login-general
    [:div.event-info
      [:h2 "Entra en Baoqu"]
      [:p ""]
    ]
    [:form.input-box
     [:input {:class "bt input-text" :placeholder "Nombre de usuario"}]
     [:input {:class "bt input-text" :placeholder "Contraseña"}]
     [:button "Entrar"]
     [:a.bottom-link {:href "#register"} "¿No tienes cuenta? Regístrate"]]]])
