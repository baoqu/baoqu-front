(ns baoqu.components.footer
  (:require [rum.core :as rum]
            [baoqu.data :as d]
            [clojure.string :as s]))

(rum/defc main < rum/reactive
  []
  (let [state (rum/react d/state)
        username (get-in state [:me :name])
        initial (s/upper-case (first username))]
    ;; [:div#mainFooter "user movidas"]
    [:div#user
     [:div.avatar
      [:div.thumb initial]]]))
