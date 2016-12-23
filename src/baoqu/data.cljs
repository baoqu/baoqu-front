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
   :voted-filter false
   :sorted-filter false
   :notification {}})

(defonce state (atom (get-initial-state)))
