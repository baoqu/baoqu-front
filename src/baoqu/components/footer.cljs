(ns baoqu.components.footer
  (:require [rum.core :as rum]
            [baoqu.data :as d]
            [baoqu.repos.user :as ur]
            [clojure.string :as s]))

(rum/defc main < rum/reactive
  []
  (let [state (rum/react d/state)
        {:keys [id name]} (ur/get-me)
        initial (s/upper-case (first name))]
    [:div#user
     [:div.avatar
      [:div.thumb {:class (str "color-" id)} initial]]]))
