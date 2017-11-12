(ns baoqu.components.base
  (:require [rum.core :as rum]
            [baoqu.data :as d]
            [baoqu.components.home :as home-c]
            [baoqu.components.login :as login-c]
            [baoqu.components.fakelogin :as fakelogin-c]
            [baoqu.components.register :as register-c]
            [baoqu.components.reset-password :as reset-password-c]
            [baoqu.components.events :as events-c]
            [sablono.core :refer-macros [html]]
            [baoqu.services.security :as sec]))

(enable-console-print!)

(rum/defc main < rum/reactive
  []
  (let [state (rum/react d/state)
        route (:route state)]
    (println "THE ROUTE IS" route)
    ;; User check should be moved to protected components
    ;;    (if (not (sec/is-authenticated?))
    ;;      (login-c/main)
    (case route
      :login (login-c/main)
      :home (home-c/main)
      :events (events-c/main)
      :fakelogin (fakelogin-c/main)
      :register (register-c/main)
      :reset-password (reset-password-c/main)
      (html [:p "Route not found"]))))
