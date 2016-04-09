(ns beer.beer-io
  (require [gpio.core :refer :all]))

;; TODO: If a button is pressed, auto is switched off until
;; pressed again.
(def auto? (atom true))

(def relay-map
  {:pump-1 (open-port 21)
   :pump-2 (open-port 20)
   :solenoid-1 (open-port 16)
   :solenoid-2 (open-port 12)})

(doseq [port (vals relay-map)]
  (set-direction! port :out)
  (write-value! port :high))

(defn switch
  [which-relay state]
  (write-value!
   (which-relay relay-map)
   (if (= :on state) :high :low))
  (if (= :on state)
    {:class "running"
     :running? true}
    {:class "not-running"
     :running? false}))

;; Relay toggles
(defn toggle
  [which-relay app-state]
  (switch
   which-relay
   (if (-> app-state which-relay :running?) :off :on)))

;; Factual state of relays
(defn read-relay
  [which-relay]
  (let [power (read-value (which-relay @relay-map))]
    (if (= :low power)
      {:running? true
       :class "running"}
      {:running? false
       :class "not-running"})))
