(ns baoqu.components.comments
  (:require [rum.core :as rum]
            [baoqu.data :as d]
            [baoqu.form-utils :as fu]
            [baoqu.services.comment :as cs]
            [clojure.string :as s]))

(enable-console-print!)

(rum/defc comments-box < rum/reactive
  []
  (let [state (rum/react d/state)
        comments (:comments state)]
    [:div.mod-body
     [:ul
      (for [comment comments]
        (let [author (get comment "name")
              initial (s/upper-case (first author))
              body (get comment "body")]
          [:li.mod-comment
           [:div.avatar
            [:div.thumb initial]
            ]
           [:div.content
            [:div.username author]
            [:div.comment body]
            ]

           ])
        )
      ]
     ]))

(rum/defc main < rum/reactive
  []
  (let [state (rum/react d/state)
        comment (fu/get-f :comment)
        event (:event state)
        circle (:circle state)
        circles (:circles state)
        comments (:comments state)
        num-comments (count comments)
        submit-action (comp cs/add-comment-req #(.preventDefault %))]
    [:div.mod-comments
     [:div.mod-header
      [:span {:class "expander js-expand-comments"}
       [:i {:class "icon-header fa fa-lg fa-comments"}]
       ]
      [:div.title (str "Chat (" num-comments ")")]
      [:span.toggle
       [:i {:class "fa fa-lg fa-chevron-right js-collapse-comments"}]]]

     (comments-box)
     [:form.mod-add-box {:on-submit submit-action}
      [:input {:placeholder "Escribe aquí"
               :on-change (fu/change-in-form :comment)
               :value comment}]
      [:button.button
       [:i {:class "fa fa-lg fa-plus"}]]]]))
