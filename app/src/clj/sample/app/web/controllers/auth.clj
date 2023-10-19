(ns sample.app.web.controllers.auth
  (:require
    [buddy.auth :refer [authenticated?]]
    [buddy.sign.jwt :as jwt]
    [clojure.tools.logging :as log]
    [java-time.api :as jt]
    [ring.middleware.cookies :refer [cookies-response]]
    [ring.util.http-response :refer [ok unauthorized bad-request]]
    [sample.app.calculator.core :as calculator]
    [sample.app.env :as env]
    [sample.app.models.credits :as credits]
    [sample.app.models.records :as records]
    [sample.app.models.users :as users]
    [sample.app.web.middleware.core :refer [secret-key]]))

(defn login!
  [request]
  (let [email  (get-in request [:body-params :username])
        attemp (get-in request [:body-params :password])
        valid? (users/verify-password attemp email)]
    (if valid?
      (let [claims     {:user (keyword email)
                        :exp  (jt/plus (jt/instant) (jt/seconds 3600))}
            token      (jwt/encrypt claims secret-key {:alg :a256kw :enc :a128gcm})
            token-name (str (:token-name env/defaults) " ")]
        (cookies-response {:cookies {"value" (str token-name token)
                                     "expires" (:exp claims)}}))
      (unauthorized))))

(defn logout!
  [request]
  (cookies-response {:cookies {"value" ""}}))

(defn data
  [{:keys [identity]}]
  (if-let [email (:user identity)]
    (ok {:logged true
         :balance (calculator/get-user-balance email)
         :email email})
    (ok {:logged false})))

(defn change-password!
  [request]
  (let [old-password (get-in request [:body-params :old-password])
        new-password (get-in request [:body-params :new-password])
        email (get-in request [:body-params :email])]
    (if (users/verify-password old-password email)
      (do
        (users/update-password! new-password email)
        (ok "Password changed"))
      (unauthorized "Invalid old password"))))
