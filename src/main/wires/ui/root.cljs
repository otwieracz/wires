(ns wires.ui.root
  (:require [wires.mutations :as mutations]
            [wires.ui.mutations :as ui-mutations]
            [wires.ui.wires :as ui.wires]
            [wires.ui.connectors :as ui.connectors]
            [wires.ui.semantic :refer [ui-menu ui-menu-item]]
            [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.dom :as dom :refer [div]]))

(defsc Root [this {:keys [active-tab wires connectors-tab]}]
  {:query         [:active-tab
                   #_{:connectors (comp/get-query ui.connectors/Connector)}
                   {:wires (comp/get-query ui.wires/Wire)}
                   {:connectors-tab (comp/get-query ui.connectors/ConnectorsTab)}]
   :initial-state {:active-tab     "wires"
                   :connectors-tab {:connectors-tab/id :singleton}}}
  (let [change-tab (fn [_ props]
                     (comp/transact! this [(ui-mutations/change-active-tab {:active-tab (.-name props)})]))]
    (div
     (ui-menu {}
              (ui-menu-item {:header true} "Wires")
              (ui-menu-item {:name "wires" :active (= active-tab "wires") :onClick change-tab} "Wires")
              (ui-menu-item {:name "connectors" :active (= active-tab "connectors") :onClick change-tab} "Connectors"))
     (case active-tab
       "wires"
       #_(when wires
           (div
            (ui.wires/ui-wire-list {:wires-list/wires wires})
            (ui-button {:onClick (fn [] (comp/transact! this [(mutations/add-wire {:wire-list/id :my-wires
                                                                                   :wire/id      2})]))} "foo")))
       "connectors"
       (ui.connectors/ui-connectors-tab connectors-tab)))))


