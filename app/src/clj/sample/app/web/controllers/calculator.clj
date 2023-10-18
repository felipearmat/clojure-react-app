(ns sample.app.web.controllers.calculator
  (:require
    [ring.util.http-response :as http-response]
    [sample.app.calculator.core :as calculator]))

(defn calculate
  [req]
  (let [user (:user (:identity req))
        expression (:expression (:params req))
        result (calculator/calculate! expression)]
    (if (:msg result)
      (http-response/bad-request (:msg result))
      (http-response/ok {:result result}))))
