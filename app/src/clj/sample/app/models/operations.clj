(ns sample.app.models.operations
  (:require
    [clojure.spec.alpha :as spec]
    [sample.app.models.utils :refer [query-fn validate-spec]]))

(spec/def :operations/id int?)

(spec/def :operations/type
  #{"addition"
    "subtraction"
    "multiplication"
    "division"
    "square_root"
    "random_string"})

(spec/def :operations/cost number?)

(spec/def :operations/create-operation!
  (spec/keys :req-un [:operations/cost :operations/type]))

(spec/def :operations/delete-operation!
  (spec/keys :req-un [:operations/cost :operations/type]))

(spec/def :operations/update-operations!
  (spec/keys :req-un [:general/where :general.update/set]))

(defn create-operation!
  "Creates a new operation with the given type and cost.
  Returns 1 on success."
  [type cost]
  (->> {:type type :cost cost}
    (validate-spec :operations/create-operation!)
    (#(query-fn :create-operation! %))))

(defn get-operations
  "Retrieves operations based on the 'where' conditions.
  Returns a collection of maps containing operation fields."
  ([]
  (get-operations nil))
  ([where]
  (->> where
    (validate-spec :general/where)
    (#(query-fn :get-operations {:where %})))))

(defn get-deleted-operations
  "Retrieves operations based on the 'where' conditions.
  Returns a collection of maps containing operation fields."
  ([]
  (get-deleted-operations nil))
  ([where]
  (->> where
    (validate-spec :general/where)
    (#(query-fn :get-deleted-operations {:where %})))))

(defn delete-operation!
  "Deletes an operation identified by its unique ID.
  Returns 1 on success."
  [id]
  (->> id
    (validate-spec :operations/id)
    (#(query-fn :delete-operation! {:id %}))))

(defn update-operations!
  "Updates operations based on 'where' and 'set' conditions.
  Returns the number of affected rows."
  [where set]
  (->> {:where where :set set}
    (validate-spec :operations/update-operations!)
    (query-fn :update-operations!)))
