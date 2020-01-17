(ns wires.parser
  (:require [com.wsscode.pathom.core :as p]
            [com.wsscode.pathom.connect :as pc]
            [taoensso.timbre :as log]
            [wires.resolvers :as resolvers]
            [wires.mutations :as mutations]))

(defn pathom-parser
  []
  (p/parser {::p/env     {::p/reader                 [p/map-reader
                                                      pc/reader2
                                                      pc/ident-reader
                                                      pc/index-reader]
                          ::pc/mutation-join-globals [:tempids]}
             ::p/mutate  pc/mutate
             ::p/plugins [(pc/connect-plugin {::pc/register [resolvers/resolvers
                                                             mutations/mutations]})
                          p/error-handler-plugin]}))

(defn api-parser [query]
  (log/info "Process" query)
  ((pathom-parser) {} query))
