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
      [:form.mod-add-box {:on-submit submit-action}
       [:input { :class "input-text js-autoexpand"
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
     [:ul
      (for [comment comments]
        (let [author (:name comment)
              initial (s/upper-case (first author))]
          [:li.mod-comment
           [:div.avatar
            [:div.thumb initial]
            ]
           [:div.content
            [:div.username author]
            [:div.comment (:body comment)]
            ]
           ])
        )
      ]
     ]))

(rum/defc main < rum/reactive
  []
  (let [state (rum/react d/state)
        active-circle (ur/get-active-circle)
        circle-in-path? (cis/circle-in-path? active-circle)
        comments (cs/get-all-for-circle (:id active-circle))

        ;; circle (:circle state)
        ;; circles (:circles state)
        ]
    [:div.mod-comments
     [:div.mod-header
      [:span {:class "expander js-expand-comments"}
       [:i {:class "icon-header fa fa-lg fa-comments-o"}]
       ]
      [:div.title (str "Chat")]
      [:span.action
       [:i {:class "fa fa-users"}]]
      [:span.toggle.hide-medium.js-collapse-comments
       [:i {:class "fa fa-lg fa-angle-right"}]
       ]
      [:div.mod-dropdown
       [:ul.mod-users-list
        [:li.user
         [:div.avatar [:div.thumb "A"]]
         [:div.content [:div.username "Adelino"]]
         ]
        [:li.user
         [:div.avatar [:div.thumb "A"]]
         [:div.content [:div.username "Adelino"]]
         ]
        [:li.user
         [:div.avatar [:div.thumb "A"]]
         [:div.content [:div.username "Adelino"]]
         ]

        [:li.user
         [:div.avatar [:div.thumb "A"]]
         [:div.content [:div.username "Adelino"]]
         ]
        [:li.user
         [:div.avatar [:div.thumb "A"]]
         [:div.content [:div.username "Adelino"]]
         ]
        ]
       ]
      ]
     (comments-box)
     (if circle-in-path?
       (comment-form))
     ]))
