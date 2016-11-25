(ns baoqu.components.header
  (:require [rum.core :as rum]
            [baoqu.data :as d]))

(rum/defc main < rum/reactive
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
      [:div.inner "Y aquí irá la descripción del evento"]
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
         [:span.title (str num-comments " Chat")]
         ]
        ])
     ]))
