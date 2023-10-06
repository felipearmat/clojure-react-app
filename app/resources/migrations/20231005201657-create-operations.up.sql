CREATE TABLE operations (
  id SERIAL PRIMARY KEY,
  type VARCHAR(255) NOT NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
--;;
CREATE INDEX operations_type_deleted on operations(type, deleted);
--;;
CREATE INDEX operations_id_deleted on operations(id, deleted);
--;;
CREATE INDEX operations_deleted on operations(deleted);
--;;
ALTER TABLE operations
   ADD CONSTRAINT check_type
   CHECK (type IN ('addition','subtraction','multiplication','division','square_root','random_string'));
--;;
CREATE TRIGGER operations_auto_updated_at BEFORE UPDATE ON operations FOR EACH ROW EXECUTE PROCEDURE auto_updated_at();
