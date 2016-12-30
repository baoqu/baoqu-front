(defproject baoqu "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.293"]
                 [rum "0.10.7"]
                 [bidi "2.0.16"]
                 [funcool/httpurr "0.6.2"]
                 [com.cognitect/transit-cljs "0.8.239"]]
  :plugins [[lein-figwheel "0.5.8"]
            [lein-cljsbuild "1.1.5"]
            [lein-ancient "0.6.10"]]
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
                                   :verbose true}}
                       {:id "dist"
                        :source-paths ["src/"]
                        :compiler {:main baoqu.core
                                   :asset-path "js"
                                   :language-in :ecmascript5
                                   :language-out :ecmascript5
                                   :output-to "dist/public/js/main.js"
                                   :output-dir "dist/public/js"
                                   :closure-defines {"baoqu.config.url" "http://app.baoqu.org"}
                                   :verbose true}}]}
  :figwheel {:css-dirs ["resources/public/css"]}
  :aliases {"dist" ["cljsbuild" "once"]}
  :clean-targets ^{:protect false} ["target" "resources/public" "dist"])
