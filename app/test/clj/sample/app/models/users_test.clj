(ns sample.app.models.users-test
  (:require
    [clojure.test :refer :all]
    [clojure.string :as str]
    [sample.app.test-utils :as test-utils]
    [sample.app.models.users :refer :all]))

(def valid-email "test@mail.com")
(def scrambled-email "TeSt@MaIl.CoM")
(def valid-password "Password@1")

(defn user-fixture [f]
  (create-user! valid-email valid-password)
  (f))

(use-fixtures :each test-utils/database-rollback user-fixture)

(deftest test-get-users
  (testing "Should retrieve users when no keyword"
    (is (= 1 (count (get-users))))))

(deftest test-create-user!
  (testing "Should create a single user"
    (is (= 1 (count (get-users [:= :users.email valid-email])))))

  (testing "Should ensure email matches"
    (is (= valid-email (:email (last (get-users))))))

  (testing "Shouldn't allow users with the same email"
    (is (thrown? Exception (create-user! valid-email valid-password)))))

(deftest test-update-password!
  (let [new-password (str valid-password "0")]
    (testing "Should update with a valid password"
      (is (= 1 (update-password! new-password valid-email)))
      (is (verify-password new-password valid-email)))

    (testing "Shouldn't update with an invalid password"
      (testing "When missing special characters"
        (let [bad-password (str/replace valid-password #"@" "")]
          (is (thrown? Exception (update-password! bad-password)))))

      (testing "When missing uppercase letters"
        (let [bad-password (str/replace valid-password #"[A-Z]" "")]
          (is (thrown? Exception (update-password! bad-password)))))

      (testing "When missing digits"
        (let [bad-password (str/replace valid-password #"\d" "")]
          (is (thrown? Exception (update-password! bad-password))))))))

(deftest test-deactivate-user!
  (testing "Should deactivate a user"
    (deactivate-user! valid-email)
    (let [user (last (get-users [:= :users.email valid-email]))]
      (is (= "inactive" (:status user))))))

(deftest test-activate-user!
  (testing "Should activate a user"
    (deactivate-user! valid-email) ; Deactivate the user first
    (activate-user! valid-email)
    (let [user (last (get-users [:= :users.email valid-email]))]
      (is (= "active" (:status user))))))

(deftest test-delete-user!
  (testing "Should delete a user"
    (delete-user! valid-email)
    (is (empty? (get-users [:= :users.email valid-email])))
    (is (seq (get-deleted-users [:= :users.email valid-email])))))

(deftest test-update-users!
  (testing "Should update a user's status"
    (update-users! [:= :users.email valid-email] {:status "inactive"})
    (let [user (last (get-users [:= :users.email valid-email]))]
      (is (= "inactive" (:status user)))))

  (testing "Should update a user's email"
    (let [new-valid-email (str "a" valid-email)]
      (update-users! [:= :users.email valid-email] {:email new-valid-email})
      (is (empty? (get-users [:= :users.email valid-email])))
      (is (seq (get-users [:= :users.email new-valid-email]))))))
