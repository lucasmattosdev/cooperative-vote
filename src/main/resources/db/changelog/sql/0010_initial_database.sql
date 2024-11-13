CREATE TABLE associate (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    document VARCHAR(14) NOT NULL
);

CREATE TABLE agenda (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid()
);

CREATE TABLE agenda_session (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    duration_in_minutes BIGINT NOT NULL,
    start_at timestamp without time zone NOT NULL,
    end_at timestamp without time zone NOT NULL,
    agenda_id uuid NOT NULL,
    FOREIGN KEY (agenda_id) REFERENCES agenda(id)
);

CREATE TABLE agenda_vote (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    value VARCHAR(8) NOT NULL,
    voted_at timestamp without time zone NOT NULL,
    agenda_id uuid NOT NULL,
    FOREIGN KEY (agenda_id) REFERENCES agenda(id),
    associate_id uuid NOT NULL,
    FOREIGN KEY (associate_id) REFERENCES associate(id),
    CONSTRAINT agenda_vote_agenda_associate_unique UNIQUE (agenda_id,associate_id)
);
