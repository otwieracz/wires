(ns wires.ui.root
  (:require [wires.ui.mutations :as mutations]
            [wires.ui.wires :as ui.wires]
            [wires.ui.connectors :as ui.connectors]
            [wires.ui.bootstrap :refer [ui-navbar ui-navbar-brand ui-nav ui-nav-item ui-nav-link ui-button]]
            [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.dom :as dom :refer [div]]))

(defsc Root [this {:keys [active-tab wires connectors-tab]}]
  {:query         [:active-tab {:wires (comp/get-query ui.wires/Wire)} {:connectors-tab (comp/get-query ui.connectors/ConnectorsTab)}]
   :initial-state {:active-tab     "wires"
                   :connectors-tab {:connectors-tab/id :singleton}}}
  (div
   (ui-navbar {:bg     "light"
               :expand "lg"}
              (ui-navbar-brand {} "Wires")
              (ui-nav {:activeKey active-tab
                       :onSelect  #(comp/transact! this [(mutations/change-active-tab {:active-tab %})])}
                      (ui-nav-item {} (ui-nav-link {:eventKey "wires"} "Wires"))
                      (ui-nav-item {} (ui-nav-link {:eventKey "connectors"} "Connectors"))))
   (js/console.log "connectors-tab" connectors-tab)
   (js/console.log "wires" wires)
   (case active-tab
     "wires"
     (when wires
       (div
        (ui.wires/ui-wire-list {:wires-list/wires wires})
        (ui-button {:onClick (fn [] (comp/transact! this [(mutations/add-wire {:wire-list/id :my-wires
                                                                               :wire/id      2})]))} "foo")))
     "connectors"
     (ui.connectors/ui-connectors-tab connectors-tab))))


