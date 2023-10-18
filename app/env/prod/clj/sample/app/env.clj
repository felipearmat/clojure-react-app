(ns sample.app.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init          (fn []
                    (log/info "\n-=[app starting]=-"))
   :start         (fn []
                    (log/info "\n-=[app started successfully]=-"))
   :stop          (fn []
                     (log/info "\n-=[app has shut down successfully]=-"))
   :middleware    (fn [handler _opts] handler)
   :allow-origin  #"^http(s)?:\/\/(.+\.)?(localhost|127.0.0.1|172.16.238.10)(:\d{4})?$"
   :token-name    "Token"
   :opts          {:profile :prod}})

(defn environment [] (:system/env state/config))
