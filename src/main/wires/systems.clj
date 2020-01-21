(ns wires.systems
  (:require [system.core :refer [defsystem]]
            (system.components
              [datomic :refer [new-datomic-db]])
            [wires.server :refer [new-fulcro-handler new-fulcro-server]]
            [wires.config :refer [config]]
            [com.stuartsierra.component :as component]))

(defsystem dev-system
  [:datomic-db (new-datomic-db (config :dev :datomic-db))
   :handler (-> (new-fulcro-handler)
                (component/using [:datomic-db]))
   :wires-http-server (component/using (new-fulcro-server :port (config :dev :wires-http-port))
                                       [:handler])])