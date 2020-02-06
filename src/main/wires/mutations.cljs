(ns wires.mutations
  "Remote mutations"
  (:require [wires.ui.mutations :as ui-mutations]
            [com.fulcrologic.fulcro.mutations :as m :refer [defmutation returning]]
            [com.fulcrologic.fulcro.algorithms.data-targeting :as targeting]
            [com.fulcrologic.fulcro.data-fetch :as df]
            [com.fulcrologic.fulcro.components :as comp]))

(defmutation add-wire
  [{wire-list-id :wire-list/id
    wire-id :wire/id}]
  (remote [_env] true)
  (ok-action [{:keys [app]}]
             (df/load! app [:wire/id wire-id] (comp/registry-key->class :wires.ui/Wire))
             (df/load! app [:wire-list/id wire-list-id] (comp/registry-key->class :wires.ui/WireList))))

(defmutation add-connector
  [{:connector/keys [modal-id _label _kind _pins]}]
  (remote [env]
          (-> env
              (returning (comp/registry-key->class :wires.ui.connectors/Connector))
              (m/with-target
                (targeting/append-to [:connectors]))))
  (ok-action [{:keys [app]}]
             (comp/transact! app [(wires.ui.mutations/change-add-connector-modal-state {:modal-id  modal-id
                                                                                        :new-state false})])))