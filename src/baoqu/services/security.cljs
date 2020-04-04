(ns baoqu.services.security
  (:require [baoqu.data :as d]))

(enable-console-print!)

(defn get-username
  []
  (get-in @d/state [:me :username]))

(def is-authenticated? (comp boolean get-username))
