(ns user
  (:require [clojure.tools.namespace.repl :refer [set-refresh-dirs refresh]]
            [system.repl :refer [system set-init! start stop reset]]
            [wires.systems :refer [dev-system]]
            [shadow.cljs.devtools.api :refer [repl]]
            [datomic.api :as datomic]))

;; Ensure we only refresh the source we care about. This is important
;; because `resources` is on our classpath and we don't want to
;; accidentally pull source from there when cljs builds cache files there.
(set-refresh-dirs "src/dev" "src/main")

(set-init! #'dev-system)