(ns wires.client
  (:require [wires.application :as application]
            [wires.ui :as ui]
            [com.fulcrologic.fulcro.application :as app]
            [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.dom :as dom]
            [com.fulcrologic.fulcro.data-fetch :as df]))

(defn ^:export init
  []
  (app/mount! application/APP ui/Root "app")
  (js/console.log "loading")
  (df/load! application/APP :wires ui/Wire)
  (js/console.log "hi there"))

