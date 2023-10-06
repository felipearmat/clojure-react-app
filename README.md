# clojure-react-app

A simple project for a back-end clojure and front-end react app

## Query functions examples

```clojure
;; Some user functions
(query-fn :create-user! {:email "addition@test.com" :password "banana"})
(query-fn :get-users {})

;; Some operation functions
(query-fn :create-operation! {:type "addition" :cost 2.00})
(query-fn :get-operations {})
```
