(ns sample.app.web.controllers.calculator
  (:require
    [ring.util.http-response :as http-response]
    [sample.app.web.models.records :as records-model]))

(defn calculate
  [req]
  (let [user (:user (:identity req))
        params (:params req)
        operation-type (:operationType params)
        amount-operator (:amountOperator params)
        amount-value (:amountValue params)
        query [:and
                [:= :users.email user]
                (when (seq operation-type)
                  [:= :operations.type operation-type])
                (when (seq operation-cost)
                  [:= :operations.cost operation-cost])
                ;; (when (seq amount-operator)
                ;;   [(keyword amount-operator) :amount amount-value])
                (when (seq start-date)
                  [:>= :records.created_at start-date])
                (when (seq end-date)
                  [:<= :records.created_at end-date])
                ]]
        (http-response/ok (records-model/get-records query))))
