(ns sample.app.web.controllers.auth
  (:require
    [buddy.sign.jwt :as jwt]
    [clj-time.core :as time]
    [ring.util.http-response :refer [ok unauthorized]]
    [sample.app.calculator.core :as calculator]
    [sample.app.env :as env]
    [sample.app.models.users :as users]
    [sample.app.config :refer [secret-key]]))

(defn generate-token
  [identificator]
  (let [claims     {:user (keyword identificator)
                    :exp  (time/plus (time/now) (time/seconds 3600))}
        token      (jwt/encrypt claims secret-key {:alg :a256kw :enc :a128gcm})
        token-name (str (:token-name env/defaults) " ")]
    {"sample.app.token" {:value (str token-name token)
                         :path "/api"
                         :expires (:exp claims)
                         :http-only true}}))

(defn login!
  [request]
  (let [email  (get-in request [:body-params :username])
        attemp (get-in request [:body-params :password])]
    (if (users/verify-password attemp email)
      (assoc (ok) :cookies (generate-token email))
      (unauthorized))))

(defn logout!
  [request]
  (assoc (ok) :cookies {"sample.app.token" {:value ""}}))

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
