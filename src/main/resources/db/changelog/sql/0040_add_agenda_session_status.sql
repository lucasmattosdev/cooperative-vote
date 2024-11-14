ALTER TABLE agenda_session ADD status VARCHAR(16) NOT NULL default 'OPEN';
ALTER TABLE agenda_session ALTER COLUMN status DROP DEFAULT;
CREATE INDEX agenda_session_status_idx ON agenda_session (status);

ALTER TABLE agenda_session ADD version BIGINT NULL default 0;
