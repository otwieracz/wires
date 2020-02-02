(ns wires.mutations
  (:require [com.fulcrologic.fulcro.mutations :as m :refer [defmutation returning]]
            [com.fulcrologic.fulcro.data-fetch :as df]
            [com.fulcrologic.fulcro.components :as comp]))

(defn- create-wire
  [state-map id label color]
  (assoc-in state-map
            [:wire/id id]
            {:wire/id        id
             :wire/label     label
             :wire/color     color
             :wire/terminals [[:terminal/id 0]
                              [:terminal/id 3]]}))

(defmutation add-wire
  [{wire-list-id :wire-list/id
    wire-id :wire/id}]
  #_(action [{:keys [state]}]
            (swap! state
                   (fn [s]
                     (-> s
                         (create-wire wire-id "Vcc" "yellow")
                         (data-targeting/integrate-ident* [:wire/id wire-id] :append [:wire-list/id wire-list-id :wire-list/wires])))))
  (remote [_env] true)
  (ok-action [{:keys [app]}]
             (df/load! app [:wire/id wire-id] (comp/registry-key->class :wires.ui/Wire))
             (df/load! app [:wire-list/id wire-list-id] (comp/registry-key->class :wires.ui/WireList))))

(defmutation change-active-tab
  [{active-tab :active-tab}]
  (action [{:keys [state]}]
          (swap! state
                 #(assoc % :active-tab active-tab))))

(defmutation change-add-connection-modal-state
  [{:keys [modal-id new-state]}]
  (action [{:keys [state]}]
          (swap! state
                 #(assoc-in % [:add-connector-modal/id modal-id :add-connector-modal/show] new-state))))

(defmutation add-connector
  [{:connector/keys [label kind pins]}]
  (remote [env] 
          (returning env (comp/registry-key->class :wires.ui.connectors/Connector))))