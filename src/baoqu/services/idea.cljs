(ns baoqu.services.idea
  (:require [clojure.set :as set]
            [baoqu.data :as d]
            [baoqu.form-utils :as fu]
            [baoqu.api :as api]
            [baoqu.repos.user :as ur]
            [baoqu.repos.idea :as ir]))

(enable-console-print!)

(defn idea-exists?
  [idea-body]
  (let [ideas (:ideas @d/state)]
    (some #(= (get (second %) "name") idea-body) ideas)))

(defn add-idea-if-new
  [idea]
  (let [ideas (:ideas @d/state)
        idea-id (get idea "id")
        ideas-ids (into #{} (for [[k v] ideas] k))
        my-id (get-in @d/state [:me :id])
        mine? (= my-id (get-in idea ["user" "id"]))]
    (if-not (boolean (ideas-ids idea-id))
      (swap! d/state update :ideas merge {idea-id idea}))))

(defn add-idea-req
  [body]
  (let [user-id (get-in @d/state [:me :id])]
    (if-not (empty? body)
      (do
        (if-not (idea-exists? body)
          (api/upvote-idea user-id body))
        (fu/empty-f :idea)))))

(defn react-to-upvote
  [id user-id]
  (let [my-id (get-in @d/state [:me :id])]
    (if (= my-id user-id)
      (swap! d/state update-in [:ideas id "voted?"] not))
    (swap! d/state update-in [:ideas id "votes"] inc)))

(defn react-to-downvote
  [id user-id]
  (let [my-id (get-in @d/state [:me :id])]
    (if (= my-id user-id)
      (swap! d/state update-in [:ideas id "voted?"] not))
    (swap! d/state update-in [:ideas id "votes"] dec)))

(defn toggle-idea-vote-req
  [id]
  (fn []
    (let [voted? (get-in @d/state [:ideas id "voted?"])
          user-id (get-in @d/state [:me :id])
          idea-name (get-in @d/state [:ideas id "name"])]
      (if voted?
        (api/upvote-idea user-id idea-name)
        (api/downvote-idea user-id idea-name)))))

(defn all-ideas
  []
  (ir/get-ideas))

(defn all-ideas-count
  []
  (count (all-ideas)))

(defn all-votes
  []
  (ir/get-votes))

(defn votes-for-idea
  [{:keys [id]}]
  (filter #(= id (:idea-id %)) (ir/get-votes)))

(defn votes-for-idea-and-circle
  [idea {circle-id :id}]
  (let [circle-users (ur/get-all-for-circle circle-id)
        user-ids (into #{} (map :id) circle-users)
        idea-votes (votes-for-idea idea)]
    (filter #(user-ids (:user-id %)) idea-votes)))

(defn vote-count-for-idea
  [{:keys [id] :as idea}]
  (count (votes-for-idea idea)))

(defn vote-count-for-idea-and-circle
  [idea circle]
  (count (votes-for-idea-and-circle idea circle)))

(defn voted?
  [idea]
  (let [me (ur/get-me)]
    (some #(= (:user-id %) (:id me)) (votes-for-idea idea))))

(defn my-votes
  []
  (let [all-votes (all-votes)
        {user-id :id} (ur/get-me)]
    (filter #(= (:user-id %) user-id) all-votes)))

(defn approval-percentage
  [{:keys [id] :as idea}]
  ;; get active circle size
  ;; get idea votes ;; (get-idea-votes idea)
  ;; (* 100 (/ votes active-circle-size))
  )

(defn sort-ideas
  []
  ;;(ir/get-ideas))
  )

(defn get-all-for-circle
  [{:keys [id]}]
  (let [users (ur/get-all-for-circle id)
        idea-ids (reduce (fn [acc {:keys [ideas]}]
                           (set/union acc (into #{} ideas)))
                         #{}
                         users)]
    (mapv ir/get-idea-by-id idea-ids)))

(defn get-all-voted-for-circle
  [circle]
  (let [{:keys [id]} (ur/get-me)
        all-ideas (get-all-for-circle circle)
        my-votes (my-votes)
        voted-ideas-ids (into #{} (map :idea-id my-votes))]
    (filter #(voted-ideas-ids (:id %)) all-ideas)))

(defn count-all-for-circle
  [{:keys [id]}]
  (count (get-all-for-circle id)))

(defn voted-filter-active?
  []
  (ir/get-voted-filter))

(defn set-voted-filter
  [active?]
  (ir/set-voted-filter active?))
