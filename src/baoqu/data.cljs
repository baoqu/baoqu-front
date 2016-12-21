(ns baoqu.data)

(defn get-initial-state
  []
  {:me {}
   :event {}
   :users {}
   :ideas {}
   :circles {}
   :comments {}
   :active-section "map"
   :active-circle nil
   :notification {}})

(defonce state (atom (get-initial-state)))
