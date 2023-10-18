(ns sample.app.utils
  (:require
    [clojure.spec.alpha :as spec]
    [clojure.string :as str]
    [clojure.tools.logging :as log]
    [honey.sql :as hsql]
    [hugsql.parameters :refer [identifier-param-quote]]
    [integrant.repl.state :as state]
    [next.jdbc :as jdbc]
    [next.jdbc.result-set :as rs]
    [sample.app.env :refer [environment]]))

(def uuid-regex
  #"^[0-9a-fA-F]{8}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{12}$")

(spec/def :general/where #(or (map? %) (= nil %)))

(spec/def :general/query #(and (vector? %) (not= % [])))

(spec/def :general/set map?)

(defn contains-some?
  [coll1 coll2]
  "Checks if any element in coll2 is present in coll1."
  (some (set coll1) coll2))

(spec/def :general/unpermitted-set
  #(not (contains-some? % [:id, :updated_at, :created_at])))

(spec/def :general.update/set
  (spec/merge :general/set
              :general/unpermitted-set))

(defn spec-error
  "Generates an exception with a spec validation error message."
  [spec input]
  (ex-info (spec/explain-str spec input)
           {:type :system.exception/business}))

(defn db-error
  "Generates an exception with a database error message."
  [error]
  (ex-info (or (ex-message error) error)
           (merge (ex-data error)
                  {:type :system.exception/business})))

(defn validate-spec
   "Validates input against a spec. Returns
   input if valid, otherwise throws an exception."
  [spec input]
  (if (spec/valid? spec input)
    input
    (throw (spec-error spec input))))

(defn format-hsql-output
  "Formats data of returning Honeysql execute query to match hugsql format"
  [response]
  (:next.jdbc/update-count (first response)))

(defn db-connector
  "Retrieves the database query function from the system state."
  []
  (if-let [query-fn (:db.sql/query-fn state/system)]
    query-fn
    (throw
      (ex-info
        (str
          "Database connection not initialized. Did you execute"
          " (integrant.repl/prep) and (integrant.repl/init)?")
        {:type :system.exception/db-connection-failure}))))

(defn db-connection
  "Retrieves the database query function from the system state."
  []
  (if-let [data-source (:db.sql/connection state/system)]
    data-source
    (throw
      (ex-info
        (str
          "Database connection not initialized. Did you execute"
          " (integrant.repl/prep) and (integrant.repl/init)?")
        {:type :system.exception/db-connection-failure}))))

(defn conn-hsql-execute!
  [conn sqlmap]
    (when (= :dev (environment))
      (log/info (hsql/format sqlmap)))
    (jdbc/execute! conn (hsql/format sqlmap) {:builder-fn rs/as-unqualified-maps}))

(defn hsql-execute!
  [sqlmap]
    (conn-hsql-execute! (db-connection) sqlmap))

(defn query-fn
  "Executes a database query function with provided arguments."
  [& vars]
  (try
    (apply (db-connector) vars)
    (catch Exception e (throw (db-error e)))))

(defn extract-ns
  [field]
  (when (str/includes? field "->")
    (str (first (str/split field #"->")) ".")))

(defn transpile-query
  "Creates a string to be used in hugsql query expressions given parameters."
  [command separator {:keys [namespace] :as params} options]
    (str/join separator
      (for [[field value] (get params command)]
        (let [expression (str " = :v" command ".")
              field-str  (name field)
              field      (last (str/split field-str #"->"))
              field-ns   (extract-ns field-str)]
          (str
            (or field-ns namespace)
            (identifier-param-quote field options)
            expression field-str)))))

(defn map-in? [map1 map2]
  (every? (fn [[k v]] (= (get map2 k) v)) map1))

(def expand-set
  (partial transpile-query :set ", "))

(defn expand-where
  [params options]
  (if (nil? (:where params))
    (str " " (:namespace params) "id IS NOT NULL ")
    (transpile-query :where " AND " params options)))