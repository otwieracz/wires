(ns wires.ui.connectors
  (:require [wires.ui.semantic :refer [ui-table ui-tbody ui-tc ui-thead ui-thc ui-tr
                                       ui-button
                                       ui-modal ui-modal-header ui-modal-content ui-modal-actions
                                       ui-form ui-form-field
                                       ui-input]]
            [wires.mutations :as mutations]
            [wires.ui.mutations :as ui-mutations]
            [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.dom :as dom :refer [div]]))

(defsc AddConnectorModal [this {:add-connector-modal/keys [open id label kind pins]} {:keys [onClose]}]
  {:query         [:add-connector-modal/id
                   :add-connector-modal/open
                   :add-connector-modal/label
                   :add-connector-modal/kind
                   :add-connector-modal/pins]
   :ident         (fn [] [:add-connector-modal/id :singleton])
   :initial-state {:add-connector-modal/open :param/open
                   :add-connector-modal/id   :param/id}}
  (let [change-fn (fn [key]
                    (fn [_ data]
                      (comp/transact! this [(ui-mutations/update-add-connector-modal-form
                                             {:add-connector-modal/id    id
                                              :add-connector-modal/field key
                                              :add-connector-modal/value (.-value data)})])))]
    (ui-modal {:open    open
               :onClose onClose}
              (ui-modal-header {} "Add new connector")
              (ui-modal-content {}
                                (ui-form {}
                                         (ui-form-field {} (ui-input {:label "Label" :onChange (change-fn :label)}))
                                         (ui-form-field {} (ui-input {:label "Kind" :onChange (change-fn :kind)}))
                                         (ui-form-field {} (ui-input {:label "Pins" :type "number" :onChange (change-fn :pins)}))))
              (ui-modal-actions {}
                                (ui-button {:className "secondary"
                                            :onClick   onClose}
                                           "Cancel")
                                (ui-button {:className "primary"
                                            :onClick   #(comp/transact! this [(mutations/add-connector {:connector/modal-id id
                                                                                                        :connector/kind     kind
                                                                                                        :connector/label    label
                                                                                                        :connector/pins     pins})])}
                                           "Add")))))

(def ui-add-connector-modal (comp/factory AddConnectorModal {:keyfn :add-connector-modal/id}))

(defsc Connector [_this {:connector/keys [label kind pins]}]
  {:query [:connector/id :connector/label :connector/kind :connector/pins]
   :ident :connector/id}
  (ui-tr {}
         (ui-tc {} label)
         (ui-tc {} kind)
         (ui-tc {} pins)))

(def ui-connector (comp/factory Connector {:keyfn :connector/id}))

(defsc ConnectorList [_this {:connectors-list/keys [connectors]}]
  {:query [{:connectors-list/connectors (comp/get-query Connector)}]
   :ident (fn [] [:connectors-list/id :singleton])}
  (ui-table {}
            (ui-thead {}
                      (ui-tr {}
                             (ui-thc {} "Label")
                             (ui-thc {} "Kind")
                             (ui-thc {} "Pins")))
            (ui-tbody {}
                      (map ui-connector connectors))))

(def ui-connector-list (comp/factory ConnectorList {:keyfn :connectors-list/id}))

(defsc ConnectorsTab [this {:keys                [connectors]
                            :connectors-tab/keys [add-connector-modal]}]
  {:query         [{[:connectors '_] (comp/get-query Connector)}
                   {:connectors-tab/add-connector-modal (comp/get-query AddConnectorModal)}]
   :ident         (fn [] [:connectors-tab/id :singleton])
   :initial-state {:connectors-tab/add-connector-modal {:id   :singleton
                                                        :open false}}}
  (let [modal-state-fn (fn [new-state]
                         #(comp/transact! this
                                          [(ui-mutations/change-add-connector-modal-state {:modal-id  :singleton
                                                                                           :new-state new-state})]))]
    (div
     (when connectors
       (div
        (ui-connector-list {:connectors-list/connectors connectors})))
     (ui-add-connector-modal (comp/computed add-connector-modal {:onClose (modal-state-fn false)}))
     (ui-button {:onClick (modal-state-fn true)} "Add new connector"))))

(def ui-connectors-tab (comp/factory ConnectorsTab {:keyfn :connectors-tab/id}))