(ns wires.ui.connectors
  (:require [wires.ui.bootstrap :as bp]
            [wires.mutations :as mutations]
            [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.dom :as dom :refer [div thead tr td th tbody]]))

(defsc AddConnectorModal [this {:add-connector-modal/keys [show]} {:keys [onClose]}]
  {:query [:add-connector-modal/id :add-connector-modal/show]
   :ident (fn [] [:add-connector-modal/id :singleton])
   :initial-state {:add-connector-modal/show false}}
  (js/console.log "Show is" show)
  (bp/ui-modal {:show show}
               (bp/ui-modal-header {}
                                   (bp/ui-modal-title {} "Add new connector"))
               (bp/ui-modal-body {}
                                 "Foo bar car")
               (bp/ui-modal-footer {}
                                   (bp/ui-button {:variant "secondary" :onClick onClose}
                                                 "nope :(")
                                   (bp/ui-button {:onClick #(comp/transact! this [(mutations/add-connector {:connector/kind  "JST"
                                                                                                            :connector/label "TEST"
                                                                                                            :connector/pins  4})])}
                                                 "yeah pls"))))

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
   :initial-state {:connectors-tab/add-connector-modal {:add-connector-modal/id :singleton}}}
  (js/console.log "connectors are" connectors) ;; => null
  (js/console.log "add connector modal is" (str add-connector-modal)) ;; => null
  (div
   (when connectors
     (div
      (ui-connector-list {:connectors-list/connectors connectors})))
   (ui-add-connector-modal (comp/computed add-connector-modal
                                          {

                                           :onClose #(comp/transact! this [(mutations/change-add-connection-modal-state {:modal-id  :singleton
                                                                                                                         :new-state false})])}))
   (bp/ui-button {:onClick (fn [] (comp/transact! this [(mutations/change-add-connection-modal-state {:modal-id  :singleton
                                                                                                      :new-state true})]))}
                 "Add new connector")))

(def ui-connectors-tab (comp/factory ConnectorsTab {:keyfn :connectors-tab/id}))