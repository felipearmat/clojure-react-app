-- place your sql queries here
-- see https://www.hugsql.org/ for documentation

-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO users
(email, password)
VALUES (:email, :password)

-- :name update-user! :! :n
-- :doc update an existing user record
UPDATE users
SET email = :email, password = :password
WHERE id = :id AND deleted IS NOT TRUE

-- :name get-users :? :*
-- :doc retrieve all users from table.
SELECT email, status, created_at, updated_at FROM users
WHERE deleted IS NOT TRUE
ORDER by id

-- :name get-user :? :1
-- :doc retrieve an active user given the id.
SELECT email, status, created_at, updated_at FROM users
WHERE id = :id AND deleted IS NOT TRUE

-- :name deactivate-user! :! :n
-- :doc change user status to inactive
UPDATE users
SET status = 'inactive'
WHERE id = :id AND deleted IS NOT TRUE

-- :name delete-user! :! :n
-- :doc soft delete user
UPDATE users
SET deleted = TRUE
WHERE id = :id
