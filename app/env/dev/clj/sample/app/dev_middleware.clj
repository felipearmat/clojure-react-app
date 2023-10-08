(ns sample.app.dev-middleware
  (:require
    [ring.middleware.cors :refer [wrap-cors]]))

(def allowed-regex
  #"^http(s)?:\/\/(.+\.)?(localhost|0.0.0.0|127.0.0.1|172.16.238.10)(:\d{4})?$")

(defn wrap-dev [handler _opts]
  (-> handler
      (wrap-cors
        :access-control-allow-origin [allowed-regex]
        :access-control-allow-methods [:get :put :post :delete])))
