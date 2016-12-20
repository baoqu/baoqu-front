(ns baoqu.http
  (:require [httpurr.client.xhr :as c]
            [baoqu.utils :refer [->kwrds]]))

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
      (js->clj)))

(defn get
  [url]
  (c/get url default-opts))

(defn post
  [url body]
  (let [encoded-body (encode body)
        opts (merge default-opts {:body encoded-body})]
    (c/post url opts)))

(defn res->kwrds
  [res]
  (-> (decode res)
      (->kwrds)))
