-- Place your SQL queries here
-- See https://www.hugsql.org/ for documentation

-- :name create-record! :! :n
-- :doc Create a new record.
-- :require [sample.app.web.models.utils :refer [expand-where]]
INSERT INTO records (operation_id, user_id, amount, user_balance, operation_response)
VALUES (:operation_id, :user_id, :amount, :user_balance, :operation_response)

-- :name get-records :? :*
-- :doc Retrieves non-deleted records.
-- :require [sample.app.web.models.utils :refer [expand-where]]
SELECT id, operation_id, user_id, amount, user_balance, operation_response, created_at, updated_at
FROM records
WHERE
--~ (expand-where params options)
AND deleted IS NOT TRUE
ORDER BY id

-- :name get-deleted-records :? :*
-- :doc Retrieves deleted records.
-- :require [sample.app.web.models.utils :refer [expand-where]]
SELECT *
FROM records
WHERE
--~ (expand-where params options)
AND deleted IS TRUE
ORDER BY id

-- :name delete-record! :! :n
-- :doc Soft delete a record.
UPDATE records
SET deleted = TRUE
WHERE id = :id
