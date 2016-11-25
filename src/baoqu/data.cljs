(ns baoqu.data)

(defn get-initial-state
  []
  {:event {}
   :me {}
   :circles []
   :participants []
   :active-section "map"
   :notification {}})

(defn get-old-state
  []
  {:event {:id 1
           :name "Probando Baoqu"
           :circle-size 3
           :approval-factor 2
           :circles [1 2]}
   :circle 1
   :active-section "ideas"
   :circles [{:id 1
              :level 1
              :name "Circle 1"
              :participants [1 2 3]
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
              :most-popular-idea-votes 4
              :num-comments 50
              :num-ideas 7
              :inner-circles [1 3 4]}
             {:id 3
              :level 1
              :participants [4 5 6]
              :name "Circle 3"
              :most-popular-idea "The idea itself"
              :most-popular-idea-votes 3
              :num-comments 3
              :num-ideas 3
              :parent-circle 2}
             {:id 4
              :level 1
              :participants [1 2 3]
              :name "Circle 4"
              :most-popular-idea "The idea itself"
              :most-popular-idea-votes 0
              :num-comments 3
              :num-ideas 3
              :parent-circle 2}
             {:id 5
              :level 3
              :name "Circle 4"
              :most-popular-idea "The idea itself"
              :most-popular-idea-votes 0
              :num-comments 3
              :num-ideas 3
              :inner-circles [6 7 8]}
             {:id 6
              :level 2
              :name "Circle 4"
              :most-popular-idea "The idea itself"
              :most-popular-idea-votes 0
              :num-comments 3
              :num-ideas 3
              :inner-circles [9 10 11]
              :parent-circle 5}
             {:id 7
              :level 2
              :name "Circle 4"
              :most-popular-idea "The idea itself"
              :most-popular-idea-votes 0
              :num-comments 3
              :num-ideas 3
              :inner-circles [12 13 14]
              :parent-circle 5}
             {:id 8
              :level 2
              :name "Circle 4"
              :participants [1 2 3]
              :most-popular-idea "The idea itself"
              :most-popular-idea-votes 0
              :num-comments 3
              :num-ideas 3
              :inner-circles [15 16 17]
              :parent-circle 5}
             {:id 9
              :level 1
              :participants [1 2 3]
              :name "Circle 4"
              :most-popular-idea "The idea itself"
              :most-popular-idea-votes 0
              :num-comments 3
              :num-ideas 3
              :parent-circle 6}
             {:id 10
              :level 1
              :participants [1 2 3]
              :name "Circle 4"
              :most-popular-idea "The idea itself"
              :most-popular-idea-votes 0
              :num-comments 3
              :num-ideas 3
              :parent-circle 6}
             {:id 11
              :level 1
              :participants [1 2 3]
              :name "Circle 4"
              :most-popular-idea "The idea itself"
              :most-popular-idea-votes 0
              :num-comments 3
              :num-ideas 3
              :parent-circle 6}
             {:id 12
              :level 1
              :participants [1 2 3]
              :name "Circle 4"
              :most-popular-idea "The idea itself"
              :most-popular-idea-votes 0
              :num-comments 3
              :num-ideas 3
              :parent-circle 7}
             {:id 13
              :level 1
              :participants [1 2 3]
              :name "Circle 4"
              :most-popular-idea "The idea itself"
              :most-popular-idea-votes 0
              :num-comments 3
              :num-ideas 3
              :parent-circle 7}
             {:id 14
              :level 1
              :participants [1 2 3]
              :name "Circle 4"
              :most-popular-idea "The idea itself"
              :most-popular-idea-votes 0
              :num-comments 3
              :num-ideas 3
              :parent-circle 7}
             {:id 15
              :level 1
              :participants [1 2 3]
              :name "Circle 4"
              :most-popular-idea "The idea itself"
              :most-popular-idea-votes 0
              :num-comments 3
              :num-ideas 3
              :parent-circle 8}
             {:id 16
              :level 1
              :participants [1 2 3]
              :name "Circle 4"
              :most-popular-idea "The idea itself"
              :most-popular-idea-votes 0
              :num-comments 3
              :num-ideas 3
              :parent-circle 8}
             {:id 17
              :level 1
              :participants [1 2 3]
              :name "Circle 4"
              :most-popular-idea "The idea itself"
              :most-popular-idea-votes 0
              :num-comments 3
              :num-ideas 3
              :parent-circle 8}
             {:id 18
              :level 1
              :participants [1 2 3]
              :name "Circle 4"
              :most-popular-idea "The idea itself"
              :most-popular-idea-votes 0
              :num-comments 3
              :num-ideas 3}
             {:id 19
              :level 1
              :participants [1 2 3]
              :name "Circle 4"
              :most-popular-idea "The idea itself"
              :most-popular-idea-votes 0
              :num-comments 3
              :num-ideas 3}
             {:id 20
              :level 1
              :participants [1 2 3]
              :name "Circle 4"
              :most-popular-idea "The idea itself"
              :most-popular-idea-votes 0
              :num-comments 3
              :num-ideas 3}
             {:id 21
              :level 1
              :participants [1 2 3]
              :name "Circle 4"
              :most-popular-idea "The idea itself"
              :most-popular-idea-votes 0
              :num-comments 3
              :num-ideas 3}]
   :ideas {1 {:id 1
              :body "Nos hacen falta más carriles bici"
              :is-voted false
              :votes 2}
           2 {:id 2
              :body "Las bicis son para hippies, ¡cómo se nota que no trabajais!"
              :is-voted false
              :votes 1}
           3 {:id 3
              :body "Cómo conectar con el centro: bulevar en Juan Bravo"
              :is-voted false
              :votes 0}
           4 {:id 4
              :body "Sin presupuestos es difícil hacer nada"
              :is-voted true
              :votes 2}
           5 {:id 5
              :body "Hay que arreglar los baches, son un peligro"
              :is-voted false
              :votes 1}}
   :comments [{:id 1
               :author 5
               :body "¿Cómo apoyo una idea que me gusta?"
               :date "2015-12-15T16:24:38.395Z"}
              {:id 2
               :author 4
               :body "Pulsa en el botón que dice \"Apoyar\""
               :date "2015-12-15T17:24:38.395Z"}
              {:id 3
               :author 1
               :body "Ya hay grupos que han acordado algo"
               :date "2015-12-15T17:24:38.395Z"}]
   :participants [{:id 1
                   :name "Miguel"}
                  {:id 2
                   :name "Andy"}
                  {:id 3
                   :name "Mario"}
                  {:id 4
                   :name "Toño"}
                  {:id 5
                   :name "Anler"}
                  {:id 6
                   :name "Miguel Álvarez"}]
   :session {}})

(defonce state (atom (get-initial-state)))
