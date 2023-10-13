(ns sample.app.web.models.users-test
  (:require
    [clojure.test :refer :all]
    [clojure.string :as str]
    [sample.app.test-utils :as test-utils]
    [sample.app.web.models.users :refer :all]))

(def valid-email "test@mail.com")

(def scrambled-email "TeSt@MaIl.CoM")

(def valid-password "Password@1")

(use-fixtures :each test-utils/database-rollback)

(deftest normalize-where-test
  (testing "Do not add an :email uneccesarily"
    (let [where {:anykey "anything"}
          result (normalize-where where)]
      (is (= (keys where) (keys result)))))

  (testing "Normalize :email"
    (let [where {:email "UsErExAmPlE@example.com"}
          result (normalize-where where)
          normalized (normalize-email (:email where))]
      (is (= normalized (:email result))))))

(deftest get-users-test
  (testing "Allow keyword :all"
    (is (= 0 (count (get-users :all)))))

  (testing "Query with normalized e-mail"
    (create-user! valid-email valid-password)
    (let [result (first (get-users {:email scrambled-email}))]
      (is (= valid-email (:email result))))))

(deftest create-user!-test
  (create-user! valid-email valid-password)
  (testing "Should create a single user"
    (is (= 1 (count (get-users {:email valid-email})))))

  (testing "Email must match"
    (is (= valid-email (:email (first (get-users :all))))))

  (testing "Shouldn't allow users with same email"
    (is (thrown? Exception
          (create-user! valid-email valid-password)))))

(deftest update-password!-test
  (let [new-password "Password@2"
        update-password #(update-password! % valid-email)]
    (create-user! valid-email valid-password)

    (testing "Should update with valid password"
      (is (= 1 (update-password new-password))))

    (testing "Shouldn't update with invalid password"
      (testing "when missing special character"
        (let [bad-password (str/replace valid-password #"@" "")]
          (is (thrown? Exception (update-password bad-password)))))

      (testing "when missing uppercase letter"
        (let [bad-password (str/replace valid-password #"[A-Z]" "")]
          (is (thrown? Exception (update-password bad-password)))))

      (testing "when missing digit"
        (let [bad-password (str/replace valid-password #"\d" "")]
          (is (thrown? Exception (update-password bad-password))))))))

(deftest verify-password-test
  (create-user! valid-email valid-password)

  (testing "Return true with right password"
    (is (= true (verify-password valid-password valid-email))))

  (testing "Return nil with wrong password"
    (is (not (verify-password (str "a" valid-password) valid-email)))))

(deftest deactivate-user!-test
  (create-user! valid-email valid-password)

  (testing "Deactivate a user"
    (deactivate-user! valid-email)
    (let [user (first (get-users {:email valid-email}))]
      (is (= "inactive" (:status user))))))

(deftest activate-user!-test
  (create-user! valid-email valid-password)

  (testing "Activate a user"
    (deactivate-user! valid-email) ; Deactivate the user first
    (activate-user! valid-email)
    (let [user (first (get-users {:email valid-email}))]
      (is (= "active" (:status user))))))

(deftest delete-user!-test
  (create-user! valid-email valid-password)

  (testing "Delete a user"
    (delete-user! valid-email)
    (is (empty? (get-users {:email valid-email})))
    (is (seq (get-deleted-users {:email valid-email})))))

(deftest update-user!-test
  (create-user! valid-email valid-password)

  (testing "Update a user's status"
    (update-user! {:email valid-email} {:status "inactive"})
    (let [user (first (get-users {:email valid-email}))]
      (is (= "inactive" (:status user)))))

  (testing "Update a user's email"
    (let [new-email "new_email@example.com"]
      (update-user! {:email valid-email} {:email new-email})
      (is (empty? (get-users {:email valid-email})))
      (is (seq (get-users {:email new-email}))))))
