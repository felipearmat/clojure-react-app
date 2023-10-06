CREATE TABLE records (
  id SERIAL PRIMARY KEY,
  operation_id INTEGER,
  user_id INTEGER,
  amount float NOT NULL,
  user_balance float NOT NULL,
  operation_response VARCHAR(255),
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_operation
    FOREIGN KEY(operation_id)
	    REFERENCES operations(id),
  CONSTRAINT fk_user
    FOREIGN KEY(user_id)
	    REFERENCES users(id)
);
--;;
CREATE INDEX records_user_id_deleted on records(user_id, deleted);
--;;
CREATE INDEX records_operation_id_deleted on records(operation_id, deleted);
--;;
CREATE INDEX records_created_at_deleted on records(created_at, deleted);
--;;
CREATE TRIGGER records_auto_updated_at BEFORE UPDATE ON records FOR EACH ROW EXECUTE PROCEDURE auto_updated_at();
