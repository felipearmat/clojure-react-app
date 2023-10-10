CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
--;;
CREATE TABLE users (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  email domain_email NOT NULL,
  password TEXT NOT NULL,
  status VARCHAR(8) NOT NULL DEFAULT 'active',
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
--;;
CREATE INDEX users_email_ix on users(email)
  WHERE deleted IS NOT TRUE;
--;;
CREATE INDEX users_id_ix on users(id)
  WHERE deleted IS NOT TRUE;
--;;
CREATE INDEX users_deleted_ix on users(deleted);
--;;
CREATE UNIQUE INDEX user_email_unique_constraint ON users(email)
  WHERE deleted IS NOT TRUE;
--;;
ALTER TABLE users
   ADD CONSTRAINT check_status
   CHECK (status IN ('active', 'inactive'));
--;;
CREATE TRIGGER users_auto_updated_at
  BEFORE UPDATE ON users
  FOR EACH ROW
  EXECUTE PROCEDURE auto_updated_at();
