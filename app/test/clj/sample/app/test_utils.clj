(ns sample.app.test-utils
  (:require
    [integrant.repl :refer [prep]]
    [integrant.repl.state :as state]
    [next.jdbc :as jdbc]
    [sample.app.models.utils :as utils]))

(defn mock
  "Creates a mock function that allows counting how many times it was called."
  [f]
  (let [counter (atom 0)]
    (fn [& args]
      (swap! counter inc)
      (apply f args))))

(defn database-rollback
  "Wrap test on a database transaction and rollback it after testing"
  [f]
  (let [query-fn    (:db.sql/query-fn state/system)
        data-source (:db.sql/connection state/system)]
    (jdbc/with-transaction [conn data-source {:isolation :serializable}]
      (with-redefs [utils/db-connector  #(partial query-fn conn)
                    utils/hsql-execute! (partial utils/conn-hsql-execute! conn)]
        (f)
        (.rollback conn)))))

(do (user/test-prep!) (prep) (user/reset-db)) ; initiate test database
(user/use-system :db.sql/query-fn) ; stablish connection with database
