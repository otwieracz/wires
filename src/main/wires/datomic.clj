(ns wires.datomic
  (:require [datomic.api :as datomic]))

(def connector-schema
  [{:db/ident       :connector/id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity
    :db/doc         "Connector ID"}
   {:db/ident       :connector/kind
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "Connector kind"}
   {:db/ident       :connector/label
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "Connector label"}
   {:db/ident       :connector/pins
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc         "Number of pins in connector"}])

(def terminal-schema
  [{:db/ident       :terminal/id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity
    :db/doc         "Terminal ID"}
   {:db/ident       :terminal/connector
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc         "Terminal connector"}
   {:db/ident       :terminal/pin
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc         "Terminal pin"}])

(def wire-schema
  [{:db/ident       :wire/id
    :db/valueType   :db.type/uuid
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

(def connectors-data
  [{:connector/id   (datomic/squuid)
    :connector/kind "JST"
    :connector/label "X STEP"
    :connector/pins 3}
   {:connector/id   (datomic/squuid)
    :connector/kind "SCREWED"
    :connector/label "POWER"
    :connector/pins 2}])

(def terminals-data
  [{:terminal/id        (datomic/squuid)
    :terminal/connector [:connector/id (:connector/id (nth connectors-data 0))]
    :terminal/pin       1}
   {:terminal/id        (datomic/squuid)
    :terminal/connector [:connector/id (:connector/id (nth connectors-data 0))]
    :terminal/pin       2}
   {:terminal/id        (datomic/squuid)
    :terminal/connector [:connector/id (:connector/id (nth connectors-data 1))]
    :terminal/pin       0}
   {:terminal/id        (datomic/squuid)
    :terminal/connector [:connector/id (:connector/id (nth connectors-data 1))]
    :terminal/pin       1}])

(def wires-data
  [{:wire/id        (datomic/squuid)
    :wire/label     "Vcc"
    :wire/color     "red"
    :wire/terminals [[:terminal/id (:terminal/id (nth terminals-data 0))]
                     [:terminal/id (:terminal/id (nth terminals-data 2))]]}
   {:wire/id        (datomic/squuid)
    :wire/label     "GND"
    :wire/color     "brown"
    :wire/terminals [[:terminal/id (:terminal/id (nth terminals-data 1))]
                     [:terminal/id (:terminal/id (nth terminals-data 3))]]}])

(defn load-schema
  [conn]
  (doall
    (map (fn [schema] @(datomic/transact conn schema))
         [connector-schema terminal-schema wire-schema])))

(defn load-initial-data
  [conn]
  (doall
    (map (fn [data] @(datomic/transact conn data))
         [connectors-data terminals-data wires-data])))

