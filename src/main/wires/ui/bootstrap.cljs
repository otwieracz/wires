(ns wires.ui.bootstrap
  (:require [com.fulcrologic.fulcro.algorithms.react-interop :as interop]
            ["react-bootstrap" :refer [Button
                                       Table 
                                       Badge 
                                       DropdownButton Dropdown.Item 
                                       ButtonToolbar 
                                       Navbar Navbar.Brand Navbar.Collapse Nav Nav.Item Nav.Link
                                       Modal Modal.Dialog Modal.Header Modal.Title Modal.Body Modal.Footer]]))

(def ui-button (interop/react-factory Button))
(def ui-button-toolbar (interop/react-factory ButtonToolbar))
(def ui-table (interop/react-factory Table))
(def ui-badge (interop/react-factory Badge))

(def ui-dropdown-button (interop/react-factory DropdownButton))
(def ui-dropdown-item (interop/react-factory Dropdown.Item))

(def ui-navbar (interop/react-factory Navbar))
(def ui-navbar-brand (interop/react-factory Navbar.Brand))
(def ui-navbar-collapse (interop/react-factory Navbar.Collapse))
(def ui-nav (interop/react-factory Nav))
(def ui-nav-item (interop/react-factory Nav.Item))
(def ui-nav-link (interop/react-factory Nav.Link))

(def ui-modal (interop/react-factory Modal))
(def ui-modal-dialog (interop/react-factory Modal.Dialog))
(def ui-modal-header (interop/react-factory Modal.Header))
(def ui-modal-title (interop/react-factory Modal.Title))
(def ui-modal-body (interop/react-factory Modal.Body))
(def ui-modal-footer (interop/react-factory Modal.Footer))
