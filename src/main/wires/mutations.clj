(ns wires.mutations
  (:require [com.wsscode.pathom.connect :refer [defmutation] :as pc]
            [datomic.api :as datomic]
            [taoensso.timbre :as log]))

(defmutation add-connector [{:keys [datomic]} {:connector/keys [kind label pins]}]
  {::pc/sym `add-connector}
  (log/info "Adding new connector" label "of type" kind "with" pins "pins")
  (let [{:keys [db-after tempids]} @(datomic/transact (:conn datomic)
                                                      [{:db/id           "new-connector"
                                                        :connector/id    (datomic/squuid)
                                                        :connector/kind  kind
                                                        :connector/label label
                                                        :connector/pins  pins}])
        added                      (datomic/pull db-after
                                                 '[:connector/id :connector/kind :connector/label :connector/pins]
                                                 (get tempids "new-connector"))]
    (log/info "Added foo" added)
    #_(datomic/pull db-after
                    '[:connector/id :connector/kind :connector/label :connector/pins]
                    (get tempids "new-connector"))))

(defn mutations []
  [add-connector])
