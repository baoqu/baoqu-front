(ns baoqu.components.ideas
  (:require [rum.core :as rum]
            [baoqu.data :as d]
            [clojure.string :as s]
            [baoqu.form-utils :as fu]
            [baoqu.services.idea :as is]
            [baoqu.services.circle :as cs]
            [baoqu.repos.user :as ur]))

(enable-console-print!)

(rum/defcs idea-form < (rum/local {:idea ""})
  [{local :rum/local}]
  (let [idea (:idea @local)]
    (letfn [(submit-action [e]
              (.preventDefault e)
              (is/add-idea-req idea)
              (reset! local {:idea ""}))
            (change-action [e]
              (swap! local assoc :idea (.. e -target -value)))]
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
  [{local :rum/local}]
  (let [show? @local]
    (letfn [(click-action [e]
              (.preventDefault e)
              (swap! local not))]
      [:div
       [:span.action {:class (str "" (if show? "active"))}
        [:i {:class "fa fa-eye"
             :on-click click-action}]]
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

(rum/defcs show < (rum/local false)
                  rum/static
                  rum/reactive
  [{local :rum/local} {:keys [id name] :as idea}]
  (let [state (rum/react d/state)
        more? @local
        limit 200
        length (count name)
        big? (> length limit)
        active-circle (ur/get-active-circle)
        participant-count (cs/get-participants-count active-circle)
        votes (is/vote-count-for-circle idea active-circle)
        approval-percentage (* 100 (/ votes participant-count))]
    (letfn [(click-action [e]
              (.preventDefault e)
              (swap! local not))]
      [:li.mod-idea {:key id}
       [:div.idea
        (if big?
          (if more?
            [:div name]
            [:div (str (s/join (take limit name)) "...")])
          [:div name])
        [:span.read-more-trigger {:on-click click-action}
         (if big?
           (if more?
             "Mostrar menos"
             "Mostrar más"))]]
       [:div.voting-block
        [:div.votes
         [:div.votes-count (str votes "/" participant-count " apoyos necesarios")]
         [:div.progress-bar
          [:div.inner {:style {:width (str approval-percentage "%")}}]
          ]
         ]
        (if (is/voted? idea)
          [:div.btn.btn-success {:on-click (is/toggle-idea-vote-req (:id idea))} "Apoyada"]
          [:div.btn.btn-gray {:on-click (is/toggle-idea-vote-req (:id idea))} "Apoyar"])
        ]
       ])))

(rum/defc main < rum/reactive
  []
  (let [state (rum/react d/state)
        active-circle (ur/get-active-circle)
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
       [:i {:class "fa fa-sort-amount-desc"}]]
      [:span.toggle.hide-medium.js-collapse-ideas
       [:i {:class "fa fa-lg fa-angle-right"}]]]

     [:div.mod-body
      (if-not (empty? ideas)
        [:ul
         (for [idea ideas]
           (show idea))
         ]
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
         ])
      ]
     (if circle-in-path?
       (idea-form))
     ]))
