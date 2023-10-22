(ns sample.app.web.controllers.auth
  (:require
    [ring.util.http-response :refer [found ok unauthorized]]
    [sample.app.calculator.core :as calculator]
    [sample.app.env :as env]
    [sample.app.models.users :as users]
    [sample.app.auth.core :refer [generate-cookie]]))

(defn login!
  [request]
  (let [email  (get-in request [:body-params :username])
        attemp (get-in request [:body-params :password])]
    (if (users/verify-password attemp email)
      (assoc (ok) :cookies (generate-cookie email))
      (unauthorized))))

(defn logout!
  [request]
  (assoc (found "/") :cookies {(:cookie-name env/defaults) {:value "" :path "/"}}))

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
