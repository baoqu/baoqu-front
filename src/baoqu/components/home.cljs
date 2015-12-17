(ns baoqu.components.home
  (:require [rum.core :as rum]
            [baoqu.services.event :as event]
            [baoqu.data :as d]
            [baoqu.mixins :as mixins]
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
        active-section (:active-section state)]
    [:div.header-wrapper
     [:div#mainHeader
       [:div.logo-icon]
       [:h1.logo "Baoqu"]
      [:div.event-name (:name event)]]
     (letfn [(change-section [section] #(swap! d/state assoc :active-section section))]
       [:ul.mobile-menu
        [:li {:class (if (= active-section "map") "active" "") :on-click (change-section "map")}
         [:i {:class "icon-header fa fa-lg fa-map"}]
         [:span.title "Map"]
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
        username (get-in state [:session :username])
        initial (s/upper-case (first username))]
    ;; [:div#mainFooter "user movidas"]
    [:div#user
     [:div.avatar
      [:div.thumb initial]]]))

(rum/defc ideas < rum/reactive
  []
  (let [state (rum/react d/state)
        event (:event state)
        circle-id (:circle state)
        circles (:circles state)
        circle (first (filter #(= circle-id (:id %)) circles))
        num-ideas (count (:ideas state))]
    [:div.mod-ideas
     [:div.mod-header
      [:i {:class "icon-header fa fa-lg fa-lightbulb-o"}]
      [:div.title (str num-ideas " ideas")]
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
        event (:event state)
        circle-id (:circle state)
        circles (:circles state)
        circle (first (filter #(= circle-id (:id %)) circles))
        num-comments (count (:comments state))]
    [:div.mod-comments
     [:div.mod-header
      [:i {:class "icon-header fa fa-lg fa-comments"}]
      [:div.title (str num-comments " comentarios")]
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

(rum/defc my-circle < rum/reactive
  []
  (let [state (rum/react d/state)
        circle-id (:circle state)
        circles (:circles state)
        circle (first (filter #(= circle-id (:id %)) circles))]
    [:div.circle-wrapper
     [:div.circle-header
      [:div.circle-header-title (:name circle)
        [:span.tag
          [:span "Nivel "]
          [:span (:level circle)]
        ]
      ]
      [:span.circle-header-exit
        [:span "Salir "]
        [:span.hide-medium " de este círculo"]
      ]]
     [:div.circle-content
      (ideas)
      (comments)]]))

(rum/defc a-circle < rum/static
  [circle]
  (let [level (:level circle)
        circle-size (:circle-size @d/state)
        circles (:circles @d/state)
        percentage (/ (:most-popular-idea-votes circle) circle-size)
        inner-circles-ids (into #{} (:inner-circles circle))
        inner-circles (when inner-circles-ids
                        (filter (comp inner-circles-ids :id) circles))]
    [:div.circle {:class (str "c-lv" (:level circle))
                  :key (str (:id circle))}
     [:div.context-info
      [:div.circle-title (:name circle)
       [:span.tag (str "Nivel " (:level circle))]
       ]
      [:div.mod-idea
       [:div.intro "Idea más apoyada"]
       [:div.idea (:most-voted-idea circle)]
       [:div.voting-block
        [:div.votes
         [:div.votes-count (str (:most-popular-idea-votes circle) "/" circle-size " apoyos para promocionar")]
         [:div.progress-bar
          [:div.inner {:style {:width (str percentage "%")}}]
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
     (for [inner-circle inner-circles]
       (a-circle inner-circle))
     ]))

(rum/defc the-map < rum/reactive
  []
  (let [state (rum/react d/state)
        all-circles (:circles state)]
    [:div.map
     (for [level (reverse (range 1 4))]
       (let [parent-circles (filter #(and (= (:level %) level) (nil? (:parent-circle %))) all-circles)]
         (for [circle parent-circles]
           (a-circle circle))))]))

(rum/defc container < rum/reactive
  []
  (let [state (rum/react d/state)
        active-section (:active-section state)]
    [:div.container {:class (str "mobile-show-" active-section)}
     (the-map)
     (my-circle)]))

(rum/defc main < rum/reactive mixins/secured-mixin mixins/connect-ws-mixin
  "The main component for the home screen"
  []
  (event/join-event)

  [:div.page
   (header)
   (container)
   (footer)])
