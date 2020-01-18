(ns wires.server
  (:require [wires.parser :refer [api-parser]]
            [com.fulcrologic.fulcro.server.api-middleware :as fulcro-server]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.resource :refer [wrap-resource]]))

(def ^:private not-found-handler
  (fn [_req]
    {:status  404
     :headers {"Content-Type" "text/plain"}
     :body    "Not Found"}))

(defn middleware
  []
  (-> not-found-handler                                     ; (1)
      (fulcro-server/wrap-api {:uri    "/api"
                               :parser api-parser})         ; (2)
      (fulcro-server/wrap-transit-params)
      (fulcro-server/wrap-transit-response)
      (wrap-resource "public")                              ; (3)
      wrap-content-type))
