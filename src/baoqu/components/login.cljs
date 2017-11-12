(ns baoqu.components.login
  (:require [rum.core :as rum]
            [promesa.core :as p]
            [baoqu.data :as d]
            [baoqu.form-utils :as fu]
            [baoqu.routes :as routes]
            [baoqu.services.user :as us]
            [baoqu.services.event :as es]))

(enable-console-print!)

(defn login-action
  [e]
  (.preventDefault e)
  (let [{:keys [username password]} (fu/get-form)]
    (-> (us/login username password)
        (p/then es/fetch-events)
        (p/then (fn [_]
                  (routes/go :events)
                  (fu/empty-form)))
        (p/catch (fn [err]
                   (js/alert (str "ERROR: " (.-message err))))))))

(rum/defc main < rum/reactive
  []
  (let [state (rum/react d/state)
        {:keys [username password]} (fu/get-form)]
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
      [:form.input-box {:on-submit login-action}
       [:input {:class "bt input-text" :placeholder "Nombre de usuario" :required ""
                :on-change (fu/change-in-form :username)
                :value (or username "")}]
       [:input {:class "bt input-text" :placeholder "Contraseña" :type "password" :required ""
                :on-change (fu/change-in-form :password)
                :value (or password "")}]
       [:button "Entrar"]
       [:a.bottom-link {:href "/#/register"} "¿No tienes cuenta? Regístrate"] ;; onclick empty form
       ]
      ]
     ]))
