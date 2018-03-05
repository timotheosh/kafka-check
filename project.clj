(defproject kafka-check "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ymilky/franzy-admin "0.0.1"]
                 [clojurewerkz/propertied "1.3.0"]
                 [mount "0.1.12"]
                 [org.clojure/data.json "0.2.6"]
                 [me.raynes/conch "0.8.0"]
                 [com.stuartsierra/component "0.3.2"]]
  :main ^:skip-aot kafka-check.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
