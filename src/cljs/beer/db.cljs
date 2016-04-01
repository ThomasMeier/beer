(ns beer.db)

(def default-db
  {:temp-1 "72"
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
            :running? true}})
