(ns baoqu.components.comments
  (:require [rum.core :as rum]
            [baoqu.data :as d]
            [baoqu.form-utils :as fu]
            [baoqu.mixins :as mixins]
            [baoqu.services.comment :as cs]
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
       [:input {:placeholder "Escribe aquí"
                :on-change change-action
                :value comment}]
       [:button.button
        [:i {:class "fa fa-lg fa-plus"}]]])))

(rum/defc comments-box < rum/reactive
                         mixins/scroll-on-insert
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
        event (:event state)
        circle (:circle state)
        circles (:circles state)
        comments (:comments state)
        num-comments (count comments)]
    [:div.mod-comments
     [:div.mod-header
      [:span {:class "expander js-expand-comments"}
       [:i {:class "icon-header fa fa-lg fa-comments-o"}]
       ]
      [:div.title (str "Chat (" num-comments ")")]
      [:span.toggle.js-collapse-comments
       [:i {:class "fa fa-lg fa-angle-right"}]]]
     (comments-box)
     (comment-form)
     ]))
