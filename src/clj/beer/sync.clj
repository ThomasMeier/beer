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

(def target-1 (atom false))
(def target-2 (atom false))

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
  (reset! target-1 true)
  (swap! app-state assoc :target-1 temperature))

(defn set-temp-target-2
  [temperature]
  (reset! target-2 true)
  (swap! app-state assoc :target-2 temperature))

(defn toggle-relay
  [which-relay]
  (println (str which-relay))
  (swap! app-state assoc which-relay
         (beer-io/toggle which-relay @app-state)))

;; Relay logic
(defn relay-logic
  [which-relay which-target which-temp]
  (let [relay-state (-> @app-state which-relay :running?)
        target (-> @app-state which-target )
        temp (-> @app-state which-temp)
        temp-neighborhood [(- temp 1)
                           (+ temp 1)]]
    (cond
      (> temp (first temp-neighborhood)) true
      (> temp (second temp-neighborhood)) true)))

;; Schedule
(defn job []
  (swap! app-state assoc :temp-1 (get-temp-1))
  (swap! app-state assoc :temp-2 (get-temp-2))
  (when @target-1 (relay-logic :solenoid-1 :target-1 :temp-1))
  (when @target-2 (relay-logic :solenoid-2 :target-2 :temp-2))
  (doseq [relay [:pump-1 :pump-2 :solenoid-1 :solenoid-2]]
    (swap! app-state assoc relay (beer-io/read-relay relay))))

(schedule job
          (-> (in 1 :second)
              (every 1 :seconds)))

;; Current state
(defn current-state []
  (cheshire/generate-string @app-state))
