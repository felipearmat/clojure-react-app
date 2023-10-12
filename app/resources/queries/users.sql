-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO users
(email, password)
VALUES (:email, :password)

-- :name update-user! :! :n
-- :doc update an existing user record
-- :require [sample.app.web.models.utils :refer [expand-set expand-where]]
UPDATE users SET
--~ (expand-set params options)
WHERE
--~ (expand-where params options)
AND deleted IS NOT TRUE

-- :name get-users :? :*
-- :doc retrieve users from table.
-- :require [sample.app.web.models.utils :refer [expand-where]]
SELECT email, status, password, created_at, updated_at FROM users
WHERE
--~ (expand-where params options)
AND deleted IS NOT TRUE
ORDER by id

-- :name get-deleted-users :? :*
-- :doc retrieve deleted users from table.
-- :require [sample.app.web.models.utils :refer [expand-where]]
SELECT email, status, password, created_at, updated_at FROM users
WHERE
--~ (expand-where params options)
AND deleted IS TRUE
ORDER by id

-- :name delete-user! :! :n
-- :doc soft delete user
UPDATE users
SET deleted = TRUE
WHERE email = :email
