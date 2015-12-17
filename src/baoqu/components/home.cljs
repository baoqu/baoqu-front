(ns baoqu.components.home
  (:require [rum.core :as rum]
            [baoqu.services.event :as event]
            [baoqu.data :as d]
            [baoqu.mixins :as mixins]
            [clojure.string :as s]))

(rum/defc header < rum/reactive
  []
  (let [state (rum/react d/state)
        event (:event state)
        circle-id (:circle state)
        circles (:circles state)
        circle (first (filter #(= circle-id (:id %)) circles))]
    [:div.header-wrapper
     [:div#mainHeader
      [:h1.logo "Baoqu"]
      [:div.event-name (:name event)]]
     [:ul.mobile-menu
      [:li.active
       [:i {:class "icon-header fa fa-lg fa-map"}]
       [:span.title "Map"]
       ]
      [:li
       [:i {:class "icon-header fa fa-lg fa-lightbulb-o"}]
       [:span.title (str (:num-ideas circle) " ideas")]
       ]
      [:li
       [:i {:class "icon-header fa fa-lg fa-comments"}]
       [:span.title (str (:num-comments circle) " deliberación")]
       ]
      ]
     ]))

(rum/defc footer < rum/reactive
  []
  (let [state (rum/react d/state)
        username (get-in state [:session :username])
        initial (s/upper-case (first username))]
    ;; [:div#mainFooter "user movidas"]
    [:div#user
     [:div.avatar
      [:div.thumb initial]]]))

(rum/defc ideas < rum/reactive
  []
  (let [state (rum/react d/state)
        circle-id (:circle state)
        circles (:circles state)
        circle (first (filter #(= circle-id (:id %)) circles))]
    [:div.mod-ideas
     [:div.mod-header
      [:i {:class "icon-header fa fa-lg fa-lightbulb-o"}]
      [:div.title (str (:num-ideas circle) " ideas")]
      [:span.toggle
       [:i {:class "fa fa-lg fa-chevron-right"}]]]
     [:div.mod-body
      [:ul
       (for [idea (:ideas state)]
         (let [circle-size (get-in state [:event :circle-size])
               votes (:votes idea)
               approval-percentage (* 100 (/ votes circle-size))]
           [:li.mod-idea
            [:div.idea (:body idea)]
            [:div.voting-block
             [:div.votes
              [:div.votes-count (str votes "/" circle-size " apoyos para promocionar el círculo")]
              [:div.progress-bar
               [:div.inner {:style {:width (str approval-percentage "%")}}]
               ]
              ]
             [:div.btn.btn-gray "Apoyar"]
             ]
            ]))
       ]
      ]
     [:div.mod-add-box
      [:input {:placeholder "# Añade una nueva idea"}]
      [:span.button
       [:i {:class "fa fa-lg fa-plus"}]]]]))

(rum/defc comments < rum/reactive
  []
  (let [state (rum/react d/state)
        circle-id (:circle state)
        circles (:circles state)
        circle (first (filter #(= circle-id (:id %)) circles))]
    [:div.mod-comments
     [:div.mod-header
      [:i {:class "icon-header fa fa-lg fa-comments"}]
      [:div.title (str (:num-comments circle) " deliberación")]
      [:span.toggle
       [:i {:class "fa fa-lg fa-chevron-right"}]]]

     [:div.mod-body
      [:ul
       (for [comment (:comments state)]
         (let [participants (:participants state)
               author-id (:author comment)
               author (first (filter #(= (:id %) author-id) participants))
               initial (s/upper-case (first (:name author)))]
           [:li.mod-comment
            [:div.avatar
             [:div.thumb initial]
             ]
            [:div.content
             [:div.username (:name author)]
             [:div.comment (:body comment)]
             ]
            ])
         )
       ]
      ]
    [:div.mod-add-box
     [:input {:placeholder "Comenta"}]
     [:span.button
      [:i {:class "fa fa-lg fa-plus"}]]]]))

(rum/defc circle < rum/reactive
  []
  (let [state (rum/react d/state)
        circle-id (:circle state)
        circles (:circles state)
        circle (first (filter #(= circle-id (:id %)) circles))]
    [:div.circle-wrapper
     [:div.circle-header
      [:div.circle-header-title (:name circle)
       [:span.tag (str "Nivel " (:level circle))]
       ]
      [:span.circle-header-exit "Salir de este círculo"]]
     [:div.circle-content
      (ideas)
      (comments)]]))

(rum/defc the-map < rum/reactive
  []
  [:div.map
    [:div.circle.c-lv1.not-full
      [:div.circle.agree]
      [:div.circle]
      [:div.circle.not-full]

      ;; context info
      [:div.context-info
        [:div.circle-title "Círculo Carmander"
          [:span.tag "Nivel 1"]
        ]
        [:div.mod-idea
          [:div.intro "Idea más apoyada"]
          [:div.idea "Montar un bar"]
          [:div.voting-block
            [:div.votes
              [:div.votes-count "2/3 apoyos para promocionar"]
              [:div.progress-bar
                [:div.inner {:style {:width "66%"}}]
              ]
            ]
          ]
        ]
        [:div.mod-meta
          [:div.item
          [:i {:class "icon-header fa fa-lightbulb-o"}]
            [:span "33"]
          ]
          [:div.item
          [:i {:class "icon-header fa fa-comments"}]
            [:span "33"]
          ]
          [:div.item
          [:i {:class "icon-header fa fa-lightbulb-o"}]
            [:span "33"]
          ]
        ]
      ]
      ;; end: context info
    ]
    [:div.circle.c-lv1.my-circle
      [:div.circle.agree]
      [:div.circle.agree.my-circle]
      [:div.circle]
    ]
    [:div.circle.c-lv2
      [:div.circle
        [:div.circle.agree]
        [:div.circle]
        [:div.circle]
      ]
      [:div.circle
        [:div.circle.agree]
        [:div.circle]
        [:div.circle.agree]
      ]
      [:div.circle
        [:div.circle.agree]
        [:div.circle.agree]
        [:div.circle.agree]
      ]
    ]
    [:div.circle.c-lv1
    [:div.circle.agree]
    [:div.circle.agree]
    [:div.circle]
    ]
    [:div.circle.c-lv3
      [:div.circle]
      [:div.circle]
      [:div.circle
        [:div.circle]
        [:div.circle]
        [:div.circle
          [:div.circle]
          [:div.circle]
          [:div.circle
            [:div.circle]
            [:div.circle]
            [:div.circle]
          ]
        ]
      ]
    ]
  ])

(rum/defc container < rum/reactive
  []
  [:div.container
   (the-map)
   (circle)])

(rum/defc main < rum/reactive mixins/secured-mixin mixins/connect-ws-mixin
  "The main component for the home screen"
  []
  ;; list-events

  ;; select-event

  ;; join-event
  (event/join-event)

  [:div.page
   (header)
   (container)
   (footer)])
