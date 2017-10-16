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
  ; start login
   [:div.login.login-general
    [:div.event-info
      [:h2 "Entra en Baoqu"]
      [:p ""]
    ]
    [:form.input-box
     [:input {:class "bt input-text" :placeholder "Nombre de usuario" :required ""}]
     [:input {:class "bt input-text" :placeholder "Contraseña" :type "password" :required ""}]
     [:button "Entrar"]
     [:a.bottom-link {:href "#register"} "¿No tienes cuenta? Regístrate"]
    ]]
    ; end login

    ; start registro
     [:div.login.login-general
      [:div.event-info
        [:h2 "Regístrate en Baoqu"]
        [:p "Te enviaremos un email con instrucciones"]
      ]
      [:form.input-box
       [:input {:class "bt input-text" :placeholder "Nombre de usuario" :required ""}]
       [:input {:class "bt input-text" :placeholder "Email" :type "email" :required ""}]
       [:button "Registrarse"]
       [:a.bottom-link {:href "#login"} "¿Ya tienes cuenta? Entra"]
       ]]
       ; end registro

     ; start reset
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
        ]]
        ; end reset

     ])
