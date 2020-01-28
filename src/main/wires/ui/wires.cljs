(ns wires.ui.wires
  (:require #_[wires.ui.mutations :as mutations]
            [wires.ui.terminals :as ui.terminals]
            [wires.ui.bootstrap :refer [ui-button-toolbar ui-table]]
            [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.dom :as dom :refer [thead tr td th tbody]]))

(defsc Wire [_this {:wire/keys [label color terminals]}]
  {:query [:wire/id :wire/label :wire/color {:wire/terminals (comp/get-query ui.terminals/TerminalBadge)}]
   :ident :wire/id}
  (tr
   (td label)
   (td {:style {:backgroundColor color}} color)
   (td (ui-button-toolbar {} (map ui.terminals/ui-terminal-badge terminals)))))

(def ui-wire (comp/factory Wire {:keyfn :wire/id}))

(defsc WireList [_this {:wires-list/keys [wires]}]
  {:query [{:wires-list/wires (comp/get-query Wire)}]
   :ident (fn [] [:wire-list/id :singleton])}
  (ui-table {:striped true
             :hover   true}
            (thead
             (tr
              (th "Label")
              (th "Color")
              (th "Terminals")))
            (tbody
             (map ui-wire wires))))

(def ui-wire-list (comp/factory WireList {:keyfn :wire-list/id}))
