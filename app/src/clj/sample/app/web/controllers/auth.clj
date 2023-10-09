(ns sample.app.web.controllers.auth
  (:require
    [ring.util.response :refer [redirect]]
    [ring.util.http-response :refer [ok unauthorized]]
    [buddy.sign.jwt :as jwt]
    [sample.app.web.middleware.auth :refer [secret-key]]
    [buddy.auth :refer [authenticated?]]
    [clj-time.core :as time]
    [clojure.tools.logging :as log])
  (:import
    [java.time OffsetDateTime]))

(def authdata
  "Global var that stores valid users with their
   respective passwords."
  {:admin "secret"
   :test "secret"})

(defn ->time-now [] (OffsetDateTime/now))

(defn plus-seconds [local-date-time seconds]
  (.plusSeconds local-date-time seconds))

(defn format-time [date-time]
  (let [time-str (.toString date-time)]
    (str (subs time-str 0 (- (count time-str) 7)) "Z")))

(defn logout!
  [request]
  (-> (redirect "/")
      (assoc :session {})))

(defn login!
  [request]
  (let [username (get-in request [:body-params :username])
        password (get-in request [:body-params :password])
        valid? (some-> authdata
                       (get (keyword username))
                       (= password))]
    (if valid?
      (let [claims {:user (keyword username)
                    :exp (time/plus (time/now) (time/seconds 3600))}
            token (jwt/encrypt claims secret-key {:alg :a256kw :enc :a128gcm})]
        (log/info token)
        (ok {:token token}))
      (unauthorized))))

(defn logged
  [request]
  (do
    (log/info (:identity request))
    (ok
      {:response  (authenticated? request)})))
