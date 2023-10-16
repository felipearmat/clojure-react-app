(ns sample.app.web.models.credits
  (:require
    [clojure.spec.alpha :as spec]
    [sample.app.web.models.utils :refer [execute-query query-fn validate-spec]]))

(spec/def :credits/id int?)
(spec/def :credits/user_id :users/uuid)
(spec/def :credits/value #(and (float? %) (pos? %)))

(spec/def :credits/add-credit!
  (spec/keys :req-un [:credits/user_id
                      :credits/value]))

(def select-fields
  [[:operations.cost :operation_cost]
   [:operations.type :operation_type]
   [:credits.amount :amount]
   [:credits.created_at :created_at]
   [:credits.user_id :user_id]
   [:credits.operation_id :operation_id]
   [:credits.id :id]
   [:credits.operation_response :operation_response]
   [:credits.updated_at :updated_at]
   [:credits.user_balance :user_balance]
   [:users.email :user_email]
   [:users.status :user_status]])

(defn format-data
  "Format sent data to the query format of honeysql"
  [data]
  (let [columns (vec (keys data))
        values  (mapv #(get data %) columns)]
        (execute-query {:insert-into [:credits]
                        :columns     columns
                        :values      [values]})))

(defn add-credit!
  "Create a new credit with the provided data. Returns 1 on success."
  [user-id value]
  (->> {:user_id user-id :value value}
    (validate-spec :credits/create-credit!)
    (format-data)
    (#(:next.jdbc/update-count (first %)))))

(defn get-credits
  "Retrieve credits based on the 'where' conditions. Returns a coll of retrieved credits."
  [where]
  (->> where
    (conj [:and [:<> :credits.deleted true]])
    (validate-spec :general/query)
    (#(execute-query {:select    select-fields
                      :from      [:credits]
                      :join      [:operations [:= :credits.operation_id :operations.id]
                                  :users      [:= :credits.user_id :users.id]]
                      :where      %
                      :order-by  [:credits.id]}))))

(defn get-deleted-credits
  "Retrieve deleted credits based on the 'where' conditions. Returns a coll of retrieved credits."
  [where]
  (->> where
    (#(vec (conj [:and [:= :credits.deleted true]] %)))
    (validate-spec :general/query)
    (#(execute-query {:select   (conj select-fields [:credits.deleted :deleted])
                      :from     :credits
                      :join     [:operations [:= :credits.operation_id :operations.id]
                                 :users      [:= :credits.user_id :users.id]]
                      :order-by [:credits.id]
                      :where %}))))

(defn delete-credit!
  "Delete a credit identified by its ID. Returns 1 on success."
  [id]
  (->> id
    (validate-spec :credits/id)
    (#(execute-query {:update :credits
                      :set    {:deleted true}
                      :where  [:= :id %]}))
    (first)
    (:next.jdbc/update-count)))
