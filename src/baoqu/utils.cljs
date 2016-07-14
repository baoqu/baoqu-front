(ns baoqu.utils)

(defn ->kwrds
  [m]
  (into {}
        (for [[k v] m]
          [(keyword k) v])))
