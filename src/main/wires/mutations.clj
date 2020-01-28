(ns wires.mutations
  (:require [com.wsscode.pathom.connect :refer [defmutation] :as pc]
            [datomic.api :as datomic]
            [taoensso.timbre :as log]))

(defmutation add-connector [{:keys [datomic]} {:connector/keys [kind label pins]}]
  {::pc/sym `add-connector}
  (log/info "Adding new connector" label "of type" kind "with" pins "pins")
  (datomic/transact (:conn datomic)
                    [{:connector/id    (datomic/squuid)
                      :connector/kind  kind
                      :connector/label label
                      :connector/pins  pins}]))

(def mutations [add-connector])
