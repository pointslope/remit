(set-env!
 :source-paths    #{"src/cljs"}
 :resource-paths  #{"resources"}
 :dependencies '[[adzerk/boot-cljs      "0.0-2814-3"     :scope "test"]
                 [adzerk/boot-cljs-repl "0.1.9"          :scope "test"]
                 [adzerk/boot-reload    "0.2.6"          :scope "test"]
                 [pandeiro/boot-http    "0.6.3-SNAPSHOT" :scope "test"]
                 [adzerk/bootlaces      "0.1.11"         :scope "test"]
                 [reagent "0.5.0"]])

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
      :description "A tiny event-handling pattern built on core.async"
      :url "https://bitbucket.org/pointslope/remit"
      :scm {:url "https://bitbucket.org/pointslope/remit"}
      :license {"Eclipse Public License" "http://www.eclipse.org/legal/epl-v10.html"}})

(deftask build []
  (comp (speak :theme "woodblock")
        (cljs)))

(deftask run []
  (comp (serve)
        (watch)
        (cljs-repl)
        (reload)
        (build)))

(deftask production []
  (task-options! cljs {:optimizations :advanced
                       ;; pseudo-names true is currently required
                       ;; https://github.com/martinklepsch/pseudo-names-error
                       ;; hopefully fixed soon
                       :pseudo-names true})
  identity)

(deftask development []
  (task-options! cljs {:optimizations :none
                       :unified-mode true
                       :source-map true}
                 reload {:on-jsload 'remit.app/init})
  identity)

(deftask dev
  "Simple alias to run application in development mode"
  []
  (comp (development)
        (run)))
