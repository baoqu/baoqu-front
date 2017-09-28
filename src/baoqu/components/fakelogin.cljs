(ns baoqu.components.fakelogin
  (:require [rum.core :as rum]))

(enable-console-print!)

(rum/defc main
  []
  [:div [:h1 "LOGIN!!"]
   [:p "aquí irían el form, los inputs y el button... dejo rienda suelta a tu creatividad :)"]])
