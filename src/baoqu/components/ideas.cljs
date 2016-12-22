(ns baoqu.components.ideas
  (:require [rum.core :as rum]
            [baoqu.data :as d]
            [baoqu.form-utils :as fu]
            [baoqu.services.idea :as is]
            [baoqu.services.circle :as cs]
            [baoqu.repos.user :as ur]))

(enable-console-print!)

(rum/defcs idea-form < (rum/local {:idea ""})
  [state]
  (let [local-atom (:rum/local state)
        idea (:idea @local-atom)]
    (letfn [(submit-action [e]
              (.preventDefault e)
              (is/add-idea-req idea)
              (reset! local-atom {:idea ""}))
            (change-action [e]
              (swap! local-atom assoc :idea (.. e -target -value)))]
      [:form.mod-add-box {:on-submit submit-action}
       [:textarea { :class "input-text js-autoexpand"
                   :id "autoResize"
                   :rows "1"
                   :data-min-rows "1"
                   :placeholder "Añade una nueva idea"
                   :on-change change-action
                   :value idea}]
       [:button.button
        [:i {:class "fa fa-lg fa-plus"}]]])))

(rum/defcs idea-filter-menu < (rum/local false)
  [state]
  (let [local-atom (:rum/local state)
        show? @local-atom]
    (letfn [(click-action [e]
              (.preventDefault e)
              (swap! local-atom not))]
      [:div.action-wrapper
       [:span.action {:class (str "" (if show? "active"))
                      :on-click click-action
                      :data-balloon-pos "left"
                      :data-balloon "Filtrar ideas"}
        [:i {:class "fa fa-eye"}]]
       (if show?
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
          ])
       ])))

(rum/defc main < rum/reactive
  []
  (let [state (rum/react d/state)
        active-circle (ur/get-active-circle)
        participant-count (cs/get-participants-count active-circle)
        circle-in-path? (cs/circle-in-path? active-circle)
        ideas (is/get-all-for-circle (:id active-circle))]
    [:div.mod-ideas
     [:div.mod-header
      [:span {:class "expander js-expand-ideas"}
        [:i {:class "icon-header fa fa-lg fa-lightbulb-o"}]
      ]

      [:div.title (str "Ideas (" (count ideas) ")")]
      (idea-filter-menu)
        [:span.action
          [:i {:class "fa fa-sort-amount-desc"
               :data-balloon-pos "left"
               :data-balloon "Ordenar ideas"}]]
        [:span.toggle.hide-medium.js-collapse-ideas
          [:i {:class "fa fa-lg fa-angle-right"}]]]

     [:div.mod-body


       [:div.zero-case
         [:h3.title "Ideas"]
         [:p.description "Aquí aparecerán las ideas que se propongan en este círculo"]
         [:ul
           [:li.mod-idea
             [:div.idea
               [:span]
               [:span]
               [:span]
             ]
             [:div.voting-block
               [:div.votes [:div.progress-bar]]
               [:div.btn.btn-gray]
             ]
           ]
          ]
       ]

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

       (for [idea ideas]
         (let [votes (is/vote-count-for-circle idea active-circle)
               voted? (is/voted? idea)
               approval-percentage (* 100 (/ votes participant-count))]
           [:li.mod-idea {:key (:id idea)}
            [:div.idea (:name idea)]
            [:div.voting-block
             [:div.votes
              [:div.votes-count (str votes "/" participant-count " apoyos necesarios")]
              [:div.progress-bar
               [:div.inner {:style {:width (str approval-percentage "%")}}]
               ]
              ]
             (if voted?
               [:div.btn.btn-success {:on-click (is/toggle-idea-vote-req (:id idea))} "Apoyada"]
               [:div.btn.btn-gray {:on-click (is/toggle-idea-vote-req (:id idea))} "Apoyar"])
             ]
            ]))
       ]
      ]
     (if circle-in-path?
       (idea-form))
     ]))
