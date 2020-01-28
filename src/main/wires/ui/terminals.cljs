(ns wires.ui.terminals
  (:require [wires.ui.bootstrap :refer [ui-button ui-badge]]
            [com.fulcrologic.fulcro.components :as comp :refer [defsc]]))

(defsc TerminalBadge [_this {:terminal/keys [connector pin]}]
  {:query         [:terminal/id {:terminal/connector [:connector/kind]} :terminal/pin]
   :ident         :terminal/id}
  (ui-button {:variant "dark" :size "sm"}
             (str (:connector/kind connector) " ")
             (ui-badge {:variant "light"}
                       pin)))

(def ui-terminal-badge (comp/factory TerminalBadge {:keyfn :terminal/id}))