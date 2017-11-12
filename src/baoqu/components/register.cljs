(ns baoqu.components.register
  (:require [rum.core :as rum]
            [promesa.core :as p]
            [baoqu.data :as d]
            [baoqu.form-utils :as fu]
            [baoqu.routes :as routes]
            [baoqu.services.user :as us]))

(enable-console-print!)

(defn register-action
  [e]
  (.preventDefault e)
  (let [{:keys [username password password2]} (fu/get-form)]
    (if-not (= password password2)
      (js/alert "Passwords do not match")
      (-> (us/register username password)
          (p/then (fn [_]
                    (js/alert "Registration complete!")
                    (routes/go :login)
                    (fu/empty-form)))
          (p/catch (fn [err]
                     (js/alert (str "ERROR: " (.-message err)))))))))

(rum/defc main < rum/reactive
  []
  (let [state (rum/react d/state)
        {:keys [username password password2]} (fu/get-form)]
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
      [:form.input-box {:on-submit register-action}
       [:input {:class "bt input-text" :placeholder "Nombre de usuario" :required ""
                :on-change (fu/change-in-form :username)
                :value (or username "")}]
       [:input {:class "bt input-text" :placeholder "Contraseña" :type "password" :required ""
                :on-change (fu/change-in-form :password)
                :value (or password "")}]
       [:input {:class "bt input-text" :placeholder "Contraseña de nuevo" :type "password" :required ""
                :on-change (fu/change-in-form :password2)
                :value (or password2 "")}]
       [:button "Registrarse"]
       [:a.bottom-link {:href "/#/login"} "¿Ya tienes cuenta? Entra"]
       ]
      ]
     ]))
