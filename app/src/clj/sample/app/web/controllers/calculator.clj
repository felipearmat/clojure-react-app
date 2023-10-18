(ns sample.app.web.controllers.calculator
  (:require
    [ring.util.http-response :as http-response]
    [sample.app.calculator.core :as calculator]
    [clojure.tools.logging :as log]
    [sample.app.models.users :as users]))

(defn calculate
  [{:keys [identity params]}]
  (let [user (first (users/get-users [:= :users.email (:user identity)]))
        b (log/info identity)
        expression (:expression params)
        b (log/info expression)
        result (calculator/calculate! (:id user) expression)
        b (log/info result)]
    (if (:msg result)
      (http-response/bad-request (:msg result))
      (http-response/ok {:result result}))))
