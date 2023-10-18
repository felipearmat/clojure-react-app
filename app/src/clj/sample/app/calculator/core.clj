(ns sample.app.calculator.core
  (:require
    [clj-http.client :as client]
    [clojure.spec.alpha :as spec]
    [clojure.string :as str]
    [clojure.tools.logging :as log]
    [infix.macros :as inf]
    [sample.app.web.models.operations :as operations]
    [sample.app.web.models.records :as records]
    [sample.app.web.models.utils :as utils]))

;; Define the expression spec for validation
(spec/def :calculator/expression
  (spec/and string? #(re-matches #"(?:[0-9√()+\-*\/.]+|randomstr)" %)))

;; Define the URL for fetching random strings
(def random-org-url
  "https://www.random.org/strings/?num=1&len=32&digits=on&upperalpha=on&loweralpha=on&unique=on&format=plain&rnd=new")

(defn char-occur
  "Count the occurrences of a specific character in a string."
  [str char]
  (get (frequencies str) char 0))

(defn gen-random-string
  "Generate a random string using an external API."
  []
  (str/replace (:body (client/get random-org-url)) #"\n" ""))

(defn count-operators
  "Count occurrences of mathematical operators in an expression."
  [expression]
  (let [char-occur-fn (partial char-occur expression)]
    {:addition       (char-occur-fn \+)
     :subtraction    (char-occur-fn \-)
     :multiplication (char-occur-fn \*)
     :division       (char-occur-fn \/)
     :square_root    (char-occur-fn \√)}))

(defn record-operation
  "Create a record for a specific operation."
  [user-id operation]
  (records/create-record! {:operation_id (:id operation)
                           :user_id      user-id
                           :amount       (:cost operation)}))

(defn eval-expression
  "Evaluate an infixed mathematical expression in string format."
  [expression]
  ((inf/from-string [] (str/replace expression #"√" "sqrt"))))

(defmulti calc-expression
  "Handle general mathematical expressions by evaluating the expression, recording
  operations based on the operator counts, and returning the evaluation result."
  (fn [_ expression] expression))

(defmethod calc-expression "randomstr" [user-id _]
  (let [random-string-op (first (operations/get-operations {:type "random_string"}))
        random-string (gen-random-string)]
    (record-operation user-id random-string-op)
    random-string))

(defmethod calc-expression :default [user-id expression]
  (let [ops (operations/get-operations)
        times-map (count-operators expression)
        result (eval-expression expression)]
    (doseq [op ops]
      (when-let [times ((keyword (:type op)) times-map)]
        (dotimes [n times]
          (record-operation user-id op))))
    result))

(defn expand-expression-operators
  "Expand '√' to '*√' when preceded by a digit or ')' to ensure correct evaluation."
  [expression]
  (str/replace expression #"(?<=\d|\))√" "*√"))

(defn calculate!
  "Validate, calculate, and charge for a mathematical expression."
  [user-id expression]
  (try
    (->> expression
      (utils/validate-spec :calculator/expression)
      (expand-expression-operators)
      (calc-expression user-id))
  (catch Exception e
    (log/info e)
    {:msg "Invalid Expression." :error e})))
