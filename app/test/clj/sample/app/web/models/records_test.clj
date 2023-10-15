(ns sample.app.web.models.records-test
  (:require
    [clojure.test :refer :all]
    [sample.app.test-utils :as test-utils]
    [sample.app.web.models.operations :as operations]
    [sample.app.web.models.records :as records]
    [sample.app.web.models.users :as users]))

(def user-id (atom nil))

;; Create a user, operations, and some records before each test
(defn base-fixtures [f]
  (users/create-user! "test@mail.com" "Password@1")
  (operations/create-operation! "addition" 10.0)
  (records/create-record! {:operation-id 1
                           :user-id 1
                           :amount 50.0
                           :user-balance 100.0})
  (swap! user-id (first (users/create-user! :all)))
  (f))

(use-fixtures :each test-utils/database-rollback base-fixtures)

(deftest test-create-record!
  (let [valid-data {:operation-id 1
                    :user-id @user-id
                    :amount 50.0
                    :user-balance 100.0}]
    (testing "Create a new record with valid data"
      (is (= 1 (records/create-record! valid-data))))
    (doseq [field (keys valid-data)]
      (testing (str "Shouldn't create a new record when missing " field)
        (is (thrown? Exception
              (records/create-record! (dissoc valid-data field))))))))

(deftest test-get-records
  (testing "Retrieve records based on 'where' conditions"
    (is (= 1 (:user-id (first (records/get-records {:user-id @user-id}))))))

  (testing "Retrieve records with invalid 'where' conditions"
    (is (thrown? Exception (records/get-records {:amount "invalid"})))))

(deftest test-delete-record!
  (testing "Delete a record with a valid ID"
    (is (= 1 (records/delete-record! 1))))

  (testing "Thrown an error when an invalid ID is given"
    (is (thrown? Exception (records/delete-record! "invalid")))))

(deftest test-get-deleted-records
  (testing "Retrieve deleted records based on 'where' conditions"
    (records/delete-record! 1)
    (is (sequential? (records/get-deleted-records {:user-id @user-id}))))

  (testing "Retrieve deleted records with invalid 'where' conditions"
    (is (thrown? Exception (records/get-deleted-records {:amount "invalid"})))))
