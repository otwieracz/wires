(ns wires.ui.semantic
  (:require [com.fulcrologic.fulcro.algorithms.react-interop :refer [react-factory]]
            ["semantic-ui-react" :refer [Button
                                         Menu Menu.Item
                                         Table Table.Body Table.Cell Table.Header Table.HeaderCell Table.Row
                                         Modal Modal.Header Modal.Content Modal.Actions
                                         Form Form.Field 
                                         Input]]))

(def ui-button (react-factory Button))

(def ui-menu (react-factory Menu))
(def ui-menu-item (react-factory Menu.Item))

(def ui-table (react-factory Table))
(def ui-tbody (react-factory Table.Body))
(def ui-tc (react-factory Table.Cell))
(def ui-thead (react-factory Table.Header))
(def ui-thc (react-factory Table.HeaderCell))
(def ui-tr (react-factory Table.Row))

(def ui-modal (react-factory Modal))
(def ui-modal-header (react-factory Modal.Header))
(def ui-modal-content (react-factory Modal.Content))
(def ui-modal-actions (react-factory Modal.Actions))

(def ui-form (react-factory Form))
(def ui-form-field (react-factory Form.Field))

(def ui-input (react-factory Input))