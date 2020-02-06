(ns wires.ui.mutations
  "UI-local mutations"
  (:require [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]))

(defmutation change-active-tab
  [{active-tab :active-tab}]
  (action [{:keys [state]}]
          (swap! state
                 #(assoc % :active-tab active-tab))))

(defmutation change-add-connector-modal-state
  [{:keys [modal-id new-state]}]
  (action [{:keys [state]}]
          (swap! state #(-> %
                            (assoc-in [:add-connector-modal/id modal-id :add-connector-modal/open] new-state)
                            ;; clean state
                            (update-in [:add-connector-modal/id modal-id] dissoc :add-connector-modal/label)
                            (update-in [:add-connector-modal/id modal-id] dissoc :add-connector-modal/kind)
                            (update-in [:add-connector-modal/id modal-id] dissoc :add-connector-modal/pins)
                            ))))

(defmutation update-add-connector-modal-form
  [{:add-connector-modal/keys [id field value]}]
  (action [{:keys [state]}]
          (let [[property-key coerce-fn] (case field
                                           :label [:add-connector-modal/label str]
                                           :kind [:add-connector-modal/kind str]
                                           :pins [:add-connector-modal/pins js/parseInt])]
            (swap! state
                   #(assoc-in % [:add-connector-modal/id id property-key] (coerce-fn value))))))

