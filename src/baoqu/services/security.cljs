(ns baoqu.services.security
  (:require [baoqu.data :as d]))

(defn get-username
  []
  (get-in @d/state [:me :name]))

(def is-authenticated? (comp boolean get-username))
