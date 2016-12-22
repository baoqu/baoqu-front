(ns baoqu.services.idea
  (:require [clojure.set :as set]
            [baoqu.data :as d]
            [baoqu.form-utils :as fu]
            [baoqu.api :as api]
            [baoqu.repos.user :as ur]
            [baoqu.repos.idea :as ir]))

(enable-console-print!)

(declare voted?)

(defn get-by-id
  [id]
  (ir/get-idea-by-id id))

(defn get-by-name
  [name]
  (ir/get-idea-by-name name))

(defn idea-exists?
  [idea-body]
  (let [ideas (:ideas @d/state)]
    (some #(= (get (second %) "name") idea-body) ideas)))

(defn add-idea-if-new
  [{:keys [id name]}]
  (if-not (get-by-name name)
    (ir/add-idea id name)))

(defn add-vote
  [user-id idea-id]
  (ir/add-vote user-id idea-id)
  (ur/add-idea-to-user user-id idea-id))

(defn remove-vote
  [user-id idea-id]
  (ir/remove-vote user-id idea-id)
  (ur/remove-idea-from-user user-id idea-id))

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
  [e {:keys [name] :as idea}]
  (.preventDefault e)
  (let [{user-id :id} (ur/get-me)]
    (if (voted? idea)
      (api/downvote-idea user-id name)
      (api/upvote-idea user-id name))))

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

(defn all-with-votes
  []
  (mapv #(assoc % :votes (vote-count-for-idea %)) (all-ideas)))

(defn all-with-votes-sorted
  []
  (->> (all-with-votes)
       (sort-by :votes)
       (reverse)))

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
  ;; Not valid approach
  ;;
  ;; (let [active-circle (ur/get-active-circle)
  ;;       res (->> (all-ideas)
  ;;                (mapv #(assoc % :votes (vote-count-for-idea-and-circle % active-circle)))
  ;;                (sort-by :votes)
  ;;                (reverse))]
  ;;   (ir/set-ideas res))
  )

(defn get-all-for-circle
  "Get all the ideas which have votes in a given circle"
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
  [circle]
  (count (get-all-for-circle circle)))

(defn voted-filter-active?
  []
  (ir/get-voted-filter))

(defn set-voted-filter
  [active?]
  (ir/set-voted-filter active?))

(defn get-highest-voted-idea-for-circle
  "Returs the highest voted idea of a given circle"
  [circle]
  (last (->> (get-all-for-circle circle)
             (mapv #(assoc % :votes (vote-count-for-idea-and-circle % circle)))
             (sort-by :votes))))
