(ns sample.app.web.services.calculator-test
  (:require
    [clojure.test :refer :all]
    [sample.app.web.models.records :as records]
    [sample.app.web.models.operations :as operations]
    [sample.app.web.models.users :as users]
    [sample.app.web.services.calculator :as calc]
    [sample.app.web.models.utils :refer [map-in?]]
    [sample.app.test-utils :as test-utils]))

(def test-user (atom {}))
(def test-ops (atom []))
(def user-email "test@mail.com")

(def operations-data [
  {:cost 1.00 :type "addition"}
  {:cost 1.50 :type "subtraction"}
  {:cost 2.00 :type "multiplication"}
  {:cost 2.50 :type "division"}
  {:cost 3.00 :type "square_root"}
  {:cost 3.50 :type "random_string"}
])

(defn calculator-fixtures [f]
  (users/create-user! user-email "Password@1")
  (doseq [operation operations-data]
    (operations/create-operation! (:type operation) (:cost operation)))
  (reset! test-user (last (users/get-users)))
  (reset! test-ops (operations/get-operations))
  (f))

(use-fixtures :each test-utils/database-rollback calculator-fixtures)

(deftest test-char-occur
  (testing "Should count character correctly"
    (let [expression "(1+2)*√(2)*3.1-3*√(4)/5/2"]
      (is (= 1 (calc/char-occur expression \+)))
      (is (= 1 (calc/char-occur expression \-)))
      (is (= 3 (calc/char-occur expression \*)))
      (is (= 2 (calc/char-occur expression \/)))
      (is (= 2 (calc/char-occur expression \√))))))

(deftest test-count-operators
  (testing "Should count operators correctly"
    (is (= {:addition 1,
            :subtraction 1,
            :multiplication 1,
            :division 1,
            :square_root 1}
            (calc/count-operators "(1 + 2) * 3 - √(4) / 5")))
    (is (= {:addition 1,
            :subtraction 1,
            :multiplication 1,
            :division 1,
            :square_root 0}
            (calc/count-operators "(1+2)*3-4/5")))
    (is (= {:addition 1,
            :subtraction 1,
            :multiplication 3,
            :division 2,
            :square_root 2}
            (calc/count-operators "(1+2)*√(2)*3.1-3*√(4)/5/2")))))

(deftest test-record-operation
  (testing "Should create a record based on parameters"
    (let [op (first @test-ops)
          user-id (:id @test-user)
          record {:operation_id (:id op)
                  :user_id user-id
                  :amount (:cost op)}]
      (calc/record-operation user-id op)
      (is (map-in? record
            (last (records/get-records [:= :records.user_id user-id])))))))

(deftest test-eval-expression
  (testing "Should evalute expected operators correctly"
    (is (= 42 (calc/eval-expression "42")))
    (is (= 42 (calc/eval-expression "(42)")))
    (is (= 42 (calc/eval-expression "21+21")))
    (is (= 42 (calc/eval-expression "45-3")))
    (is (= 42 (calc/eval-expression "2*21")))
    (is (= 84/2 (calc/eval-expression "(80+4)/2")))
    (is (= 42 (calc/eval-expression "((80+4)/2)")))
    (is (= 42.0 (calc/eval-expression "√(1764)")))))

(deftest test-calc-expression-randomstr
  (testing "calc-expression for 'randomstr'"
    (let [user-id (:id @test-user)
          op (last (filter #(= "random_string" (:type %)) @test-ops))
          expected-record {:operation_id (:id op) :user_id user-id :amount (:cost op)}]
      (with-redefs [calc/gen-random-string (fn [] "gen-random-string")]
        (is (= "gen-random-string" (calc/calc-expression user-id "randomstr")))
        (is (map-in? expected-record (last (records/get-records [:= :records.user_id user-id]))))))))

(deftest test-calc-expression-mathematical
  (testing "calc-expression for mathematical expressions"
    (let [user-id (:id @test-user)
          expression "(1+2)*√(4)*3.1-3*√(9)/5"
          result (calc/calc-expression user-id expression)
          records (records/get-records [:= :records.user_id user-id])]
      (is (= 16.8 result))
      (is (= 1 (count (filter #(= "addition" (:operation_type %)) records))))
      (is (= 1 (count (filter #(= "subtraction" (:operation_type %)) records))))
      (is (= 3 (count (filter #(= "multiplication" (:operation_type %)) records))))
      (is (= 2 (count (filter #(= "square_root" (:operation_type %)) records))))
      (is (= 1 (count (filter #(= "division" (:operation_type %)) records)))))))

(deftest test-expand-expression-operators
  (testing "expand-expression-operators function"
    (is (= "2*√(2)" (calc/expand-expression-operators "2√(2)")))
    (is (= "(1+2)*√(2)" (calc/expand-expression-operators "(1+2)√(2)")))
    (is (= "(1+2)*√(2)*3.1-3*√(4)/5/2" (calc/expand-expression-operators "(1+2)√(2)*3.1-3√(4)/5/2")))))

(deftest test-calculate
  (testing "calculate! for valid expressions"
    (let [user-id (:id @test-user)
          expression "(1+2)*√(4)*3.1-3*√(9)/5"
          result (calc/calc-expression user-id expression)
          records (records/get-records [:= :records.user_id user-id])]
      (is (= result (calc/calculate! user-id expression)))))

  (testing "calculate! for 'randomstr'"
    (let [user-id (:id @test-user)
          expected-record {:operation_id 6 :user_id 1 :amount 3.5}]
      (with-redefs [calc/gen-random-string (fn [] "gen-random-string")]
        (is (= "gen-random-string" (calc/calculate! user-id "randomstr"))))))

  (testing "calculate! for invalid expressions"
    (is (= "Invalid Expression." (:msg (calc/calculate! 1 "abc"))))))
