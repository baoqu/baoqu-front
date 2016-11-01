(ns baoqu.components.comments
  (:require [rum.core :as rum]
            [baoqu.data :as d]
            [baoqu.form-utils :as fu]
            [baoqu.services.comment :as cs]
            [clojure.string :as s]))

(rum/defc main < rum/reactive
  []
  (let [state (rum/react d/state)
        comment (fu/get-f :comment)
        event (:event state)
        circle (:circle state)
        circles (:circles state)
        comments (:comments state)
        num-comments (count comments)]
    [:div.mod-comments
     [:div.mod-header
      [:span {:class "expander js-expand-comments"}
        [:i {:class "icon-header fa fa-lg fa-comments"}]
      ]
      [:div.title (str "Comentarios (" num-comments ")")]
      [:span.toggle
       [:i {:class "fa fa-lg fa-chevron-right js-collapse-comments"}]]]

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
      ]
     [:div.mod-add-box
      [:input {:placeholder "Comenta"
               :on-change (fu/change-in-form :comment)
               :value comment}]
      [:button.button {:on-click cs/add-comment-req}
       [:i {:class "fa fa-lg fa-plus"}]]]]))
