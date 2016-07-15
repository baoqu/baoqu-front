(ns baoqu.components.home
  (:require [rum.core :as rum]
            [baoqu.data :as d]
            [baoqu.mixins :as mixins]
            [baoqu.form-utils :as fu]
            [baoqu.services.comment :as cs]
            [baoqu.services.idea :as is]
            [clojure.string :as s]))

(enable-console-print!)

(rum/defc notifications < rum/reactive
  []
  (let [state (rum/react d/state)
        active-section (:active-section state)
        active-notification (:active-notification state)]
    [:div.notifications
     [:div.notification.success {:class (if (= active-notification "success") "active" "")} "Yay!"
      [:div.fa.fa-close]
      ]
     [:div.notification.error {:class (if (= active-notification "error") "active" "")} "Nay!"
      [:div.fa.fa-close]
      ]
     [:div.notification.info {:class (if (= active-notification "info") "active" "")} " ¯|_(ツ)_|¯ "
      [:div.fa.fa-close]
      ]
     ]))


(rum/defc header < rum/reactive
  []
  (let [state (rum/react d/state)
        event (:event state)
        circle-id (:circle state)
        circles (:circles state)
        circle (first (filter #(= circle-id (:id %)) circles))
        num-ideas (count (:ideas state))
        num-comments (count (:comments state))
        active-section (:active-section state)
        active-modal (:active-modal state)]

    [:div.header-wrapper
     [:div#mainHeader
       [:div.logo-icon]
       [:h1.logo "Baoqu"]
       [:div.event-name (:name event)]
       [:div.event-info-toggle.js-event-info-toggle {:data-balloon-pos "left" :data-balloon "Información del evento"}
        [:div.fa.fa-plus]
       ]
      (letfn [(change-notification [notification] #(swap! d/state assoc :active-notification notification))]
        [:div.test-notifications.hide
          [:div.fa.fa-send
            [:ul
              [:li {:on-click (change-notification "success")} "success"]
              [:li {:on-click (change-notification "error")} "error"]
              [:li {:on-click (change-notification "info")} "info"]
            ]
          ]
        ]
      )
      (letfn [(change-modal [modal] #(swap! d/state assoc :active-modal modal))]
        [:i.fa.fa-lg.fa-windows.hide {:on-click (change-modal "show") :style {:margin-right "30px" :cursor "pointer"}}]
      )
     ]

     [:div.header-event-info.js-event-info
      [:div.inner "El Ayuntamiento (Urbanismo y Transportes), Vecinos y Colectivos se han de reunir para trazar un plan a largo plazo para la progresiva incorporación de la bicicleta como medio de transporte"]
      ]

     (letfn [(change-section [section] #(swap! d/state assoc :active-section section))]
       [:ul.mobile-menu
        [:li {:class (if (= active-section "map") "active" "") :on-click (change-section "map")}
         [:i {:class "icon-header fa fa-lg fa-map"}]
         [:span.title "Mapa"]
         ]
        [:li {:class (if (= active-section "ideas") "active" "") :on-click (change-section "ideas")}
         [:i {:class "icon-header fa fa-lg fa-lightbulb-o"}]
         [:span.title (str num-ideas " ideas")]
         ]
        [:li {:class (if (= active-section "comments") "active" "") :on-click (change-section "comments")}
         [:i {:class "icon-header fa fa-lg fa-comments"}]
         [:span.title (str num-comments " comentarios")]
         ]
        ])
     ]))

(rum/defc footer < rum/reactive
  []
  (let [state (rum/react d/state)
        username (get-in state [:me :name])
        initial (s/upper-case (first username))]
    ;; [:div#mainFooter "user movidas"]
    [:div#user
     [:div.avatar
      [:div.thumb initial]]]))

(rum/defc ideas < rum/reactive
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
              [:div.votes-count (str votes "/" circle-size " apoyos para promocionar el círculo")]
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
      [:input {:placeholder "# Añade una nueva idea"
               :on-change (fu/change-in-form :idea)
               :value idea}]
      [:button.button {:on-click is/add-idea-req}
       [:i {:class "fa fa-lg fa-plus"}]]]]))

(rum/defc comments < rum/reactive
  []
  (let [state (rum/react d/state)
        comment (fu/get-f :comment)
        event (:event state)
        circle (:circle state)
        circles (:circles state)
        comments (:comments state)
        num-comments (count comments)]
    [:div.mod-comments
     [:div.mod-header
      [:span {:class "expander js-expand-comments"}
        [:i {:class "icon-header fa fa-lg fa-comments"}]
      ]
      [:div.title (str "Comentarios (" num-comments ")")]
      [:span.toggle
       [:i {:class "fa fa-lg fa-chevron-right js-collapse-comments"}]]]

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
      ]
     [:div.mod-add-box
      [:input {:placeholder "Comenta"
               :on-change (fu/change-in-form :comment)
               :value comment}]
      [:button.button {:on-click cs/add-comment}
       [:i {:class "fa fa-lg fa-plus"}]]]]))

(rum/defc workspace < rum/reactive
  []
  (let [state (rum/react d/state)
        circle (:circle state)]
    [:div.circle-wrapper
     [:div.circle-header
      [:div.circle-header-title (str "Círculo " (get circle "id"))
        [:span.tag
          [:span "Nivel "]
          [:span (get circle "level")]
        ]
        [:span.tag.my-circle "Mi círculo"]
      ]
      [:span.circle-header-exit
        [:span "Salir "]
        [:span.hide-medium " de este círculo"]
      ]]
     [:div.circle-content
      (ideas)
      (comments)]]))

(declare my-circle)

(rum/defc a-circle < rum/static
  [circle]
  (let [level (get circle "level")
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
       (for [participant (:participants circle)]
         [:div.circle])
       (for [inner-circle inner-circles]
         (let [my-circle? (= (:id inner-circle) (:circle @d/state))]
           (if my-circle?
             (my-circle inner-circle)
             (a-circle inner-circle)))))
     ]))

(rum/defc my-circle < rum/static
  [circle]
  (let [level (:level circle)
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
     (if (= (:level circle) 1)
       (let [size (get-in @d/state [:event :circle-size])
             most-popular-idea-votes (apply max (map (comp :votes last) (:ideas @d/state)))
             delta (- size most-popular-idea-votes)
             has-voted-vector (concat (repeat most-popular-idea-votes true) (repeat delta false))]
         (for [has-voted? has-voted-vector]
           (if has-voted?
             [:div.circle.agreed]
             [:div.circle])))
       (for [inner-circle inner-circles]
         (a-circle inner-circle)))
     ]))

(rum/defc the-map < rum/reactive
  []
  (let [state (rum/react d/state)
        all-circles (:circles state)]
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
        (for [idea [{:supports 129 :supported? false :body "Las bicis son para hippies, ¡cómo se nota que no trabajais!" :circle "Onosizumi" :level "2"}
                    {:supports 28 :supported? true :body "Nos hacen falta más carriles bici" :circle "Onosizumi" :level "2"}
                    {:supports 18 :supported? false :body "Cómo conectar con el centro: bulevar en Juan Bravo" :circle "Onosizumi" :level "2"}
                    {:supports 8 :supported? true :body "Si empezamos por lo más elemental, tendremos que decir que aeróbico significa trabajo en presencia de oxígeno" :circle "Onosizumi" :level "2"}
                    {:supports 7 :supported? false :body "Hay que arreglar los baches, son un peligro" :circle "Onosizumi" :level "2"}
                    {:supports 6 :supported? false :body "Nos vamos a comer un arroz con bacalao" :circle "Onosizumi" :level "2"}
                    {:supports 6 :supported? false :body "WHATEVER" :circle "Onosizumi" :level "2"}
                    {:supports 6 :supported? false :body "Aprenda de bolsa con el nº1 en España en CFDs" :circle "Onosizumi" :level "2"}
                    {:supports 6 :supported? true :body "Cómo conectar con el centro: bulevar en Juan Bravo" :circle "Onosizumi" :level "2"}
                    {:supports 1 :supported? false :body "¿Cómo apoyo una idea que me gusta?" :circle "Onosizumi" :level "2"}
                    {:supports 1 :supported? false :body "Si la cosa funciona" :circle "Onosizumi" :level "2"}
                    {:supports 1 :supported? false :body "Hay que arreglar los baches, son un peligro" :circle "Onosizumi" :level "2"}
                    {:supports 1 :supported? false :body "Cómo conectar con el centro: bulevar en Juan Bravo" :circle "Onosizumi" :level "2"}
                    {:supports 1 :supported? false :body "Alien es del 79" :circle "Onosizumi" :level "2"}]]
                    [:div.mod-idea
                      [:div.supports
                        [:div.value (:supports idea)]
                        [:div.label " apoyos"]
                      ]
                      [:div.body
                        [:div.idea (:body idea)]
                        [:div.info  "Círculo " (:circle idea) " | " "nivel " (:level idea)]
                        (if (:supported? idea)
                          [:span.badge "la apoyaste"])
                      ]
                    ])
        ]
      ]
      [:div {:class "map-circles-view"}
        [:div {:class "circle my-circle c-lv1 c-c4 REMOVE"}
          [:div {:class "circle"}]
          [:div {:class "circle"}]
          [:div {:class "circle"}]
          [:div {:class "circle"}]
        ]
     (for [level (reverse (range 1 4))]
       (let [parent-circles (filter #(and (= (get % "level") level) (nil? (:parent-circle %))) all-circles)]
         (for [circle parent-circles]
           (a-circle circle))))
           ]
     ]))

(rum/defc container < rum/reactive
  []
  (let [state (rum/react d/state)
        active-section (:active-section state)]
    [:div.container.event-wrapper {:class (str "mobile-show-" active-section)}
     (the-map)
     (workspace)]))

(rum/defc modaltesting < rum/reactive
 []
 (let [state (rum/react d/state)
 active-modal (:active-modal state)]
   (letfn [(change-modal [modal] #(swap! d/state assoc :active-modal modal))]
     [:div
       [:div.modal {:class active-modal}
        [:div.modal-inner
          [:div.modal-title "Modal title"]
          [:div.modal-body "Modal body"]
          [:i.fa.fa-lg.fa-close.modal-close {:on-click (change-modal "")}]
        ]
       ]
     ]
   )
 ))

(rum/defc main < rum/reactive mixins/secured-mixin mixins/connect-see-mixin
  "The main component for the home screen"
  []
  [:div.page
   (notifications)
   (header)
   (container)
   (footer)
   (modaltesting)])
