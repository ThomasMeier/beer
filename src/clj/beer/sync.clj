(ns beer.sync
  (require [immutant.scheduling :refer :all]
           [cheshire.core :as cheshire]
           [clojure.string :as cstr]))

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
(defn c->f [c]
  (format
   "%.2f"
   (+ 32
      (* 1.8
         (* 0.001 (Integer. c))))))

(defn get-temp-1 []
  (c->f
   (last
    (cstr/split
     (slurp
      (System/getenv "TEMP_1"))
     #"="))))

(defn get-temp-2 []
  (c->f
   (last
    (cstr/split
     (slurp
      (System/getenv "TEMP_2"))
     #"="))))


;; TEMP TARGETS
(defn set-temp-target-1 [])

(defn set-temp-target-2 [])

;; RELAY
(defn solenoid-1 [])

(defn solenoid-2 [])

(defn pump-1 [])

(defn pump-2 [])

;; Schedule
(defn job []
  (swap! app-state assoc :temp-1 (get-temp-1))
  (swap! app-sttate assoc :temp-2 (get-temp-2)))

(schedule job
          (-> (in 1 :second)
              (every 2 :seconds)))

;; Current state
(defn current-state []
  (cheshire/generate-string @app-state))
