(ns sample.app.web.middleware.auth
  (:require
    [buddy.auth :refer [authenticated?]]
    [clojure.string :as s]
    [reitit.middleware :as middleware]
    [ring.util.codec :as codec]
    [ring.util.http-response :refer [get-header unauthorized]]
    [sample.app.env :as env]))

(defn ->cookie-token
  [request]
  (let [header (get-header request "cookie")]
    (if (seq header)
      (let [token (get (codec/form-decode header) "value")]
        (if (seq token)
          (first
            (s/split token #";")))))))

(defn set-authorization
  [request value]
  (assoc request
    :headers
    (merge (:headers request) {"authorization" value})))

(defn set-token-from-cookie
  [request]
  (set-authorization request (->cookie-token request)))

(defn no-authorization?
  [request]
  (let [header (get-header request "authorization")]
    (empty?
      (s/trim
        (s/replace (str header) (re-pattern (:token-name env/defaults)) "")))))

(defn wrap-ensure-token-middleware
  [handler]
  (fn [request]
    (cond-> request
      (no-authorization? request) (set-token-from-cookie)
      true (handler))))

(defn wrap-authentication-middleware
  [handler]
  (fn [request]
    (if-not (authenticated? request)
      (unauthorized)
      (handler request))))

(def authentication-middleware
  (middleware/map->Middleware
    {:name ::authentication-middleware
     :description "Middleware that checks authentication and authorization"
     :wrap wrap-authentication-middleware}))
