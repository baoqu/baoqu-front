(ns baoqu.utils
  (:require [clojure.string :refer [join]]
            [baoqu.config :refer [cfg]]))

(enable-console-print!)

(defn ->kwrds
  [m]
  (into {}
        (for [[k v] m]
          (if (map? v)
            [(keyword k) (->kwrds v)]
            [(keyword k) v]))))

(defn get-params-string
  [params]
  (join "&" (for [[k v] params]
              (str (name k) "=" v))))

(defn ->url
  ([path]
   (->url path {}))
  ([path params]
   (let [base-url (:server cfg)]
     (if-not (empty? params)
       (str base-url path)
       (let [params-string (get-params-string params)]
         (str base-url path "?" params-string))))))
