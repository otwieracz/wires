(ns wires.resolvers
  (:require [com.wsscode.pathom.connect :as pc :refer [defresolver]]
            [datomic.api :as datomic]))

(defresolver connector-resolver [{:keys [datomic]} {:connector/keys [id]}]
  {::pc/input  #{:connector/id}
   ::pc/output [:connector/kind :connector/label :connector/pins]}
  (datomic/pull (datomic/db (:conn datomic))
                [:connector/id :connector/label :connector/kind :connector/pins]
                [:connector/id id]))

(defresolver connectors-resolver [{:keys [datomic]} _input]
  {::pc/output [{:connectors [:connector/id]}]}
  (let [connectors (datomic/q '[:find (pull ?w [:connector/id])
                                :where [?w :connector/id]]
                              (datomic/db (:conn datomic)))]
    {:connectors (map first connectors)}))

(defresolver terminal-resolver [{:keys [datomic]} {:terminal/keys [id]}]
  {::pc/input  #{:terminal/id}
   ::pc/output [{:terminal/connector [:connector/id]} :terminal/pin]}
  (datomic/pull (datomic/db (:conn datomic))
                [:terminal/id {:terminal/connector [:connector/id]} :terminal/pin]
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

(def resolvers [connector-resolver connectors-resolver terminal-resolver wire-resolver wires-resolver])