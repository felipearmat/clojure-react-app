(ns sample.app.web.controllers.auth
  (:require
    [buddy.auth :refer [authenticated?]]
    [buddy.sign.jwt :as jwt]
    [clojure.tools.logging :as log]
    [java-time.api :as jt]
    [ring.middleware.cookies :refer [cookies-response]]
    [ring.util.http-response :refer [ok unauthorized]]
    [sample.app.env :as env]
    [sample.app.web.middleware.core :refer [secret-key]]))

(def authdata
  "Global var that stores valid users with their
   respective passwords."
  {:admin "secret"
   :test "secret"})

(defn login!
  [request]
  (let [username (get-in request [:body-params :username])
        password (get-in request [:body-params :password])
        valid? (some-> authdata
                       (get (keyword username))
                       (= password))]
    (if valid?
      (let [claims     {:user (keyword username)
                        :exp  (jt/plus (jt/instant) (jt/seconds 3600))}
            token      (jwt/encrypt claims secret-key {:alg :a256kw :enc :a128gcm})
            token-name (str (:token-name env/defaults) " ")]
        (cookies-response {:cookies {"value" (str token-name token)
                                     "expires" (:exp claims)}}))
      (unauthorized))))

(defn logged
  [request]
  (do
    (log/info (:identity request))
    (ok
      {:response  (authenticated? request)})))
