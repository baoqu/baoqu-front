(ns baoqu.http
  (:refer-clojure :exclude [get])
  (:require [httpurr.client.xhr :as c]
            [promesa.core :as p]))

(def default-opts {:headers {"Content-Type" "application/json"}})

(defn encode
  [data]
  (-> data
      (clj->js)
      (js/JSON.stringify)))

(defn decode
  [data]
  (-> data
      (:body)
      (js/JSON.parse)
      (js->clj :keywordize-keys true)))

(defn get
  [url]
  (-> (c/get url default-opts)
      (p/then decode)))

(defn post
  [url body]
  (let [encoded-body (encode body)
        opts (merge default-opts {:body encoded-body})]
    (-> (c/post url opts)
        (p/then decode))))
