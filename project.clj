(defproject baoqu "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.51"]
                 [rum "0.10.4"]
                 [bidi "2.0.9"]
                 [funcool/httpurr "0.6.1"]
                 [com.cognitect/transit-cljs "0.8.232"]]
  :plugins [[lein-figwheel "0.5.4-7"]
            [lein-cljsbuild "1.1.3"]]
  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/"]
                        :figwheel true
                        :compiler {:main baoqu.core
                                   :asset-path "js"
                                   :pretty-print true
                                   :language-in :ecmascript5
                                   :language-out :ecmascript5
                                   :output-to "resources/public/js/main.js"
                                   :output-dir "resources/public/js"
                                   :verbose true}}]}
  :figwheel {:css-dirs ["resources/public/css"]}
  :aliases {"dist" ["cljsbuild" "once"]})
