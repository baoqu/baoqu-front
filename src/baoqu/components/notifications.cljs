(ns baoqu.components.notifications
  (:require [rum.core :as rum]
            [baoqu.data :as d]))

(rum/defc top < rum/reactive
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
