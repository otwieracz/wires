(ns wires.datomic
  (:require [datomic.api :as datomic]))

(def terminal-schema
  [{:db/ident       :terminal/id
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity
    :db/doc         "Terminal ID"}
   {:db/ident       :terminal/connector
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "Terminal connector"}
   {:db/ident       :terminal/pin
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc         "Terminal pin"}])

(def wire-schema
  [{:db/ident       :wire/id
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity
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

(def terminals-data
  [{:terminal/id        0
    :terminal/connector "JST.0"
    :terminal/pin       1}
   {:terminal/id        1
    :terminal/connector "JST.0"
    :terminal/pin       2}
   {:terminal/id        2
    :terminal/connector "SCREWED.0"
    :terminal/pin       0}
   {:terminal/id        3
    :terminal/connector "SCREWED.0"
    :terminal/pin       1}])

(def wires-data
  [{:wire/id        0
    :wire/label     "Vcc"
    :wire/color     "red"
    :wire/terminals [[:terminal/id 0]
                     [:terminal/id 2]]}
   {:wire/id        1
    :wire/label     "GND"
    :wire/color     "brown"
    :wire/terminals [[:terminal/id 1]
                     [:terminal/id 3]]}])

(defn load-schema
  [conn]
  (doall
    (map (fn [schema] @(datomic/transact conn schema))
         [terminal-schema wire-schema])))

(defn load-initial-data
  [conn]
  (doall
    (map (fn [data] @(datomic/transact conn data))
         [terminals-data wires-data])))

