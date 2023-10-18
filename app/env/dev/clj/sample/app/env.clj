(ns sample.app.env
  (:require
    [clojure.tools.logging :as log]
    [integrant.repl.state :as state]
    [sample.app.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init          (fn []
                    (log/info "\n-=[app starting using the development or test profile]=-"))
   :start         (fn []
                    (log/info "\n-=[app started successfully using the development or test profile]=-"))
   :stop          (fn []
                    (log/info "\n-=[app has shut down successfully]=-"))
   :middleware    wrap-dev
   :allow-origin  #"^http(s)?:\/\/(.+\.)?(localhost|127.0.0.1|172.16.238.10)(:\d{4})?$"
   :token-name    "Token"
   :opts          {:profile       :dev
                   :persist-data? true}})

(defn environment [] (:system/env state/config))
