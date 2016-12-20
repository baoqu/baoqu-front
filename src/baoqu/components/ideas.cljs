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
       (for [[idea-id idea] ideas]
         (let [votes (get idea "votes")
               voted? (get idea "voted?")
               approval-percentage (* 100 (/ votes circle-size))]
           [:li.mod-idea
            [:div.idea (get idea "name")]
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
      [:input {:class "input-text"
               :placeholder "Añade una nueva idea"
               :on-change (fu/change-in-form :idea)
               :value idea}]
      [:button.button
       [:i {:class "fa fa-lg fa-plus"}]]]]))
