(ns sample.app.web.models.utils
  (:require
    [clojure.string :as string]
    [hugsql.parameters :refer [identifier-param-quote]]
    [integrant.repl.state :as state]))

(defn db-error
  [message]
  (ex-info message
     {:type :system.exception/business
      :causes #{:fridge-door-open  :dangerously-high-temperature}
      :current-temperature {:value 25 :unit :celsius}}))

(defn query-fn [& vars]
  (try
    (apply (:db.sql/query-fn state/system) vars)
    (catch Exception e (throw (db-error (-> e ex-cause ex-message))))))

(defn transpile-query
  [params command separator options]
    (clojure.string/join " AND "
      (for [[field value] (get params :where)]
        (let [expression (or (:raw value) (str " = :v" :where "."))]
          (str (identifier-param-quote (name field) options)
            expression (name field))))))

(defn expand-set
  [params options]
    (transpile-query params :set ", " options))

(defn expand-where
  [params options]
  (if (= params {:where :all})
    " id IS NOT NULL "
    (transpile-query params :where " AND " options)))
