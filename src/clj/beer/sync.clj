(ns beer.sync
  (require [immutant.scheduling :refer :all]
           [cheshire.core :as cheshire]
           [clojure.string :as cstr]
           [beer.beer-io :as beer-io]))

(def app-state
  (atom {:temp-1 "82"
         :temp-2 "71"
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

;; Temperature sensors
(defn c->f [c]
  (format
   "%.2f"
   (+ 32
      (* 1.8
         (* 0.001 (Integer. (cstr/trim c)))))))

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

;; Update relay from beer io
(defn set-relay
  [which-relay state]
  (swap! app-state assoc which-relay state))

;; Temperature targets
(defn set-temp-target-1
  [temperature]
  (swap! app-state assoc :target-1 temperature))

(defn set-temp-target-2
  [temperature]
  (swap! app-state assoc :target-2 temperature))

;; Schedule
(defn job []
  (swap! app-state assoc :temp-1 (get-temp-1))
  (swap! app-state assoc :temp-2 (get-temp-2))
  (doseq [relay [:pump-1 :pump-2 :solenoid-1 :solenoid-2]]
    (swap! app-state assoc relay (beer-io/read-relay relay))))

(schedule job
          (-> (in 1 :second)
              (every 2 :seconds)))

;; Current state
(defn current-state []
  (cheshire/generate-string @app-state))
