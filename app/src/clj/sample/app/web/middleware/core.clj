(ns sample.app.web.middleware.core
  (:require
    ;; [buddy.auth.accessrules :refer [wrap-access-rules]]
    [buddy.auth.backends.token :refer [jwe-backend]]
    [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
    [buddy.core.hash :as hash]
    [clojure.tools.logging :as log]
    [iapetos.collector.ring :as prometheus-ring]
    [ring.middleware.cors :refer [wrap-cors]]
    [ring.middleware.defaults :as defaults]
    [ring.middleware.session.cookie :as cookie]
    [sample.app.env :as env]
    [sample.app.web.middleware.auth :refer [wrap-ensure-token-middleware]]))

;; (def rules [{:uri "/foo/*"
;;              :handler (fn [_] true)}
;;             {:uri "/bar/*"
;;              :handler {:or [(fn [_] true) (fn [_] false)]}}
;;             {:uri "/baz/*"
;;              :handler {:and [(fn [_] true) {:or [(fn [_] false) (fn [_] false)]}]}}])

(def secret-key (hash/sha256 "mysecretkey"))

;; Create jwe based backend manager
(def auth-backend (jwe-backend {:secret secret-key
                                :options {:alg :a256kw :enc :a128gcm}
                                :token-name (:token-name env/defaults)}))

(defn wrap-logger
  [handler]
  (fn [request]
    (when (= :dev (env/environment))
      (log/info request))
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
          (wrap-logger)
          (wrap-authorization auth-backend)
          (wrap-authentication auth-backend)
          (wrap-ensure-token-middleware)
          ;; Should put anything that uses session above wrap-defaults
          (defaults/wrap-defaults
            (assoc-in site-defaults-config [:session :store] cookie-store))
          (wrap-cors
            :access-control-allow-origin [allowed-cors-regex]
            :access-control-allow-methods [:get :put :post :delete])
          (cond->
            (some? metrics) (prometheus-ring/wrap-metrics metrics))))))
