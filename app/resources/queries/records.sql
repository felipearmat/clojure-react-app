-- place your sql queries here
-- see https://www.hugsql.org/ for documentation

-- :name create-record! :! :n
-- :doc creates a new record record
INSERT INTO records
(operation_id, user_id, amount, operation_response)
VALUES (:operation_id, :user_id, :amount, :operation_response)

-- :name get-records :? :*
-- :doc retrieve all records from table.
SELECT id, operation_id, user_id, amount, user_balance, operation_response, created_at, updated_at FROM records
WHERE deleted IS NOT TRUE
ORDER by id

-- :name get-record :? :1
-- :doc retrieve an active record given the id.
SELECT id, operation_id, user_id, amount, user_balance, operation_response, created_at, updated_at FROM records
WHERE id = :id AND deleted IS NOT TRUE

-- :name delete-record! :! :n
-- :doc soft delete record
UPDATE records
SET deleted = TRUE
WHERE id = :id
