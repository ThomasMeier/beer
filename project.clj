(defproject beer "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170"]
                 [reagent "0.5.1"]
                 [re-frame "0.7.0"]
                 [re-com "0.8.0"]
                 [compojure "1.5.0"]
                 [clj-gpio "0.2.0"]
                 [ring "1.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [org.immutant/scheduling "2.1.3"]
                 [cheshire "5.5.0"]
                 [cljs-ajax "0.5.4"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj"]

  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-figwheel "0.5.0-6"]
            [lein-ring "0.9.7"]]

  :ring {:handler beer.handler/app}

  ;;:clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :figwheel {:css-dirs ["resources/public/css"]
             :ring-handler beer.handler/app}

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/cljs"]
                        :figwheel {:on-jsload "beer.core/mount-root"
                                   :css-dirs ["resources/public/vendor/css"]
                                   :ring-handler beer.handler/app}
                        :compiler {:main beer.core
                                   :output-to "resources/public/js/compiled/app.js"
                                   :output-dir "resources/public/js/compiled/out"
                                   :asset-path "js/compiled/out"
                                   :source-map-timestamp true}}

                       {:id "min"
                        :source-paths ["src/cljs"]
                        :compiler {:main beer.core
                                   :output-to "resources/public/js/compiled/app.js"
                                   :optimizations :advanced
                                   :closure-defines {goog.DEBUG false}
                                   :pretty-print false}}]})
