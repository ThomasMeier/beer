(ns beer.handler
  (:require [compojure.core :refer [GET defroutes]]
            [ring.util.response :refer [file-response content-type]]
            [beer.sync :as sync]
            [clojure.java.io :as io]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [clojure.java.shell :as shell]))

(println "Starting!")

;; Set up GPIO
(shell/sh "sudo" "modprobe" "w1-gpio")
(shell/sh "sudo" "modprobe" "w1-therm")

(defroutes app-routes
  (GET "/" [] (slurp (io/resource "public/index.html")))
  (GET "/sync" [] (sync/current-state))
  (GET "/toggle-relay/:which-relay" [which-relay]
       (sync/toggle-relay (keyword which-relay)))
  (GET "/set-target-1/:temperature" [temperature]
       (sync/set-temp-target-1 temperature))
  (GET "/set-target-2/:temperature" [temperature]
       (sync/set-temp-target-2 temperature)))

(def app
  (wrap-defaults app-routes site-defaults))

(println "Restarting nginx")
(shell/sh "sudo" "service" "nginx" "restart")
(println "Started!")
