(defproject pointslope/remit "0.3.0"
  :description "A tiny event pub/sub library built on core.async"
  :url "https://github.com/pointslope/remit"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.36"]
                 [org.clojure/core.async "0.2.374" ]]
  :repositories    [["clojars" {:url "https://clojars.org/repo/"
                                :creds :gpg}]]
  :deploy-repositories [["releases" :clojars]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
