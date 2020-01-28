(ns wires.config
  (:require [aero.core :refer [read-config]]
            [clojure.java.io :as io]))

(defn config
  "Get specific part of `PROFILE` system configuration from `(IO/RESOURCES \"wired.edn\")`."
  ([profile]
   (read-config (io/resource "wires.edn")
                {:profile profile}))
  ([profile & key]
   (get-in (read-config (io/resource "wires.edn")
                        {:profile profile})
           key)))
