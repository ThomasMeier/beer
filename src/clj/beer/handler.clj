(ns beer.handler
  (:require [compojure.core :refer [GET defroutes]]
            [ring.util.response :refer [file-response content-type]]
            [beer.sync :as sync]
            [clojure.java.io :as io]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defroutes app-routes
  (GET "/" [] (content-type
               (file-response "index.html" {:root "resources/public"})
               "text/html"))
  (GET "/sync" [] (sync/current-state))
  (GET "/toggle-relay/:which-relay" [which-relay])
  (GET "/set-target-1/:temperature" [temperature])
  (GET "/set-target-2/:temperature" [temperature]))

(def app
  (wrap-defaults app-routes site-defaults))