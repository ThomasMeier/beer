(ns beer.beer-io
  (require [gpio.core :refer :all]
           [clojure.java.shell :as shell]))

;; TODO: If a button is pressed, auto is switched off until
;; pressed again.
(def auto?
  (atom
   {:solenoid-1-enabled true
    :solenoid-2-enabled true
    :solenoid-1-running-target false
    :solenoid-2-running-target false}))

(def relay-map
  {:pump-1 (open-port 21)
   :pump-2 (open-port 20)
   :solenoid-1 (open-port 16)
   :solenoid-2 (open-port 12)})

(doseq [port (vals relay-map)]
  (set-direction! port :out)
  (write-value! port :high))

(defn relay-on-auto?
  "If relay is on auto, then target temps are being used. If button is used,
  then auto is switched off on that relay. When auto is on, this program tries
  to keep within a limit of the temperature."
  [which-relay]
  ())

(defn switch
  [which-relay state]
  (println (str "[SWITCH] " state which-relay))
  (write-value!
   (which-relay relay-map)
   (if (= :on state) :low :high))
  (if (not= :on state)
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
  (let [power (read-value (which-relay relay-map))]
    (if (= :low power)
      {:running? true
       :class "running"}
      {:running? false
       :class "not-running"})))
