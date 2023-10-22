(ns sample.app.web.controllers.record
  (:require
    [ring.util.http-response :as http-response]
    [sample.app.calculator.core :as calculator]
    [sample.app.models.records :as records]))

(defn get-records
  [req]
  (let [user (:user (:identity req))
        params (:params req)
        operation-type (:operationType params)
        operation-cost (:operationCost params)
        amount-operator (:amountOperator params)
        amount-value (:amountValue params)
        start-date (:startDate params)
        end-date (:endDate params)
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
        (http-response/ok {:records (records/get-records query)})))

(defn delete-records!
  [{:keys [identity body-params]}]
  (let [email (:user identity)
        records (:records body-params)
        total (records/delete-records! records)
        user-balance (calculator/get-user-balance email)
        message (str " record" (if (> total 1) "s") " deleted sucessfully!")]
        (http-response/ok {:message (str total message)
                           :balance user-balance})))
