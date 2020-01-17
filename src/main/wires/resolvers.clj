(ns wires.resolvers
  (:require [com.wsscode.pathom.core :as p]
            [com.wsscode.pathom.connect :as pc :refer [defresolver]]))

(def terminals-table
  {0 {:terminal/id        0
      :terminal/connector "JST.0"
      :terminal/pin       1}
   1 {:terminal/id        1
      :terminal/connector "JST.0"
      :terminal/pin       2}
   2 {:termina/id         2
      :terminal/connector "SCREWED.0"
      :terminal/pin       0}
   3 {:termina/id         3
      :terminal/connector "SCREWED.0"
      :terminal/pin       1}})

(def wire-table
  (atom
    {0 {:wire/id        0
        :wire/label     "Vcc"
        :wire/color     "red"
        :wire/terminals [{:terminal/id 0}
                         {:terminal/id 2}]}
     1 {:wire/id        1
        :wire/label     "GND"
        :wire/color     "brown"
        :wire/terminals [{:terminal/id 1}
                         {:terminal/id 3}]}}))

(def wire-list-table
  (atom {:my-wires {:wire-list/id    :my-wires
                    :wire-list/wires [{:wire/id 0}
                                      {:wire/id 1}]}}))

(defresolver terminal-resolver [_env {:terminal/keys [id]}]
  {::pc/input  #{:terminal/id}
   ::pc/output [:terminal/connector :terminal/pin]}
  (get terminals-table id))

(defresolver wire-resolver [_env {:wire/keys [id]}]
  {::pc/input  #{:wire/id}
   ::pc/output [:wire/label :wire/color {:wire/terminals [:terminal/id]} :wire/saved]}
  (merge
    (get @wire-table id)
    {:wire/saved? true}))

(defresolver wire-list-resolver [_env {:wire-list/keys [id]}]
  {::pc/input  #{:wire-list/id}
   ::pc/output [:wire-list/wires]}
  (get @wire-list-table id))

(defresolver wires-resolver [_env _input]
  {::pc/output [{:wires [:wire-list/id]}]}
  {:wires {:wire-list/id :my-wires}})

(def resolvers [terminal-resolver wire-resolver wire-list-resolver wires-resolver])