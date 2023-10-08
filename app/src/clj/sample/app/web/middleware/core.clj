(ns sample.app.web.middleware.core
  (:require
    [sample.app.env :as env]
    [ring.middleware.defaults :as defaults]
    [ring.middleware.session.cookie :as cookie]
    [iapetos.collector.ring :as prometheus-ring]
    [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
    [buddy.auth.backends :as backends]
    [ring.middleware.cors :refer [wrap-cors]]))

;; Create an instance backend auth session
(def backend (backends/session))

;; Get the allowed regex for CORS by ENV
(def allowed-cors-regex
  (:allow-origin env/defaults))

(defn wrap-base
  [{:keys [metrics site-defaults-config cookie-secret] :as opts}]
  (let [cookie-store (cookie/cookie-store {:key (.getBytes ^String cookie-secret)})]
    (fn [handler]
      (-> ((:middleware env/defaults) handler opts)
          ;; (wrap-authentication backend)
          ;; Should put anything that uses session above wrap-defaults
          (defaults/wrap-defaults
            (assoc-in site-defaults-config [:session :store] cookie-store))
          (wrap-cors
            :access-control-allow-origin [allowed-cors-regex]
            :access-control-allow-methods [:get :put :post :delete])
          (cond->
            (some? metrics) (prometheus-ring/wrap-metrics metrics))))))
