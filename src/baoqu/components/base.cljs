(ns baoqu.components.base
  (:require [rum.core :as rum]
            [baoqu.data :refer [state]]
            [baoqu.components.home :as home-c]
            [baoqu.components.login :as login-c]))

(rum/defc main < rum/reactive
  []
  (let [state (rum/react state)
        username (get-in state [:session :username])]
    (if username
      (home-c/main)
      (login-c/main))))
