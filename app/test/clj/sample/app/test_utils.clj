(ns sample.app.test-utils
  (:require
    [integrant.repl :refer [prep]]
    [integrant.repl.state :as state]
    [next.jdbc :as jdbc]
    [sample.app.web.models.utils :as utils]))

(defn database-rollback
  "Wrap test on a database transaction and rollback it after testing"
  [f]
  (let [query-fn    (:db.sql/query-fn state/system)
        data-source (:db.sql/connection state/system)]
    (jdbc/with-transaction [conn data-source {:isolation :serializable}]
      (with-redefs [utils/db-connector  #(partial query-fn conn)
                    utils/execute-query (partial utils/execute-query-conn conn)]
        (f)
        (.rollback conn)))))

(do (user/test-prep!) (prep) (user/reset-db)) ; initiate test database
(user/use-system :db.sql/query-fn) ; stablish connection with database
