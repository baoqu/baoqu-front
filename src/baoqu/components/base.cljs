(ns baoqu.components.base
  (:require [rum.core :as rum]
            [baoqu.data :as d]
            [baoqu.components.home :as home-c]
            [baoqu.components.login :as login-c]
            [baoqu.components.fakelogin :as fakelogin-c]
            [baoqu.components.fakeevents :as fakeevents-c]
            [sablono.core :refer-macros [html]]
            [baoqu.services.security :as sec]))

(enable-console-print!)

(rum/defc main < rum/reactive
  []
  (let [state (rum/react d/state)
        route (:route state)]
    ;; User check should be moved to protected components
    ;;    (if (not (sec/is-authenticated?))
    ;;      (login-c/main)
    (case route
      :login (login-c/main)
      :home (home-c/main)
      :fakelogin (fakelogin-c/main)
      :fakeevents (fakeevents-c/main)
      (html [:p "Route not found"]))))
