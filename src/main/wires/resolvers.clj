(ns wires.resolvers
  (:require [com.wsscode.pathom.core :as p]
            [com.wsscode.pathom.connect :as pc :refer [defresolver]]
            [datomic.api :as datomic]
            [taoensso.timbre :as log]
            [clojure.tools.trace :as trace]))

(defresolver terminal-resolver [{:keys [datomic]} {:terminal/keys [id]}]
  {::pc/input  #{:terminal/id}
   ::pc/output [:terminal/connector :terminal/pin]}
  (datomic/pull (datomic/db (:conn datomic))
                [:terminal/id :terminal/connector :terminal/pin]
                [:terminal/id id]))

(defresolver wire-resolver [{:keys [datomic]} {:wire/keys [id]}]
  {::pc/input  #{:wire/id}
   ::pc/output [:wire/label :wire/color {:wire/terminals [:terminal/id]}]}
  (datomic/pull (datomic/db (:conn datomic))
                [:wire/id :wire/label :wire/color {:wire/terminals [:terminal/id]}]
                [:wire/id id]))

(defresolver wires-resolver [{:keys [datomic]} _input]
  {::pc/output [{:wires [:wire/id]}]}
  (let [wires (datomic/q '[:find (pull ?w [:wire/id])
                           :where [?w :wire/id]]
                         (datomic/db (:conn datomic)))]
    {:wires (map first wires)}))

(def resolvers [terminal-resolver wire-resolver wires-resolver])