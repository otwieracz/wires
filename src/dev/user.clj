(ns user
  (:require [clojure.tools.namespace.repl :refer [set-refresh-dirs refresh]]
            [clojure.tools.trace :as trace]
            [system.repl :refer [system set-init! start stop reset]]
            [shadow.cljs.devtools.api :refer [repl]]
            [datomic.api :as datomic]
            [wires.systems :refer [dev-system]]
            [wires.datomic :as wd]))

;; Ensure we only refresh the source we care about. This is important
;; because `resources` is on our classpath and we don't want to
;; accidentally pull source from there when cljs builds cache files there.
(set-refresh-dirs "src/dev" "src/main")

(set-init! #'dev-system)

(defn conn [] (:conn (:datomic system)))
(defn db [] (datomic/db (conn)))

(defn reset-and-load
  []
  (reset)
  (wd/load-schema (conn))
  (wd/load-initial-data (conn)))
