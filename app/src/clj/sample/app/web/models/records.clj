(ns sample.app.web.models.records
  (:require
    [clojure.spec.alpha :as spec]
    [sample.app.web.models.utils :refer [query-fn validate-spec]]))

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

(defn create-record!
  "Create a new record with the provided data. Returns 1 on success."
  [record-data]
  (->> record-data
    (#(assoc % :operation_response ""))
    (validate-spec :records/create-record!)
    (query-fn :create-record!)))

(defn get-records
  "Retrieve records based on the 'where' conditions. Returns a coll of retrieved records."
  [where]
  (->> where
    (validate-spec :general/where)
    (#(query-fn :get-records {:where % :namespace "records."}))))

(defn get-deleted-records
  "Retrieve deleted records based on the 'where' conditions. Returns a coll of retrieved records."
  [where]
  (->> where
    (validate-spec :general/where)
    (#(query-fn :get-deleted-records {:where % :namespace "records."}))))

(defn delete-record!
  "Delete a record identified by its ID. Returns 1 on success."
  [id]
  (->> id
    (validate-spec :records/id)
    (#(query-fn :delete-record! {:id %}))))
