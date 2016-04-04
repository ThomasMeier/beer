(ns beer.beer-io
  (require [beer.sync :as sync]
           [gpio.core :refer :all]))

(def relay-map
  {:pump-1 (open-port 2)
   :pump-2 (open-port 3)
   :solenoid-1 (open-port 4)
   :solenoid-2 (open-port 17)})

(doseq [port (values relay-map)]
  (set-direction! port :out)
  (write-value! port :high))

(defn switch
  [which-relay state]
  (write-value!
   (which-relay relay-map)
   (if (= :on state) :high :low))
  (sync/set-relay
   which-relay
   (if (= :on state)
     {:class "running"
      :running? true}
     {:class "not-running"
      :running? false})))

;; Relay toggles
(defn toggle
  [which-relay]
  (if (-> sync/app-state deref which-relay :running?)
    (switch which-relay :off)
    (switch which-relay :on)))

;; Report actual states of relays
(defn read-relay
  [which-relay]
  (let [power (read-value (which-relay @relay-map))]
    (if (= :low power)
      {:running? true
       :class "running"}
      {:running? false
       :class "not-running"})))
