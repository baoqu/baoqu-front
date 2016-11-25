(ns baoqu.components.ideas
  (:require [rum.core :as rum]
            [baoqu.data :as d]
            [baoqu.form-utils :as fu]
            [baoqu.services.idea :as is]))

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
        num-ideas (count ideas)]
    [:div.mod-ideas
     [:div.mod-header
      [:span {:class "expander js-expand-ideas"}
        [:i {:class "icon-header fa fa-lg fa-lightbulb-o"}]
      ]
      [:div.title (str "Ideas (" num-ideas ")")]
        [:span.toggle
          [:i {:class "fa fa-lg fa-chevron-right js-collapse-ideas"}]]]
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
     [:div.mod-add-box
      [:input {:placeholder "Añade una nueva idea"
               :on-change (fu/change-in-form :idea)
               :value idea}]
      [:button.button {:on-click is/add-idea-req}
       [:i {:class "fa fa-lg fa-plus"}]]]]))
