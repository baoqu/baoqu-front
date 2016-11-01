(ns baoqu.components.circles
  (:require [rum.core :as rum]
            [baoqu.data :as d]))

(declare my-circle)

(rum/defc a-circle < rum/static
  [circle]
  (let [users (get circle "users")
        level (get circle "level")
        circle-size (get-in @d/state [:event :circle-size])
        circles (:circles @d/state)
        percentage (* 100 (/ (:most-popular-idea-votes circle) (* circle-size level)))
        parent (get circle "parent-circle-id")
        inner-circles-ids (into #{} (get circle "inner-circles"))
        inner-circles (when inner-circles-ids
                        (filter (comp inner-circles-ids #(get % "id")) circles))]
    [:div.circle {:class (str "c-lv" level " " (when (nil? parent) "root js-circle-root"))}
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
     (if (= level 1)
       (repeat users
           [:div.circle])
       (for [inner-circle inner-circles]
         (let [my-circle? (= (:id inner-circle) (:circle @d/state))]
           (if my-circle?
             (my-circle inner-circle)
             (a-circle inner-circle)))))
     ]))

(rum/defc my-circle < rum/static
  [circle]
  (let [level (get circle "level")
        users (get circle "users")
        circle-size (get-in @d/state [:event :circle-size])
        circles (:circles @d/state)
        percentage (* 100 (/ (:most-popular-idea-votes circle) circle-size))
        parent (:parent-circle circle)
        inner-circles-ids (into #{} (:inner-circles circle))
        inner-circles (when inner-circles-ids
                        (filter (comp inner-circles-ids :id) circles))]
    [:div.circle.my-circle {:class (str "c-lv" (:level circle) " " (when (nil? parent) "root js-circle-root"))
                  :key (str (:id circle))}
     [:div.context-info.js-context-info
      [:div.circle-title (:name circle)
       [:span.tag (str "Nivel " (:level circle))]
       ]
      [:div.mod-idea
       [:div.intro "Idea más apoyada"]
       [:div.idea (:most-voted-idea circle)]
       [:div.voting-block
        [:div.votes
         [:div.votes-count (str (:most-popular-idea-votes circle) "/" circle-size " apoyos para promocionar")]
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
     (if (= level 1)
       (let [;; old stuff in let vvv
             size (get-in @d/state [:event :circle-size])
             most-popular-idea-votes (apply max (map (comp :votes last) (:ideas @d/state)))
             delta (- size most-popular-idea-votes)
             has-voted-vector (concat (repeat most-popular-idea-votes true) (repeat delta false))]
;         (repeat users
;                 [:div.circle])
       (for [inner-circle inner-circles]
         (a-circle inner-circle))))
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
