-- place your sql queries here
-- see https://www.hugsql.org/ for documentation

-- :name create-operation! :! :n
-- :doc creates a new operation record
INSERT INTO operations
(type, cost)
VALUES (:type, :cost)

-- :name update-operations! :! :n
-- :doc update an existing operation record
-- :require [sample.app.web.models.utils :refer [expand-where]]
UPDATE operations SET
--~ (expand-set params options)
WHERE
--~ (expand-where params options)
AND deleted IS NOT TRUE

-- :name get-operations :? :*
-- :doc retrieve all operations from table.
-- :require [sample.app.web.models.utils :refer [expand-where]]
SELECT id, type, cost, created_at, updated_at FROM operations
WHERE
--~ (expand-where params options)
AND deleted IS NOT TRUE
ORDER by id

-- :name get-deleted-operations :? :*
-- :doc retrieve all operations from table.
-- :require [sample.app.web.models.utils :refer [expand-where]]
SELECT * FROM operations
WHERE
--~ (expand-where params options)
AND deleted IS TRUE
ORDER by id

-- :name delete-operation! :! :n
-- :doc soft delete operation
UPDATE operations
SET deleted = TRUE
WHERE id = :id
