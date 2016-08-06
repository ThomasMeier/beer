(ns beer.beer-io
  (require [gpio.core :refer :all]
           [clojure.java.shell :as shell]))

;; TODO: If a button is pressed, auto is switched off until
;; pressed again.
(def auto? (atom true))
(def solenoid-auto-1 (atom false))
(def solenoid-auto-2 (atom false))

#_(doseq [pin ["21" "20" "16" "12"]]
  (spit "/sys/class/gpio/unexport" pin))

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
