ALTER TABLE agenda ADD last_agenda_session_id uuid NULL;
ALTER TABLE agenda ADD FOREIGN KEY (last_agenda_session_id) REFERENCES agenda_session(id);
