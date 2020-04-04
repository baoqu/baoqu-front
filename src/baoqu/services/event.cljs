(ns baoqu.services.event
  (:require [baoqu.http :as http]
            [promesa.core :as p]
            [baoqu.data :as d]
            [baoqu.api :as api]
            [baoqu.routes :as routes]
            [baoqu.repos.user :as user-r]
            [baoqu.repos.idea :as idea-r]
            [baoqu.repos.comment :as comment-r]
            [baoqu.repos.circle :as circle-r]
            [baoqu.repos.event :as event-r]))

(enable-console-print!)

(defn fetch-events
  []
  (-> (api/get-all-events)
      (p/then (fn [res]
                (event-r/set-events res)))))

(defn fetch-event-data
  [event-id]
  (-> (api/get-event event-id)

      (p/then (fn [{:keys [id name agreement-factor circle-size]}]
                (event-r/set-event id name agreement-factor circle-size)
                (api/get-event-circles event-id)))

      (p/then (fn [res]
                (let [user-id (:id (user-r/get-me))]
                  (circle-r/set-circles res)
                  (api/get-user-path user-id))))

      (p/then (fn [res]
                (user-r/set-my-path res)
                (user-r/set-active-circle (first res))
                (api/get-event-users event-id)))

      (p/then (fn [res]
                (user-r/set-users res)
                (api/get-event-ideas event-id)))

      (p/then (fn [res]
                (idea-r/set-ideas res)
                (api/get-event-comments event-id)))

      (p/then (fn [res]
                (comment-r/set-comments res)
                (api/get-event-votes event-id)))

      (p/then idea-r/set-votes)))

(defn join-event
  [event-id username]
  (-> (api/join-event event-id username)
      (p/then (fn [{:keys [id name]}]
                (user-r/set-me id name "")
                (fetch-event-data event-id)))
      (p/catch #(println (str "[HTTP-ERROR]>> " %)))))

(defn reload-event-data
  []
  (let [event-id (get-in @d/state [:event :id])]
    (fetch-event-data event-id)))

(defn visit-event
  [e {:keys [id name] :as event}]
  (.preventDefault e)
  (.stopPropagation e)
  (println (str "Visiting event #" id ": " name))
  (event-r/set-event event)
  (routes/go :home))
