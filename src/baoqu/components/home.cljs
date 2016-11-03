(ns baoqu.components.home
  (:require [rum.core :as rum]
            [baoqu.data :as d]
            [baoqu.mixins :as mixins]
            [baoqu.components.header :as header]
            [baoqu.components.footer :as footer]
            [baoqu.components.comments :as comments]
            [baoqu.components.ideas :as ideas]
            [baoqu.components.circles :as circles]
            [baoqu.components.notifications :as notifications]))

(enable-console-print!)

(rum/defc workspace < rum/reactive
  []
  (let [state (rum/react d/state)
        circle (:circle state)]
    [:div.circle-wrapper
     [:div.circle-header
      [:div.circle-header-title (str "Círculo " (get circle "id"))
        [:span.tag
          [:span "Nivel "]
          [:span (get circle "level")]
        ]
        [:span.tag.my-circle "Mi círculo"]
      ]
      [:span.circle-header-exit
        [:span "Salir "]
        [:span.hide-medium " de este círculo"]
      ]]
     [:div.circle-content
      (ideas/main)
      (comments/main)]]))

(rum/defc main < rum/reactive mixins/secured-mixin mixins/connect-see-mixin
  "The main component for the home screen"
  []
  (let [state (rum/react d/state)
        active-section (:active-section state)
        notification (:notification state)]
    [:div.page
     (notifications/top)
     (header/main)
     [:div.container.event-wrapper {:class (str "mobile-show-" active-section)}
      (circles/the-map)
      (workspace)]
     (footer/main)
     (if-not (empty? notification)
       (notifications/modaltesting))]))
