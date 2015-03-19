(set-env!
 :source-paths    #{"src"}
 :resource-paths  #{"resources"}
 :dependencies '[[adzerk/boot-cljs      "0.0-2814-3"     :scope "test"]
                 [adzerk/boot-cljs-repl "0.1.9"          :scope "test"]
                 [adzerk/boot-reload    "0.2.6"          :scope "test"]
                 [pandeiro/boot-http    "0.6.3-SNAPSHOT" :scope "test"]
                 [adzerk/bootlaces      "0.1.11"         :scope "test"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [reagent "0.5.0" :exclusions [org.clojure/clojure org.clojure/clojurescript]]])

(require
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 '[pandeiro.boot-http    :refer [serve]]
 '[adzerk.bootlaces      :refer :all])

(def +version+ "0.1.0-SNAPSHOT")
(bootlaces! +version+)

(task-options!
 pom {:project  'pointslope/remit
      :version +version+
      :description "A tiny (experimental) event-handling library built on core.async"
      :url "https://github.com/pointslope/remit"
      :scm {:url "https://github.com/pointslope/remit"}
      :license {"Eclipse Public License" "http://www.eclipse.org/legal/epl-v10.html"}})
