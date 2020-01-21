(ns wires.server
  (:require [com.fulcrologic.fulcro.server.api-middleware :as fulcro-server]
            [com.stuartsierra.component :as component]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.resource :refer [wrap-resource]]
            [wires.parser :refer [make-api-parser]]
            [org.httpkit.server :refer [run-server]]
            [system.components.http-kit :refer [Options]]
            [schema.core :as s]))

(def ^:private not-found-handler
  (fn [_req]
    {:status  404
     :headers {"Content-Type" "text/plain"}
     :body    "Not Found"}))

(defrecord FulcroHandler [db]
  component/Lifecycle
  (start [component]
    (let [handler (fn [] (-> not-found-handler
                             (fulcro-server/wrap-api {:uri "/api" :parser (make-api-parser db)})
                             (fulcro-server/wrap-transit-params)
                             (fulcro-server/wrap-transit-response)
                             (wrap-resource "public")
                             wrap-content-type))]
      (assoc component :fulcro-handler handler)))
  (stop [component]
    (dissoc component :fulcro-handler)))

(defn new-fulcro-handler [] (map->FulcroHandler {}))

(defrecord FulcroServer [options server handler]
  component/Lifecycle
  (start [component]
    (let [server (run-server ((:fulcro-handler handler)) options)]
      (assoc component :server server)))
  (stop [component]
    (when server
      (server)
      component)))

(defn new-fulcro-server [& {:keys [port options]}]
  (map->FulcroServer {:options (s/validate Options (merge {:port port} options))}))
