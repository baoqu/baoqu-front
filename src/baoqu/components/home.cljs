(ns baoqu.components.home
  (:require [rum.core :as rum]
            [baoqu.data :as d]
            [baoqu.mixins :as mixins]
            [baoqu.services.circle :as circle-s]
            [baoqu.repos.user :as user-r]
            [baoqu.repos.notification :as notification-r]
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
        my-circle (circle-s/get-my-circle)]
    [:div.circle-wrapper
     [:div.circle-header
      [:div.circle-header-title (str "Círculo " (:id my-circle))
        [:span.tag
          [:span.label "Nivel "]
          [:span.value (:level my-circle)]
        ]
        [:span.tag.my-circle
          [:span.label "Mi círculo"]
        ]
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
        active-section (user-r/get-active-section)
        notification (notification-r/get-notification)]
    [:div.page
     (notifications/top)
     (header/main)
     [:div.container.event-wrapper {:class (str "mobile-show-" active-section)}
      (circles/the-map)
      (workspace)]
     (footer/main)
     (if-not (empty? notification)
       (notifications/modaltesting))]))
