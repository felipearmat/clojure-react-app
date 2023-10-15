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
SELECT
  operations.cost AS operation_cost,
  operations.id AS operation_id,
  operations.type AS operation_type,
  records.amount AS amount,
  records.created_at AS created_at,
  records.id AS id,
  records.operation_response AS operation_response,
  records.updated_at AS updated_at,
  records.user_balance AS user_balance,
  users.email AS user_email,
  users.id AS user_id,
  users.status AS user_status
FROM records
INNER JOIN users ON records.user_id = users.id
INNER JOIN operations ON records.operation_id = operations.id
WHERE
--~ (expand-where params options)
AND records.deleted IS NOT TRUE
ORDER BY records.id

-- :name get-deleted-records :? :*
-- :doc Retrieves deleted records.
-- :require [sample.app.web.models.utils :refer [expand-where]]
SELECT
  operations.cost AS operation_cost,
  operations.deleted AS operation_deleted,
  operations.id AS operation_id,
  operations.type AS operation_type,
  records.amount AS amount,
  records.created_at AS created_at,
  records.deleted AS deleted,
  records.id AS id,
  records.operation_response AS operation_response,
  records.updated_at AS updated_at,
  records.user_balance AS user_balance,
  users.deleted AS user_deleted,
  users.email AS user_email,
  users.id AS user_id,
  users.status AS user_status
FROM records
INNER JOIN users ON records.user_id = users.id
INNER JOIN operations ON records.operation_id = operations.id
WHERE
--~ (expand-where params options)
AND records.deleted = TRUE
ORDER BY records.id;

-- :name delete-record! :! :n
-- :doc Soft delete a record.
UPDATE records
SET deleted = TRUE
WHERE id = :id
