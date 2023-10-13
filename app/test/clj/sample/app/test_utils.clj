(ns sample.app.test-utils
  (:require
    [clojure.test :refer :all]
    [integrant.repl.state :as state]
    [integrant.repl :refer [prep]]
    [sample.app.core :as core]))

(defn system-state
  []
  (or @core/system state/system))

(defn system-fixture
  "Reset database state between each deftest"
  [f]
    ;; reset database before each test to avoid data left from last deftest
    (user/reset-db)
    (f))

(do (user/test-prep!) (prep) (user/migrate)) ; initiate test database
(use-fixtures :each system-fixture) ; apply system-fixture
(user/use-system :db.sql/query-fn) ; stablish connection with database
