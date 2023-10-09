(ns sample.app.web.middleware.auth
  (:require
    [ring.util.http-response :refer [unauthorized]]
    [buddy.auth :refer [authenticated?]]
    [buddy.core.hash :as hash]
    [buddy.core.nonce :as nonce]
    [buddy.core.codecs :refer [bytes->hex]]
    [clojure.tools.logging :as log]
    [reitit.middleware :as middleware]))

;; (def secret-key (bytes->hex (hash/sha256 "mysecret")))
(def secret-key (hash/sha256 "mysecret"))

(defn authentication-wrapper
  [handler]
  (fn [request]
    (log/info request)
    (if-not (authenticated? request)
      (unauthorized)
      (handler request))))

(def check-authentication
  (middleware/map->Middleware
    {:name ::check-authentication
     :description "Middleware that checks session authentication and authorization"
     :wrap authentication-wrapper}))
