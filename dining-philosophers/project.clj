(defproject dining-philosophers "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [ch.qos.logback/logback-classic "1.0.13"]]
  :main ^:skip-aot dining-philosophers.core
  :java-source-paths ["java-src"]
  :target-path "target"
  :profiles {:uberjar {:aot :all}})
