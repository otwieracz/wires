(ns wires.ui.connectors
  (:require [wires.ui.bootstrap :as bp]
            [wires.mutations :as mutations]
            [wires.ui.mutations :as ui-mutations]
            [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.dom :as dom :refer [div thead tr td th tbody]]))

(defsc AddConnectorModal [this {:add-connector-modal/keys [show id label kind pins]} {:keys [onClose]}]
  {:query         [:add-connector-modal/id
                   :add-connector-modal/show
                   :add-connector-modal/label
                   :add-connector-modal/kind
                   :add-connector-modal/pins]
   :ident         (fn [] [:add-connector-modal/id :singleton])
   :initial-state {:add-connector-modal/show :param/show
                   :add-connector-modal/id   :param/id}}
  (let [change-fn (fn [event]
                    (comp/transact! this [(ui-mutations/update-add-connector-modal-form
                                           {:add-connector-modal/id    id
                                            :add-connector-modal/field (.-id (.-target event))
                                            :add-connector-modal/value (.-value (.-target event))})]))]
    (bp/ui-modal {:show   show
                  :onHide onClose}
                 (bp/ui-modal-header {} (bp/ui-modal-title {} "Add new connector"))
                 (bp/ui-modal-body {}
                                   (bp/ui-form {}
                                               (bp/ui-form-group {:controlId "label"}
                                                                 (bp/ui-form-label {} "Connector label")
                                                                 (bp/ui-form-control {:type         "text"
                                                                                      :placeholder  "Label"
                                                                                      :defaultValue label
                                                                                      :onChange     change-fn}))
                                               (bp/ui-form-group {:controlId "kind"}
                                                                 (bp/ui-form-label {} "Kind of connector (DB9, JST, etc)")
                                                                 (bp/ui-form-control {:type         "text"
                                                                                      :placeholder  "Kind"
                                                                                      :defaultValue kind
                                                                                      :onChange     change-fn}))
                                               (bp/ui-form-group {:controlId "pins"}
                                                                 (bp/ui-form-label {} "Number of pins in connector")
                                                                 (bp/ui-form-control {:type         "number"
                                                                                      :min          1
                                                                                      :placeholder  "Number"
                                                                                      :defaultValue pins
                                                                                      :onChange     change-fn}))))
                 (bp/ui-modal-footer {}
                                     (bp/ui-button {:variant "secondary" :onClick onClose} "Cancel")
                                     (bp/ui-button {:onClick #(comp/transact! this [(mutations/add-connector {:connector/modal-id id
                                                                                                              :connector/kind     kind
                                                                                                              :connector/label    label
                                                                                                              :connector/pins     pins})])}
                                                   "Add")))))

(def ui-add-connector-modal (comp/factory AddConnectorModal {:keyfn :add-connector-modal/id}))

(defsc Connector [_this {:connector/keys [label kind pins]}]
  {:query [:connector/id :connector/label :connector/kind :connector/pins]
   :ident :connector/id}
  (tr
   (td label)
   (td kind)
   (td pins)))

(def ui-connector (comp/factory Connector {:keyfn :connector/id}))

(defsc ConnectorList [_this {:connectors-list/keys [connectors]}]
  {:query [{:connectors-list/connectors (comp/get-query Connector)}]
   :ident (fn [] [:connectors-list/id :singleton])}
  (bp/ui-table {:striped true
                :hover   true}
               (thead
                (tr
                 (th "Label")
                 (th "Kind")
                 (th "Pins")))
               (tbody
                (map ui-connector connectors))))

(def ui-connector-list (comp/factory ConnectorList {:keyfn :connectors-list/id}))

(defsc ConnectorsTab [this {:keys                [connectors]
                            :connectors-tab/keys [add-connector-modal]}]
  {:query         [{[:connectors '_] (comp/get-query Connector)}
                   {:connectors-tab/add-connector-modal (comp/get-query AddConnectorModal)}]
   :ident         (fn [] [:connectors-tab/id :singleton])
   :initial-state {:connectors-tab/add-connector-modal {:id   :singleton
                                                        :show false}}}
  (let [modal-state-fn (fn [new-state]
                         #(comp/transact! this
                                          [(ui-mutations/change-add-connector-modal-state {:modal-id  :singleton
                                                                                           :new-state new-state})]))]
    (div
     (when connectors
       (div
        (ui-connector-list {:connectors-list/connectors connectors})))
     (ui-add-connector-modal (comp/computed add-connector-modal {:onClose (modal-state-fn false)}))
     (bp/ui-button {:onClick (modal-state-fn true)} "Add new connector"))))

(def ui-connectors-tab (comp/factory ConnectorsTab {:keyfn :connectors-tab/id}))