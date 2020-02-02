(ns wires.client
  (:require [wires.application :as application]
            [wires.ui.root :as root]
            [wires.ui.wires :as ui.wires]
            [wires.ui.connectors :as ui.connectors]
            [com.fulcrologic.fulcro.application :as app]
            [com.fulcrologic.fulcro.data-fetch :as df]))

(defn ^:export init
  []
  (app/mount! application/APP root/Root "app")
  (js/console.log "loading")
  (df/load! application/APP :wires ui.wires/Wire)
  (df/load! application/APP :connectors ui.connectors/Connector #_{:target [:connectors-tab/id :singleton :connectors]})
  (js/console.log "hi there"))

