(ns baoqu.components.home
  (:require [rum.core :as rum]
            [baoqu.data :as d]
            [baoqu.mixins :as mixins]
            [baoqu.form-utils :as fu]
            [baoqu.components.comments :as comments]
            [baoqu.components.ideas :as ideas]
            [baoqu.components.circles :as circles]
            [baoqu.components.notifications :as notifications]
            [clojure.string :as s]))

(enable-console-print!)

(rum/defc header < rum/reactive
  []
  (let [state (rum/react d/state)
        event (:event state)
        circle-id (:circle state)
        circles (:circles state)
        circle (first (filter #(= circle-id (:id %)) circles))
        num-ideas (count (:ideas state))
        num-comments (count (:comments state))
        active-section (:active-section state)
        active-modal (:active-modal state)]

    [:div.header-wrapper
     [:div#mainHeader
       [:div.logo-icon]
       [:h1.logo "Baoqu"]
       [:div.event-name (:name event)]
       [:div.event-info-toggle.js-event-info-toggle {:data-balloon-pos "left" :data-balloon "Información del evento"}
        [:div.fa.fa-plus]
       ]
      (letfn [(change-notification [notification] #(swap! d/state assoc :active-notification notification))]
        [:div.test-notifications.hide
          [:div.fa.fa-send
            [:ul
              [:li {:on-click (change-notification "success")} "success"]
              [:li {:on-click (change-notification "error")} "error"]
              [:li {:on-click (change-notification "info")} "info"]
            ]
          ]
        ]
      )
      (letfn [(change-modal [modal] #(swap! d/state assoc :active-modal modal))]
        [:i.fa.fa-lg.fa-windows.hide {:on-click (change-modal "show") :style {:margin-right "30px" :cursor "pointer"}}]
      )
     ]

     [:div.header-event-info.js-event-info
      [:div.inner "El Ayuntamiento (Urbanismo y Transportes), Vecinos y Colectivos se han de reunir para trazar un plan a largo plazo para la progresiva incorporación de la bicicleta como medio de transporte"]
      ]

     (letfn [(change-section [section] #(swap! d/state assoc :active-section section))]
       [:ul.mobile-menu
        [:li {:class (if (= active-section "map") "active" "") :on-click (change-section "map")}
         [:i {:class "icon-header fa fa-lg fa-map"}]
         [:span.title "Mapa"]
         ]
        [:li {:class (if (= active-section "ideas") "active" "") :on-click (change-section "ideas")}
         [:i {:class "icon-header fa fa-lg fa-lightbulb-o"}]
         [:span.title (str num-ideas " ideas")]
         ]
        [:li {:class (if (= active-section "comments") "active" "") :on-click (change-section "comments")}
         [:i {:class "icon-header fa fa-lg fa-comments"}]
         [:span.title (str num-comments " comentarios")]
         ]
        ])
     ]))

(rum/defc footer < rum/reactive
  []
  (let [state (rum/react d/state)
        username (get-in state [:me :name])
        initial (s/upper-case (first username))]
    ;; [:div#mainFooter "user movidas"]
    [:div#user
     [:div.avatar
      [:div.thumb initial]]]))

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

(rum/defc container < rum/reactive
  []
  (let [state (rum/react d/state)
        active-section (:active-section state)]
    [:div.container.event-wrapper {:class (str "mobile-show-" active-section)}
     (circles/the-map)
     (workspace)]))

(rum/defc main < rum/reactive mixins/secured-mixin mixins/connect-see-mixin
  "The main component for the home screen"
  []
  [:div.page
   (notifications/top)
   (header)
   (container)
   (footer)
   (notifications/modaltesting)])
