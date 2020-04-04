(ns baoqu.components.events
  (:require [rum.core :as rum]
            [baoqu.data :as d]
            [baoqu.repos.event :as event-r]
            [baoqu.services.event :as event-s]))

(rum/defc event-item
  [{:keys [id name circle-size agreement-factor] :as event}]
  [:li.grid-cell {:key id}
    [:a.event {:key id :on-click #(event-s/visit-event % event)}
     [:div.header
       [:span.id id]
       [:span.name name]
     ]
     [:div.description "Deberíamos restringir esta cantidad de caracteres a una cantidad parecida a esta que está puesta que no la he contado y cerrar con elipsis ..."]
     [:div.mod-meta
       [:div.item
        [:span
          [:i {:class "icon-header fa fa-lightbulb-o"}]
          [:span.value "33"]
          [:span.label "ideas"]
        ]
       ]
       [:div.item
        [:span
          [:i {:class "icon-header fa fa-comments"}]
          [:span.value "33"]
          [:span.label "comentarios"]
        ]
       ]
       [:div.item
        [:span
          [:i {:class "icon-header fa fa-users"}]
          [:span.value "33"]
          [:span.label "participantes"]
        ]
       ]]]])


(rum/defc main < rum/reactive {:will-mount (fn [state]
                                             (event-s/fetch-events)
                                             state)}
  []
  (let [state (rum/react d/state)
        events (event-r/get-events)]
    [:div.events-list
     [:div.header-wrapper
      [:div#mainHeader
       [:div.logo-icon]
       [:h1.logo "Baoqu"]]]
     [:div.mod-body
      [:h3.title "Eventos en Baoqu"]
      [:ul.grid
       (for [event events]
         [(event-item event)])
       ]
      ]
     ]))
