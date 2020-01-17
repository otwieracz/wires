(ns wires.mutations
  (:require [wires.resolvers :refer [wire-list-table wire-table]]
            [com.wsscode.pathom.connect :refer [defmutation] :as pc]
            [taoensso.timbre :as log]))

(defmutation add-wire [_env {wire-list-id :wire-list/id
                             wire-id      :wire/id}]
  {::pc/sym `add-wire}
  (log/info "Adding new wire" wire-id "to" wire-list-id)
  (swap! wire-table merge {wire-id {:wire/id       wire-id
                                    :wire/label     "Vcc"
                                    :wire/color     "yellow"
                                    :wire/terminals [{:terminal/id 0}
                                                     {:terminal/id 3}]}})
  (swap! wire-list-table update wire-list-id (fn [w] (update w :wire-list/wires #(conj % {:wire/id wire-id})))))

(def mutations [add-wire])
