(ns sample.app.utils-test
  (:require
    [clojure.test :refer :all]
    [integrant.repl.state :as state]
    [sample.app.utils :refer :all]))

(deftest test-contains-some?
  (testing "Should return true if any element in coll2 is in coll1"
    (is (contains-some? [1 2 3] [3 4 5]))
    (is (contains-some? [:a :b :c] [:b :d :e]))
    (is (contains-some? #{:x :y :z} [:a :x :b])))

  (testing "Should return false if none of the elements in coll2 is in coll1"
    (is (not (contains-some? [1 2 3] [4 5 6])))
    (is (not (contains-some? [:a :b :c] [:d :e :f])))
    (is (not (contains-some? #{:x :y :z} [:a :b :c])))))

(deftest test-validate-spec
  (testing "It returns the input if it's valid"
    (is (= "test" (validate-spec string? "test"))))

  (testing "It throws an exception if input is not valid"
    (is (thrown? Exception (validate-spec integer? "not-an-integer")))))

(deftest test-db-connector
  (testing "Should return a data-source if a database connection is established"
    (let [data-source :ds]
      (with-redefs [state/system {:db.sql/connection data-source}]
        (is (= data-source (db-connector))))))

  (testing "It throws an exception if there isn't a database connection"
    (with-redefs [state/system {}]
      (is (thrown? Exception (db-connector))))))
