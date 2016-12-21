(ns baoqu.components.ideas
  (:require [rum.core :as rum]
            [baoqu.data :as d]
            [baoqu.form-utils :as fu]
            [baoqu.services.idea :as is]))

(enable-console-print!)

(rum/defc main < rum/reactive
  []
  (let [state (rum/react d/state)
        idea (fu/get-f :idea)
        ideas (:ideas state)
        event (:event state)
        circle (:circle state)
        circles (:circles state)
        circle-level (get circle "level")
        circle-size (Math.pow (get circle "size") circle-level)
        num-ideas (count ideas)
        submit-action (comp is/add-idea-req #(.preventDefault %))]
    [:div.mod-ideas
     [:div.mod-header
      [:span {:class "expander js-expand-ideas"}
        [:i {:class "icon-header fa fa-lg fa-lightbulb-o"}]
      ]
      [:div.title (str "Ideas (" num-ideas ")")]
        [:span.action.active
          [:i {:class "fa fa-eye"}]]
        [:span.action
          [:i {:class "fa fa-sort-amount-desc"}]]
        [:span.toggle.hide-medium.js-collapse-ideas
          [:i {:class "fa fa-lg fa-angle-right"}]]]

          [:div.mod-dropdown
            [:ul.mod-options-list
              [:li.option
               [:label.content [:input {:type "radio" :name "view-type"}]
                "Sólo ideas apoyadas"
               ]
              ]
              [:li.option
                [:label.content [:input {:type "radio" :name "view-type"}]
                 "Todas las ideas"
                ]
              ]
             ]
            ]
     [:div.mod-body
      [:ul

        [:li.mod-idea
          [:div.idea ""
            [:input.read-more-state {:type "checkbox" :id "read-more"}]
            [:div.read-more-wrapper "can demore denauer dore jore can demore denauer dore jore can demore denauer dore jorecan demore denauer dore jore can demore denauer dore jore can demore denauer dore jorecan demore denauer dore jore can demore denauer dore jore can demore denauer dore jorecan demore denauer dore jore can demore denauer dore jore can demore denauer dore jore"]
            [:label.read-more-trigger {:for "read-more"}]
          ]
        ]

        [:li.mod-idea
          [:div.idea ""
            [:div.read-more-wrapper "can demore denauer dore jore can demore denauer dore jore can demore denauer dore jorecan demore denauer dore jore can demore denauer dore jore can demore denauer dore jorecan demore denauer dore jore can demore denauer dore jore can demore denauer dore jorecan demore denauer dore jore can demore denauer dore jore can demore denauer dore jore"]
            [:label.read-more-trigger {:for "read-more"}]
          ]
        ]

       (for [[idea-id idea] ideas]
         (let [votes (get idea "votes")
               voted? (get idea "voted?")
               approval-percentage (* 100 (/ votes circle-size))]
           [:li.mod-idea
          ; [:div.idea (get idea "name")]
            [:div.idea ""
              [:input.read-more-state {:type "checkbox" :id "read-more"}]
              [:div.read-more-wrapper (get idea "name")]
              [:label.read-more-trigger {:for "read-more"}]
            ]
            [:div.voting-block
             [:div.votes
              [:div.votes-count (str votes "/" circle-size " apoyos necesarios")]
              [:div.progress-bar
               [:div.inner {:style {:width (str approval-percentage "%")}}]
               ]
              ]
             (if voted?
               [:div.btn.btn-success {:on-click (is/toggle-idea-vote-req idea-id)} "Apoyada"]
               [:div.btn.btn-gray {:on-click (is/toggle-idea-vote-req idea-id)} "Apoyar"])
             ]
            ]))
       ]
       [:div.ideas-zero-case.hide
        [:div.inner
          [:p {:class "copy"}
            "Aún no hay ideas en este círculo. Puedes proponer la primera desde aquí mismo."
          ]
          [:i {:class "fa fa-lg fa-hand-o-down"}]
        ]
       ]
      ]
     [:form.mod-add-box {:on-submit submit-action}
      [:textarea { :class "input-text js-autoexpand"
      :id "autoResize"
                   :rows "1"
                   :data-min-rows "1"
                   :placeholder "Añade una nueva idea"
                   :on-change (fu/change-in-form :idea)
                   :value idea}]
      [:button.button
       [:i {:class "fa fa-lg fa-plus"}]]]]))
