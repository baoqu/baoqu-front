(ns baoqu.data)

(defn get-initial-state
  []
  {:event {} :circles [] :participants [] :session {}})

(defonce state (atom (get-initial-state)))
