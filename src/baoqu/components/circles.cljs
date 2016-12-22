(ns baoqu.components.circles
  (:require [rum.core :as rum]
            [baoqu.data :as d]
            [baoqu.repos.circle :as cr]
            [baoqu.repos.user :as ur]
            [baoqu.repos.idea :as ir]
            [baoqu.services.idea :as is]
            [baoqu.services.circle :as cs]))

(enable-console-print!)

(declare my-circle)

(rum/defc circle-context < rum/reactive
  [{:keys [level size] :as circle}]
  (let [state (rum/react d/state)
        percentage (* 100 (/ (:most-popular-idea-votes circle) (* size level)))]
    [:div.context-info.js-context-info
     [:div.circle-title (get circle "name")
      [:span.tag (str "Nivel " level)]
      ]
     [:div.mod-idea
      [:div.intro "Idea más apoyada"]
      [:div.idea (:most-popular-idea circle)]
      [:div.voting-block
       [:div.votes
        [:div.votes-count (str (:most-popular-idea-votes circle) "/" (* size level) " apoyos para promocionar")]
        [:div.progress-bar
         [:div.inner {:style {:width (str percentage "%")}}]
         ]
        ]
       ]
      ]
     [:div.mod-meta
      [:div.item
       [:i {:class "icon-header fa fa-lightbulb-o"}]
       [:span "33"]
       ]
      [:div.item
       [:i {:class "icon-header fa fa-comments"}]
       [:span "33"]
       ]
      [:div.item
       [:i {:class "icon-header fa fa-lightbulb-o"}]
       [:span "33"]
       ]
      ]
     ]
    ))

(rum/defc a-circle < rum/reactive
  [{:keys [id users level parent-circle-id] :as circle}]
  (let [state (rum/react d/state)
        in-path? (cs/circle-in-path? circle)
        active-circle? (cs/is-active-circle? circle)
        inner-circles (cs/get-inner-circles-for-circle circle)]
    [:div.circle {:class (str "c-lv" level " " (when (nil? parent-circle-id) "root js-circle-root") " " (if in-path? "my-circle") " " (if active-circle? "active-circle"))
                  :on-click #(cs/visit-circle % circle)}
     ;;(circle-context circle)
     (if (= level 1)
       (for [key users]
         [:div.circle {:key key}])
       (for [{inner-id :id :as inner-circle} inner-circles]
         (-> (a-circle inner-circle)
             (rum/with-key inner-id))))
     ]))

(rum/defc the-map < rum/reactive
  []
  (let [state (rum/react d/state)
        ideas (ir/get-ideas)
        path (ur/get-my-path)
        {active-circle-id :id} (ur/get-active-circle)]
    [:div.map
     [:div {:class "map-toggle-view js-map-toggle-view"}
      [:div {:class "map-toggle map-toggle-map" :data-balloon-pos "left" :data-balloon "Mapa de acuerdos"}
       [:i {:class "icon-header fa fa-lg fa-map"}]
       ]
      [:div {:class "map-toggle map-toggle-list" :data-balloon-pos "left" :data-balloon "Ideas más votadas"}
       [:i {:class "icon-header fa fa-lg fa-list"}]
       ]
      ]
     [:div {:class "map-list-view"}
      [:div.header "Ideas más apoyadas"]
      [:div.ideas-list
       (for [idea ideas]
         [:div.mod-idea {:key (:id idea)}
          [:div.supports
           [:div.value (is/vote-count-for-idea idea)]
           [:div.label " apoyos"]
           ]
          [:div.body
           [:div.idea (:name idea)]
           [:div.info  "Círculo " (:circle idea) " | " "nivel " (:level idea)]
           (if (get idea "voted?")
             [:span.badge "la apoyaste"])
           ]
          ])
       ]
      ]
     [:div {:class "map-circles-view"}
      (for [level (cs/get-level-range)]
        (let [parent-circles (cs/get-parent-circles-for-level level)]
          (for [circle parent-circles]
            (-> (a-circle circle)
                (rum/with-key (:id circle))))))
      ]
     ]))
