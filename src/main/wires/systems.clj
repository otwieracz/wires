(ns wires.systems
  (:require [system.core :refer [defsystem]]
            (system.components
              [datomic :refer [new-datomic-db]]
              [http-kit :refer [new-http-kit]])
            [wires.config :refer [config]]
            [wires.server :refer [middleware]]))

(defsystem dev-system
  [:datomic-db (new-datomic-db (config :dev :datomic-db))
   :wire-http-server (new-http-kit :port (config :dev :wires-http-port)
                                   :handler (middleware))])
