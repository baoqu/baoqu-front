(ns baoqu.data)

(defn get-initial-state
  []
  ;; {:event {} :circles [] :participants [] :session {} :active-section "map"})
  {:event {:id 1
           :name "¿Cuando vamos a ver Star Wars?"
           :circle-size 3
           :approval-factor 2
           :circles [1 2]}
   :circle 1
   :active-section "ideas"
   :circles [{:id 1
              :level 1
              :name "Circle 1"
              :most-popular-idea "The idea itself"
              :most-popular-idea-votes 2
              :num-comments 3
              :num-ideas 3
              :parent-circle 2}
             {:id 2
              :level 2
              :name "Circle 2"
              :participants 3
              :most-popular-idea "The idea itself"
              :most-popular-idea-votes 1
              :num-comments 50
              :num-ideas 7
              :inner-circles [1 3]}
             {:id 3
              :level 1
              :name "Circle 3"
              :most-popular-idea "The idea itself"
              :most-popular-idea-votes 3
              :num-comments 3
              :num-ideas 3
              :parent-circle 2}
             {:id 4
              :level 1
              :name "Circle 4"
              :most-popular-idea "The idea itself"
              :most-popular-idea-votes 3
              :num-comments 3
              :num-ideas 3}]
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
