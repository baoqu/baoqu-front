(ns baoqu.data)

(defn get-initial-state
  []
  ;; {:event {} :circles [] :participants [] :session {}})
  {:event {:id 1
           :name "¿Cuando vamos a ver Star Wars?"
           :circle-size 3
           :approval-factor 2
           :circles [1 2]}
   :circle 1
   :circles [{:id 1
              :level 1
              :name "Circle 1"
              :most-voted-idea "The idea itself"
              :num-comments 3
              :num-ideas 3}
             {:id 2
              :level 2
              :name "Circle 2"
              :participants 3
              :most-voted-idea "The idea itself"
              :num-comments 50
              :num-ideas 7}]
   :ideas [{:id 1
            :body "Mi idea 1"
            :votes 2}
           {:id 2
            :body "Mi idea 2"
            :votes 3}
           {:id 3
            :body "Mi idea 3"
            :votes 1}]
   :comments [{:id 1
               :author 1
               :body "¿Qué mierda es esta?"
               :date "2015-12-15T16:24:38.395Z"}
              {:id 2
               :author 2
               :body "¿Sabes quién era nazi también?"
               :date "2015-12-15T17:24:38.395Z"}
              {:id 3
               :author 1
               :body "LOS ALIENS"
               :date "2015-12-15T17:24:38.395Z"}]
   :participants [{:id 1
                   :name "Miguel"}
                  {:id 2
                   :name "Andy"}]
   :session {:username "Miguel"}})

(defonce state (atom (get-initial-state)))
