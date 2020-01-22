(ns wires.ui
  (:require [wires.mutations :as mutations]
            [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.dom :as dom :refer [thead tr td th tbody div]]
            [com.fulcrologic.fulcro.algorithms.react-interop :as interop]
            ["react-bootstrap" :refer [Button Table Badge DropdownButton Dropdown.Item ButtonToolbar]]))

(def ui-button (interop/react-factory Button))
(def ui-button-toolbar (interop/react-factory ButtonToolbar))
(def ui-table (interop/react-factory Table))
(def ui-badge (interop/react-factory Badge))
(def ui-dropdown-button (interop/react-factory DropdownButton))
(def ui-dropdown-item (interop/react-factory Dropdown.Item))

(defsc Terminal [_this {:terminal/keys [connector pin]}]
  {:query         [:terminal/id :terminal/connector :terminal/pin]
   :ident         :terminal/id}
  (ui-button {:variant "dark" :size "sm"}
             (str connector " ")
             (ui-badge {:variant "light"}
                       pin)))

(def ui-terminal (comp/factory Terminal {:keyfn :terminal/id}))

(defsc Wire [_this {:wire/keys [label color terminals saved?]}]
  {:query         [:wire/id :wire/label :wire/color {:wire/terminals (comp/get-query Terminal)} :wire/saved?]
   :ident         :wire/id}
  (tr
    (td label)
    (td {:style {:backgroundColor color}} color)
    (td (ui-button-toolbar {} (map ui-terminal terminals)))
    (td (if saved?
          "saved"
          "in-progress"))))

(def ui-wire (comp/factory Wire {:keyfn :wire/id}))

(defsc WireList [_this {:wires-list/keys [wires]}]
  {:query [{:wires-list/wires (comp/get-query Wire)}]
   :ident (fn [] [:wire-list/id :singleton])
   }
  (println (str "Wires on WireList are: " wires))
  (ui-table {:striped true :hover true}
            (thead
              (tr
                (th "Label")
                (th "Color")
                (th "Terminals")
                (th "Saved?")
                ))
            (tbody
              (map ui-wire wires))))

(def ui-wire-list (comp/factory WireList #_{:keyfn :wire-list/id}))

(defsc Root [this {:keys [wires]}]
  {:query         [{:wires (comp/get-query Wire)}]
   :initial-state {}}
  (div
    (when wires
      (println (str "Wires on Wires are: " wires))
      (div
        (ui-wire-list {:wires-list/wires wires})
        (ui-button {:onClick (fn [] (comp/transact! this [(mutations/add-wire {:wire-list/id :my-wires :wire/id 2})]))} "foo")))))


