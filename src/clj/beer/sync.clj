(ns beer.sync
  (require [immutant.scheduling :refer :all]
           [cheshire.core :as cheshire]))

(def app-state
  (atom {:temp-1 "72"
         :temp-2 "72"
         :target-1 "212"
         :target-2 "212"
         :solenoid-1 {:class "not-running"
                      :running? false}
         :solenoid-2 {:class "running"
                      :running? true}
         :pump-1 {:class "not-running"
                  :running? false}
         :pump-2 {:class "running"
                  :running? true}}))

;; TEMP SENSORS
(defn get-temp-1 [])

(defn get-temp-2 [])

(defn set-temp-1 [])

(defn set-temp-1 [])

;; TEMP TARGETS
(defn set-temp-target-1 [])

(defn set-temp-target-2 [])

;; RELAY
(defn solenoid-1 [])

(defn solenoid-2 [])

(defn pump-1 [])

(defn pump-2 [])

;; Current state
(defn current-state []
  (cheshire/generate-string @app-state))
