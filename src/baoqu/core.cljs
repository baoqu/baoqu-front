(ns baoqu.core
  (:require [rum.core :as rum]
            [goog.dom :as dom]
            [bidi.router :as bidi]
            [httpurr.status :as s]
            [httpurr.client.xhr :as xhr]
            [promesa.core :as p]
            ))

(defonce state (atom {}))

;; ROUTES
(def routes ["/" [["home" :home]
                  ["index" :index]
                  [["event/" :id] :event]])

(defn- on-navigate
  [{route :handler}]
  (swap! state assoc :route route))

(defonce +router+
  (bidi/start-router! routes {:on-navigate on-navigate
                              :default-location {:handler :home}}))

(defn empty-form []
  (swap! state assoc :form nil))

(defn login-action []
  (let [username (get-in @state [:form :username])]
    (swap! state assoc-in [:session :username] username)
    (empty-form)

    ;; if (error)
    ;;   (swap! state assoc-in [:errors :username] error-message)
    ))



(defn change-field [& path]
  (fn [e]
    (let [new-value (-> e (.-target) (.-value))]
      (enable-console-print!)
      (println "====================")
      (println new-value)
      (println "====================")
      (swap! state assoc-in path new-value))))

(def change-in-form (partial change-field :form))

(rum/defc login < rum/reactive
  []
  (let [state (rum/react state)
        username (get-in state [:form :username])]
    [:div
     [:h2 "Login"]
     [:input {:class "bt" :placeholder "login input"
              :on-change (change-in-form :username)
              :value username}]
     [:button {:on-click login-action} "login"]]))










(defn on-error
  [message & path]
  (swap! state assoc-in path message))

(defn join-event-success
  [response]
  (let [parsed-response (js/json.Parse (:body response))]
    ;; change state
    (swap! state assoc :event (:id parsed-response))
    ;; change url??
    (swap! state assoc :route :event)))

(defn join-event-request
  []
  ;; http request
  ;; on-success => join-event-success
  ;; on-error => on-error
  (on-error "id not found" :errors :event :id)
  )

(defn join-event-handler
  []
  (let [event-id 1
        username (get-in @state [:session :username])]
    (send-request :get url params on-success on-error)
    )



;; request
(defn join-event-request
  []
  (defn eid->url
    [event-id]
    (str "http://private-a24a2a-baoqu.apiary-mock.com/events/" :event_id "/users"))

  (defn process-response
    [response]
    (condp = (:status response)
      s/ok           (p/resolved (:body response))
      s/not-found    (p/rejected :not-found)
      s/unauthorized (p/rejected :unauthorized)))

  (-> (p/then (xhr/get (eid->url event-id))
              process-response)
      (p/catch (fn [err]
                 (.error js/console err))))
  )

(defn join-event []
  (let [event-id 1
        username (get-in @state [:session :username])]

    (enable-console-print!)
    (println "====================")
    (println "hola")
    (println "====================")

    (join-event-request event-id username)
    )

(rum/defc home < rum/reactive
  []
  [:div
   [:h2 "Home"]
   [:button#join-event {:on-click join-event} "Join Event"]
    ])

(rum/defc base < rum/reactive
  []
  (let [state (rum/react state)
        username (get-in state [:session :username])]
    (if username
      (home)
      (login))))

;; MOUNT
(rum/mount (base) (dom/getElement "content"))
