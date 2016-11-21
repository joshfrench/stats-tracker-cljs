(defproject stats-tracker "0.1.0-SNAPSHOT"
  :plugins [[lein-cljsbuild "1.1.4"]]
  :description "Stats Tracker"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.51"]
                 [org.omcljs/om "1.0.0-alpha46"]
                 [sablono "0.7.4"]
                 [figwheel-sidecar "0.5.8" :scope "test"]
                 [lein-cljsbuild "1.1.4"]]
  :hooks [lein.cljsbuild]
  :cljsbuild {:builds [{:source-paths ["src/stats_tracker"]
                        :compiler {:main "stats-tracker.core"
                                   :verbose true
                                   :optimizations :advanced
                                   :asset-path "js/compiled"
                                   :output-to "resources/public/js/main.js"
                                   :source-map "resources/public/js/main.js.map"
                                   :output-dir "resources/public/js/compiled"}}]}
  :clean-targets ^{:protect false} ["resources/public/js"
                                    :target-path])
