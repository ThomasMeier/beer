(ns beer.handlers
    (:require [re-frame.core :as re-frame]
              [beer.db :as db]
              [ajax.core :refer [GET]]))

(re-frame/register-handler
 :initialize-db
 (fn  [_ _]
   db/default-db))

;; Set temperature targets
(re-frame/register-handler
 :set-target-1
 (fn [db [_ target-temp]]
   (GET (str "/set-target-1/" target-temp))
   (assoc db :target-1 target-temp)))

(re-frame/register-handler
 :set-target-2
 (fn [db [_ target-temp]]
   (GET (str "/set-target-2/" target-temp))
   (assoc db :target-2 target-temp)))

;; Start/Stop Relays
;; relay-key must be the name from db, ex. :pump-1
(re-frame/register-handler
 :toggle-relay
 (fn [db [_ relay-key]]
   (GET (str "/toggle-relay/" (name relay-key)))
   (if-let [running? (:running? (relay-key db))]
     (assoc db relay-key {:running? false :class "not-running"})
     (assoc db relay-key {:running? true :class "running"}))))

;; Keep Pi io and this program's data synchronized
(re-frame/register-handler
 :do-sync
 (fn [db [_ resp]]
   (.log js/console (str (js->clj resp)))
   (js->clj resp)))

(re-frame/register-handler
 :sync-db
 (fn [db _]
   (GET "/sync"
        {:handler #(re-frame/dispatch [:do-sync %1])
         :error #(.log js/console (str "[SYNC FAILURE] " %1))})
   db))

(js/setInterval #(re-frame/dispatch [:sync-db]) 3000)
