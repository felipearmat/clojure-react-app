(ns sample.app.web.middleware.auth
  (:require
    [buddy.auth :refer [authenticated?]]
    [clojure.string :as str]
    [reitit.middleware :as middleware]
    [ring.util.http-response :refer [get-header unauthorized]]
    [sample.app.env :as env]))

(defn set-token-from-cookie
  [request]
  (assoc-in request [:headers "authorization"]
    (get-in request [:cookies (:cookie-name env/defaults) :value])))

(defn no-authorization?
  [request]
  (let [header (get-header request "authorization")]
    (empty?
      (str/trim
        (str/replace (str header) (re-pattern (:token-name env/defaults)) "")))))

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
