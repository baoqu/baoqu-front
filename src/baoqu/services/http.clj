(ns baoqu.services.http
  (:require [httpurr.client :as c]))

(def default-opts {:headers {"Content-Type" "application/json"}})

(defn get
  [url]
  (c/get url default-opts))

(defn post
  [url body]
  (let [opts (merge default-opts {:body body})]
    (c/post url opts)))
