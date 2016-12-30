(ns baoqu.utils
  (:require [clojure.string :refer [join]]
            [baoqu.config :as cfg]))

(enable-console-print!)

(defn get-params-string
  [params]
  (join "&" (for [[k v] params]
              (str (name k) "=" v))))

(defn ->url
  ([path]
   (->url path {}))
  ([path params]
   (let [base-url cfg/url]
     (if-not (empty? params)
       (str base-url path)
       (let [params-string (get-params-string params)]
         (str base-url path "?" params-string))))))

(defn list->map
  ([data key]
   (into {} (map #(do {(get % key) %}) data)))
  ([data]
   (into {} (map #(do {(:id %) %}) data))))
