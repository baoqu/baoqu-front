(ns baoqu.http
  (:refer-clojure :exclude [get])
  (:require [httpurr.client.xhr :as c]
            [promesa.core :as p]
            [baoqu.repos.user :as ur]))

(def default-opts {:headers {"Content-Type" "application/json"}})

(defn default-opts-with-token
  []
  (let [token (ur/get-token)]
    (if token
      (as-> (:headers default-opts) x
          (merge x {:authorization (str "Bearer " token)})
          (assoc default-opts :headers x))
      default-opts)))

(defn encode
  [data]
  (-> data
      (clj->js)
      (js/JSON.stringify)))

(defn decode
  [{:keys [body]}]
  (if-not (empty? body)
    (-> body
        (js/JSON.parse)
        (js->clj :keywordize-keys true))))

(defn get
  [url]
  (-> (c/get url (default-opts-with-token))
      (p/then decode)))

(defn post
  [url body]
  (let [encoded-body (encode body)
        opts (merge (default-opts-with-token) {:body encoded-body})]
    (-> (c/post url opts)
        (p/then decode))))
