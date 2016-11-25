(ns baoqu.components.circles
  (:require [rum.core :as rum]
            [baoqu.data :as d]))

(declare my-circle)

(rum/defc circle-context < rum/static
  [circle]
  (let [level (get circle "level")
        circle-size (get-in @d/state [:event :circle-size])
        percentage (* 100 (/ (:most-popular-idea-votes circle) (* circle-size level)))]
    [:div.context-info.js-context-info
     [:div.circle-title (get circle "name")
      [:span.tag (str "Nivel " level)]
      ]
     [:div.mod-idea
      [:div.intro "Idea más apoyada"]
      [:div.idea (:most-popular-idea circle)]
      [:div.voting-block
       [:div.votes
        [:div.votes-count (str (:most-popular-idea-votes circle) "/" (* circle-size level) " apoyos para promocionar")]
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

(rum/defc a-circle < rum/static
  [circle]
  (let [users (get circle "users")
        level (get circle "level")
        circle-size (get-in @d/state [:event :circle-size])
        circles (:circles @d/state)
        parent (get circle "parent-circle-id")
        my-circle (= (get circle "id") (get-in @d/state [:circle "id"]))
        inner-circles-ids (into #{} (get circle "inner-circles"))
        inner-circles (when inner-circles-ids
                        (filter (comp inner-circles-ids #(get % "id")) circles))]
    [:div.circle {:key (get circle "id") :class (str "c-lv" level " " (when (nil? parent) "root js-circle-root") " " (if my-circle "my-circle"))}
     (circle-context circle)
     (if (= level 1)
       (repeat users
           [:div.circle])
       (for [inner-circle inner-circles]
         (a-circle inner-circle)))
     ]))

(rum/defc the-map < rum/reactive
  []
  (let [state (rum/react d/state)
        all-circles (:circles state)
        ideas (:ideas state)]
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
         (for [[_ idea] ideas]
           [:div.mod-idea
            [:div.supports
             [:div.value (get idea "votes")]
             [:div.label " apoyos"]
             ]
            [:div.body
             [:div.idea (get idea "name")]
             [:div.info  "Círculo " (:circle idea) " | " "nivel " (:level idea)]
             (if (get idea "voted?")
               [:span.badge "la apoyaste"])
             ]
            ])
        ]
      ]
      [:div {:class "map-circles-view"}
     (for [level (reverse (range 1 4))]
       (let [parent-circles (filter #(and (= (get % "level") level) (nil? (get % "parent-circle-id"))) all-circles)]
         (for [circle parent-circles]
           (a-circle circle))))
           ]
     ]))
