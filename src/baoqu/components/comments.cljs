(ns baoqu.components.comments
  (:require [rum.core :as rum]
            [baoqu.data :as d]
            [baoqu.form-utils :as fu]
            [baoqu.mixins :as mixins]
            [baoqu.services.comment :as cs]
            [baoqu.services.circle :as cis]
            [baoqu.repos.user :as ur]
            [clojure.string :as s]))

(enable-console-print!)

(rum/defcs comment-form < (rum/local {:comment ""})
  [state]
  (let [local-atom (:rum/local state)
        comment (:comment @local-atom)]
    (letfn [(submit-action [e]
              (.preventDefault e)
              (cs/add-comment-req comment)
              (reset! local-atom {:comment ""}))
            (change-action [e]
              (swap! local-atom assoc :comment (.. e -target -value)))]
      [:form {:on-submit submit-action
              :id "form-comments"
              :class "mod-add-box"}
       [:input { :class "input-text js-autoexpand js-comments-textarea"
                    :rows "1"
                    :data-min-rows "1"
                    :placeholder "Escribe aquí"
                    :on-change change-action
                    :value comment}]
       [:button.button
        [:i {:class "fa fa-lg fa-plus"}]]])))

(rum/defc comments-box < rum/reactive
                         mixins/scroll-on-insert
  []
  (let [state (rum/react d/state)
        active-circle (ur/get-active-circle)
        comments (cs/get-all-for-circle (:id active-circle))]
    [:div.mod-body
     (if-not (empty? comments)
       [:ul
        (for [comment comments]
          (let [author (:name comment)
                author-id (:user-id comment)
                initial (s/upper-case (first author))]
            [:li.mod-comment
             [:div.avatar
              [:div.thumb {:class (str "color-" author-id)} initial]
              ]
             [:div.content
              [:div.username author]
              [:div.comment (:body comment)]
              ]
             ])
          )
        ]
       [:div.zero-case
        [:h3.title "Chat"]
        [:p.description "Aún nadie ha dicho nada en este círculo."]
        [:ul
         [:li.mod-comment
          [:div.avatar
           [:div.thumb]
           ]
          [:div.content
           [:div.username]
           [:div.comment]
           ]
          ]
         [:li.mod-comment
          [:div.avatar
           [:div.thumb]
           ]
          [:div.content
           [:div.username]
           [:div.comment]
           ]
          ]
         [:li.mod-comment
          [:div.avatar
           [:div.thumb]
           ]
          [:div.content
           [:div.username]
           [:div.comment]
           ]
          ]
         ]
        ])
     ]))

(rum/defcs user-list < rum/reactive
                       (rum/local false)
  [local-state]
  (let [state (rum/react d/state)
        local-atom (:rum/local local-state)
        show? @local-atom
        active-circle (ur/get-active-circle)
        users (ur/get-all-for-circle (:id active-circle))]
    (letfn [(click-action [e]
              (.preventDefault e)
              (swap! local-atom not))]
      [:div.action-wrapper
       [:span.action {:class (str "" (if show? "active"))
                      :on-click click-action
                      :data-balloon-pos "left"
                      :data-balloon "Participantes en este círculo"}
        [:i {:class "fa fa-users"}]]
       (if show?
         [:div.mod-dropdown
          [:ul.mod-users-list
           (for [user users]
             (let [id (:id user)
                   author (:name user)
                   initial (s/upper-case (first author))]
               [:li.user {:key id}
                [:div.avatar [:div.thumb {:class (str "color-" id)} initial]]
                [:div.content [:div.username author]]
                ]))
           ]
          ])
       ])))

(rum/defc main < rum/reactive
  []
  (let [state (rum/react d/state)
        active-circle (ur/get-active-circle)
        circle-in-path? (cis/circle-in-path? active-circle)
        comments (cs/get-all-for-circle (:id active-circle))]
    [:div.mod-comments
     [:div.mod-header
      [:span {:class "expander js-expand-comments"}
       [:i {:class "icon-header fa fa-lg fa-comments-o"}]
       ]
      [:div.title (str "Chat")]
      (user-list)
      [:span.toggle.hide-medium.js-collapse-comments
       [:i {:class "fa fa-lg fa-angle-right"}]
       ]

      ]
     (comments-box)
     (if circle-in-path?
       (comment-form))
     ]))
