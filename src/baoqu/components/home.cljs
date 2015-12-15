(ns baoqu.components.home
  (:require [rum.core :as rum]))

(rum/defc header < rum/reactive
  []
  [:div#mainHeader
   [:h1.logo "Baoqu"]
   [:div.event-name "¿Qué tipo de muerte dolorosa queremos para los ciclistas de La Guindalera?"]])

(rum/defc footer < rum/static
  []
  [:div#mainFooter "user movidas"])

(rum/defc ideas < rum/static
  []
  [:div.mod-ideas
   [:div.mod-header
    [:div.title "36 ideas"]
    [:span.toggle
     [:i {:class "fa fa-lg fa-chevron-right"}]]]
   [:div.mod-body "ideas body"]
   [:div.mod-add-box
    [:input {:placeholder "Añade una nueva idea"}]
    [:span.button
     [:i {:class "fa fa-lg fa-plus"}]]]])

(rum/defc comments < rum/static
  []
  [:div.mod-comments
   [:div.mod-header
    [:div.title "36 comments"]
    [:span.toggle
     [:i {:class "fa fa-lg fa-chevron-right"}]]]
   [:div.mod-body "comments body"]
   [:div.mod-add-box
    [:input {:placeholder "Comenta"}]
    [:span.button
     [:i {:class "fa fa-lg fa-plus"}]]]])

(rum/defc circle < rum/static
  []
  [:div.circle-wrapper
   [:div.circle-header
    [:div.circle-header-title "Círculo Onisuzume"]
    [:span.circle-header-exit "Salir de este círculo"]]
   [:div.circle-content
    (ideas)
    (comments)]])

(rum/defc map < rum/reactive
  []
  [:div.map "mapa"])

(rum/defc container < rum/reactive
  []
  [:div.container
   (map)
   (circle)])

(rum/defc main < rum/reactive
  "The main component for the home screen"
  []
  [:div.page
   (header)
   (container)
   (footer)])