(ns baoqu.components.notifications
  (:require [rum.core :as rum]
            [baoqu.data :as d]
            [baoqu.services.notification :as ns]))

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
        notification (:notification state)]
    [:div
     [:div.modal {:class "show"}
      [:div.modal-inner
       [:div.modal-title (:title notification)]
       [:div.modal-body (:description notification)]
       [:div.btn.btn-primary.btn-large {:on-click ns/clear-notification} "OK!"]
       [:i.fa.fa-2x.fa-close.modal-close {:on-click ns/clear-notification}]
       ]
      ]
     ]
    ))
