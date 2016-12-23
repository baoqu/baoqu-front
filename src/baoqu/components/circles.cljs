(ns baoqu.components.circles
  (:require [rum.core :as rum]
            [baoqu.data :as d]
            [baoqu.repos.circle :as cr]
            [baoqu.repos.user :as ur]
            [baoqu.repos.idea :as ir]
            [baoqu.services.idea :as is]
            [baoqu.services.circle :as cis]
            [baoqu.services.comment :as cs]))

(enable-console-print!)

(declare my-circle)

(rum/defc circle-context < rum/reactive
  [{:keys [id level size] :as circle}]
  (let [state (rum/react d/state)
        max-participants (cis/get-participants-count circle)
        {name :name votes :votes :as highest-voted-idea} (is/get-highest-voted-idea-for-circle circle)
        idea-count (is/count-all-for-circle circle)
        comment-count (cs/count-all-for-circle circle)
        users-count (count (ur/get-all-for-circle (:id circle)))
        percentage (* 100 (/ votes max-participants))]
    [:div.context-info.js-context-info
     [:div.circle-title (str "Círculo " id)
      [:span.tag (str "Nivel " level)]
      ]
     [:div.mod-idea
      [:div.intro "Idea más apoyada"]
      [:div.idea name]
      [:div.voting-block
       [:div.votes
        [:div.votes-count (str votes "/" max-participants " apoyos para promocionar")]
        [:div.progress-bar
         [:div.inner {:style {:width (str percentage "%")}}]
         ]
        ]
       ]
      ]
     [:div.mod-meta
      [:div.item
       [:i {:class "icon-header fa fa-lightbulb-o"}]
       [:span idea-count]
       ]
      [:div.item
       [:i {:class "icon-header fa fa-comments"}]
       [:span comment-count]
       ]
      [:div.item
       [:i {:class "icon-header fa fa-users"}]
       [:span users-count]
       ]
      ]
     ]
    ))

(rum/defc a-circle < rum/reactive
  [{:keys [id users level parent-circle-id] :as circle}]
  (let [state (rum/react d/state)
        in-path? (cis/circle-in-path? circle)
        active-circle? (cis/is-active-circle? circle)
        inner-circles (cis/get-inner-circles-for-circle circle)]
    [:div.circle.js-circle-w-context {:class (str "c-lv" level " " (when (nil? parent-circle-id) "root js-circle-root") " " (if in-path? "my-circle") " " (if active-circle? "active-circle"))
                  :on-click #(cis/visit-circle % circle)}
     (circle-context circle)
     (if (= level 1)
       (for [key users]
         [:div.circle.circle-user {:key key}])
       (for [{inner-id :id :as inner-circle} inner-circles]
         (-> (a-circle inner-circle)
             (rum/with-key inner-id))))
     ]))

(rum/defc ideas-resume < rum/reactive
  []
  (let [state (rum/react d/state)
        ideas (is/all-with-votes-sorted)]
    [:div {:class "map-list-view"}
     [:div.header "Ideas más apoyadas"]
     [:div.ideas-list
      (for [idea ideas]
        [:div.mod-idea {:key (:id idea)}
         [:div.supports
          [:div.value (:votes idea)]
          [:div.label " apoyos"]
          ]
         [:div.body
          [:div.idea (:name idea)]
          [:div.info  "Círculo " "??" " | " "nivel " "??"]
          (if (is/voted? idea)
            [:span.badge "la apoyaste"])
          ]
         ])
      ]
     ]))

(rum/defc the-map < rum/reactive
  []
  (let [state (rum/react d/state)]
    [:div.map
     [:div {:class "map-toggle-view js-map-toggle-view"}
      [:div {:class "map-toggle map-toggle-map" :data-balloon-pos "left" :data-balloon "Mapa de acuerdos"}
       [:i {:class "icon-header fa fa-lg fa-map"}]
       ]
      [:div {:class "map-toggle map-toggle-list" :data-balloon-pos "left" :data-balloon "Ideas más votadas"}
       [:i {:class "icon-header fa fa-lg fa-list"}]
       ]
      ]
     (ideas-resume)
     [:div {:class "map-circles-view"}
      (for [level (cis/get-level-range)]
        (let [parent-circles (cis/get-parent-circles-for-level level)]
          (for [circle parent-circles]
            (-> (a-circle circle)
                (rum/with-key (:id circle))))))
      ]
     ]))
