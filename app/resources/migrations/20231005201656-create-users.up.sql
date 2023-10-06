CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  email domain_email NOT NULL UNIQUE,
  password TEXT NOT NULL,
  status VARCHAR(255) NOT NULL DEFAULT 'active',
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
--;;
CREATE INDEX users_email_deleted on users(email, deleted);
--;;
CREATE INDEX users_id_deleted on users(id, deleted);
--;;
CREATE INDEX users_deleted on users(deleted);
--;;
ALTER TABLE users
   ADD CONSTRAINT check_status
   CHECK (status IN ('active', 'inactive'));
--;;
CREATE TRIGGER users_auto_updated_at BEFORE UPDATE ON users FOR EACH ROW EXECUTE PROCEDURE auto_updated_at();
