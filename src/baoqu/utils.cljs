(ns baoqu.utils)

(defn ->kwrds
  [m]
  (into {}
        (for [[k v] m]
          (if (map? v)
            [(keyword k) (->kwrds v)]
            [(keyword k) v]))))
