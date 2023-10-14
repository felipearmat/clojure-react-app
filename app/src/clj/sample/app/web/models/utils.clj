(ns sample.app.web.models.utils
  (:require
    [clojure.spec.alpha :as spec]
    [hugsql.parameters :refer [identifier-param-quote]]
    [integrant.repl.state :as state]))

(spec/def :general/where #(or (map? %) (= :all %)))

(spec/def :general/set map?)

(defn contains-some?
  [coll1 coll2]
  (some (set coll1) coll2))

(spec/def :general/unpermitted-set
  #(not (contains-some? % [:id, :updated_at, :created_at])))

(spec/def :general.update/set
  (spec/merge :general/set
              :general/unpermitted-set))

(defn spec-error
  [spec input]
  (ex-info (spec/explain-str spec input)
           {:type :system.exception/business}))

(defn db-error
  [error]
  (ex-info (or (ex-message error) error)
           (merge (ex-data error)
                  {:type :system.exception/business})))

(defn validate-spec
  [spec input]
  (if (spec/valid? spec input)
    input
    (throw (spec-error spec input))))

(defn db-connector
  []
  (if-let [query-fn (:db.sql/query-fn state/system)]
    query-fn
    (throw (ex-info "Database connection not initialized. Did you execute (prep) and (init)?" {:type :system.exception/db-connection-failure}))))

(defn query-fn [& vars]
  (try
    (apply (db-connector) vars)
    (catch Exception e (throw (db-error e)))))

(defn transpile-query
  [command separator params options]
    (clojure.string/join separator
      (for [[field value] (get params command)]
        (let [expression (or (:raw value) (str " = :v" command "."))]
          (str (identifier-param-quote (name field) options)
            expression (name field))))))

(defn expand-set
  [params options]
  (transpile-query :set ", " params options))

(defn expand-where
  [params options]
  (if (= :all (:where params))
    " id IS NOT NULL "
    (transpile-query :where " AND " params options)))
