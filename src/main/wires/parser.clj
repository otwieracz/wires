(ns wires.parser
  (:require [com.wsscode.pathom.core :as p]
            [com.wsscode.pathom.connect :as pc]
            [taoensso.timbre :as log]
            [wires.resolvers :as resolvers]
            [wires.mutations :as mutations]))

(defn make-pathom-parser
  [datomic]
  (p/parser {::p/env     {::p/reader                 [p/map-reader
                                                      pc/reader2
                                                      pc/ident-reader
                                                      pc/index-reader]
                          ::pc/mutation-join-globals [:tempids]
                          :datomic                   datomic}
             ::p/mutate  pc/mutate
             ::p/plugins [(pc/connect-plugin {::pc/register [(resolvers/resolvers)
                                                             (mutations/mutations)]})
                          p/error-handler-plugin]}))

(defn make-api-parser [datomic]
  (fn [query]
    ;; it's important to make new parser every single query to reload
    ;; changes dynamicly when mutations 
    (let [parser (make-pathom-parser datomic)]
      (log/info "Processing query: " query)
      (parser {} query))))
