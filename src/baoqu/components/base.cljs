(ns baoqu.components.base
  (:require [rum.core :as rum]
            [baoqu.data :as d]
            [baoqu.components.home :as home-c]
            [baoqu.components.login :as login-c]
            [sablono.core :refer-macros [html]]))

(enable-console-print!)

(rum/defc main < rum/reactive
  []
  (let [state (rum/react d/state)
        route (:route state)]
    (case route
      :login (login-c/main)
      :home (home-c/main)
      (html [:p "Route not found"]))))
