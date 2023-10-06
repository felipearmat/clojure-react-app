-- place your sql queries here
-- see https://www.hugsql.org/ for documentation

-- :name create-operation! :! :n
-- :doc creates a new operation record
INSERT INTO operations
(type, cost)
VALUES (:type, :cost)

-- :name update-operation! :! :n
-- :doc update an existing operation record
UPDATE operations
SET type = :type, cost = :cost
WHERE id = :id AND deleted IS NOT TRUE

-- :name get-operations :? :*
-- :doc retrieve all operations from table.
SELECT id, type, cost, created_at, updated_at FROM operations
WHERE deleted IS NOT TRUE
ORDER by id

-- :name get-operation :? :1
-- :doc retrieve an active operation given the id.
SELECT id, type, cost, created_at, updated_at FROM operations
WHERE id = :id AND deleted IS NOT TRUE

-- :name delete-operation! :! :n
-- :doc soft delete operation
UPDATE operations
SET deleted = TRUE
WHERE id = :id
