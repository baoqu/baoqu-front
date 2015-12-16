(require '[cljs.build.api :as b])

(b/watch (b/inputs "test" "src")
  {:main 'baoqu.core
   :output-to "resources/public/js/main.js"
   :output-dir "resources/public/js"
   :asset-path "/js"
   :optimizations :none
   :pretty-print true
   :language-in  :ecmascript5
   :language-out :ecmascript5
   :verbose true})
