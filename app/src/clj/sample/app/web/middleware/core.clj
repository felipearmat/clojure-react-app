(ns sample.app.web.middleware.core
  (:require
    [sample.app.env :as env]
    [ring.middleware.defaults :as defaults]
    [ring.middleware.session.cookie :as cookie]
    [clojure.tools.logging :as log]
    [buddy.auth.backends.token :refer [jwe-backend]]
    [sample.app.web.middleware.auth :refer [secret-key]]
    ;; [buddy.auth.accessrules :refer [wrap-access-rules]]
    [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
    [iapetos.collector.ring :as prometheus-ring]
    [ring.middleware.cors :refer [wrap-cors]]))

;; (def rules [{:uri "/foo/*"
;;              :handler (fn [_] true)}
;;             {:uri "/bar/*"
;;              :handler {:or [(fn [_] true) (fn [_] false)]}}
;;             {:uri "/baz/*"
;;              :handler {:and [(fn [_] true) {:or [(fn [_] false) (fn [_] false)]}]}}])

;; Create jwe based backend manager
(def auth-backend (jwe-backend {:secret secret-key
                                :options {:alg :a256kw :enc :a128gcm}}))

(defn logger-middleware [handler]
  (fn [request]
    (log/info (:body request))
    (handler request)))

;; Get the allowed regex for CORS by ENV
(def allowed-cors-regex
  (:allow-origin env/defaults))

(defn wrap-base
  [{:keys [metrics site-defaults-config cookie-secret] :as opts}]
  (let [cookie-store (cookie/cookie-store {:key (.getBytes ^String cookie-secret)})]
    (fn [handler]
      (-> ((:middleware env/defaults) handler opts)
          ;; (wrap-access-rules (assoc opts :rules rules))
          (wrap-authorization auth-backend)
          (wrap-authentication auth-backend)
          ;; Should put anything that uses session above wrap-defaults
          (defaults/wrap-defaults
            (assoc-in site-defaults-config [:session :store] cookie-store))
          (wrap-cors
            :access-control-allow-origin [allowed-cors-regex]
            :access-control-allow-methods [:get :put :post :delete])
          (cond->
            (some? metrics) (prometheus-ring/wrap-metrics metrics))))))
