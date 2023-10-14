(ns sample.app.web.models.utils-test
  (:require
    [clojure.test :refer :all]
    [integrant.repl.state :as state]
    [sample.app.web.models.utils :refer :all]))

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
  (testing "It returns a query function if connection is established"
    (let [query-function :query-fn]
      (with-redefs [state/system {:db.sql/query-fn query-function}]
        (is (= query-function (db-connector))))))

  (testing "It throws an exception if no database connection"
    (with-redefs [state/system {}]
      (is (thrown? Exception (db-connector))))))

(deftest test-expand-where
  (testing "Returns ' id IS NOT NULL ' if :where value is :all"
    (is (= " id IS NOT NULL " (expand-where {:where :all} {})))))

(deftest test-transpile-query
  (testing "It transpile values of keyword"
    (let [params {:set {:field1 "value1" :field2 "value2"}
                  :where {:field3 "value3" :field4 "value4"}}
          options {:param1 "param1" :param2 "param2"}]
      (testing ":set to be used in a hugsql command"
        (is (= "field1 = :v:set.field1, field2 = :v:set.field2"
              (transpile-query :set ", " params options))))

      (testing ":where to be used in a hugsql command"
        (is (= "field3 = :v:where.field3 AND field4 = :v:where.field4"
              (transpile-query :where " AND " params options)))))))
