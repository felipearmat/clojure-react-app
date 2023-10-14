(ns sample.app.web.models.operations-test
  (:require
    [clojure.test :refer :all]
    [sample.app.test-utils :as test-utils]
    [sample.app.web.models.operations :refer :all]))

(use-fixtures :each test-utils/database-rollback)

;; Create an operation before each test
(use-fixtures :each {:before (fn [] (create-operation! "addition" 10.0)) })

(deftest test-get-operations
  (testing "It retrieves operations based on 'where' conditions"
    (is (= 1 (count (get-operations {:type "addition"}))))
    (is (= 0 (count (get-operations {:type "non-existent"})))))

  (testing "It throws an exception if 'where' is not a map"
    (is (thrown? Exception (get-operations :type "addition")))
    (is (thrown? Exception (get-operations "addition")))))

(deftest test-delete-operation!
  (testing "It deletes an operation based on 'id'"
    (let [op-id (:id (last (get-operations :all)))]
      (is (= 1 (delete-operation! op-id)))
      (is (= 0 (count (get-operations {:id op-id}))))))

  (testing "It throws an exception if 'id' is not an integer"
    (is (thrown? Exception (delete-operation! "not-an-integer")))
    (is (thrown? Exception (delete-operation! 3.14)))))

(deftest test-update-operations!
  (testing "It updates operations based on 'where' and 'set' conditions"
    (let [op-id (:id (last (get-operations :all)))]
      (is (= 1 (update-operations! {:id op-id} {:cost 20.0})))
      (is (= 1 (count (get-operations {:id op-id}))))
      (is (= 20.0 (:cost (last (get-operations {:id op-id})))))))

  (testing "It throws an exception if 'where' is not a map"
    (is (thrown? clojure.lang.ExceptionInfo (update-operations! "addition" {:cost 20.0}))))

  (testing "It throws an exception if 'set' is not a map"
    (is (thrown? clojure.lang.ExceptionInfo (update-operations! {:type "addition"} "set 20.0")))))

(deftest test-create-operation!
  (testing "It creates a new operation"
    (let [new-op (create-operation! "addition" 10.0)
          new-id (:id new-op)]
      (is (integer? new-id))
      (is (= 1 (count (get-operations {:id new-id}))))
      (is (= 10.0 (-> (last (get-operations {:id new-id})) :cost)))))

  (testing "It throws an exception if 'type' is invalid"
    (is (thrown? clojure.lang.ExceptionInfo (create-operation! "non-existent" 10.0)))
    (is (thrown? clojure.lang.ExceptionInfo (create-operation! "invalid_type" 10.0))))

  (testing "It throws an exception if 'cost' is not a float"
    (is (thrown? clojure.lang.ExceptionInfo (create-operation! "addition" "not-a-float")))
    (is (thrown? clojure.lang.ExceptionInfo (create-operation! "addition" :not-a-float)))
    (is (thrown? clojure.lang.ExceptionInfo (create-operation! "addition" 10)))))
