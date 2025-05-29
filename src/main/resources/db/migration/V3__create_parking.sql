CREATE TABLE organisation
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE parking
(
    id              SERIAL PRIMARY KEY,
    organisation_id INTEGER      NOT NULL REFERENCES organisation (id) ON DELETE CASCADE,
    name            VARCHAR(255) NOT NULL,
    ui_object       JSONB        NOT NULL
);

CREATE TABLE parking_spot
(
    id         SERIAL PRIMARY KEY,
    parking_id INTEGER NOT NULL REFERENCES parking (id) ON DELETE CASCADE,
    spot_uuid  UUID    NOT NULL,
    owner_id   UUID REFERENCES users (id),
    status     VARCHAR(50),
    UNIQUE (parking_id, spot_uuid)
);
