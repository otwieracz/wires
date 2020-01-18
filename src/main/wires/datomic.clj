(ns wires.datomic
  (:require [datomic.api :as d]
            ))

(def wire-schema
  [{:db/ident       :wire/id
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc         "Wire ID"}
   {:db/ident       :wire/label
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "Wire Label"}
   {:db/ident       :wire/color
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "Wire Color"}
   {:db/ident       :wire/terminals
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/many
    :db/doc         "Wire Terminals"}])


