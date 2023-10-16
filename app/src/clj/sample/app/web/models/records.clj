(ns sample.app.web.models.records
  (:require
    [clojure.spec.alpha :as spec]
    [sample.app.web.models.utils :refer [execute-query query-fn validate-spec]]))

(spec/def :records/id int?)
(spec/def :records/operation_id int?)
(spec/def :records/user_id string?)
(spec/def :records/amount float?)
(spec/def :records/user_balance float?)
(spec/def :records/operation-response string?)
(spec/def :records/deleted boolean?)

(spec/def :records/create-record!
  (spec/keys :req-un [:records/operation_id
                      :records/user_id
                      :records/amount
                      :records/user_balance]))

(def select-fields
  [[:operations.cost :operation_cost]
   [:operations.type :operation_type]
   [:records.amount :amount]
   [:records.created_at :created_at]
   [:records.user_id :user_id]
   [:records.operation_id :operation_id]
   [:records.id :id]
   [:records.operation_response :operation_response]
   [:records.updated_at :updated_at]
   [:records.user_balance :user_balance]
   [:users.email :user_email]
   [:users.status :user_status]])

(defn format-data
  "Format sent data to the query format of honeysql"
  [data]
  (let [columns (vec (keys data))
        values  (mapv #(get data %) columns)]
        (execute-query {:insert-into [:records]
                        :columns     columns
                        :values      [values]})))

(defn create-record!
  "Create a new record with the provided data. Returns 1 on success."
  [record-data]
  (->> record-data
    (#(assoc % :operation_response "x"))
    (validate-spec :records/create-record!)
    (format-data)
    (#(:next.jdbc/update-count (first %)))))

(defn get-records
  "Retrieve records based on the 'where' conditions. Returns a coll of retrieved records."
  [where]
  (->> where
    (conj [:and [:<> :records.deleted true]])
    (validate-spec :general/query)
    (#(execute-query {:select    select-fields
                      :from      [:records]
                      :join      [:operations [:= :records.operation_id :operations.id]
                                  :users      [:= :records.user_id :users.id]]
                      :where      %
                      :order-by  [:records.id]}))))

(defn get-deleted-records
  "Retrieve deleted records based on the 'where' conditions. Returns a coll of retrieved records."
  [where]
  (->> where
    (#(vec (conj [:and [:= :records.deleted true]] %)))
    (validate-spec :general/query)
    (#(execute-query {:select   (conj select-fields [:records.deleted :deleted])
                      :from     :records
                      :join     [:operations [:= :records.operation_id :operations.id]
                                 :users      [:= :records.user_id :users.id]]
                      :order-by [:records.id]
                      :where %}))))

(defn delete-record!
  "Delete a record identified by its ID. Returns 1 on success."
  [id]
  (->> id
    (validate-spec :records/id)
    (#(execute-query {:update :records
                      :set    {:deleted true}
                      :where  [:= :id %]}))
    (first)
    (:next.jdbc/update-count)))
