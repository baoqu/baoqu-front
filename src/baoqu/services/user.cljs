(ns baoqu.services.user
  (:require [promesa.core :as p]
            [baoqu.api :as api]
            [baoqu.repos.user :as ur]))

(enable-console-print!)

(defn login
  [username password]
  (-> (api/login username password)
      (p/then (fn [{:keys [status id username token]}]
                (if (= status "ok")
                  (ur/set-me id username token)
                  (throw (js/Error. "Invalid credentials")))))))

(defn register
  [username password]
  (-> (api/register username password)
      (p/then (fn [{:keys [message]}]
                (if message
                  (throw (js/Error. message)))))))
