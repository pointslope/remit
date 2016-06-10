(set-env!
 :source-paths    #{"src"}
 :resource-paths  #{"resources"}
 :dependencies '[[adzerk/boot-cljs      "1.7.228-1" :scope "test"]
                 [adzerk/boot-cljs-repl "0.3.0"     :scope "test"]
                 [adzerk/boot-reload    "0.4.8"     :scope "test"]
                 [pandeiro/boot-http    "0.7.3"     :scope "test"]
                 [adzerk/bootlaces      "0.1.13"    :scope "test"]
                 [org.clojure/core.async "0.2.374" ]])

(require
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 '[pandeiro.boot-http    :refer [serve]]
 '[adzerk.bootlaces      :refer :all])

(def +version+ "0.2.0-SNAPSHOT")
(bootlaces! +version+)

(task-options!
 pom {:project  'pointslope/remit
      :version +version+
      :description "A tiny (experimental) event-handling library built on core.async"
      :url "https://github.com/pointslope/remit"
      :scm {:url "https://github.com/pointslope/remit"}
      :license {"Eclipse Public License" "http://www.eclipse.org/legal/epl-v10.html"}})
