CREATE TABLE parking_borrows
(
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    borrow_time     TIMESTAMP WITH TIME ZONE NOT NULL,
    return_time     TIMESTAMP WITH TIME ZONE,
    user_id         UUID      NOT NULL,
    parking_spot_id INTEGER NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE        DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parking_spot_id) REFERENCES parking_spot(id)
);
DROP TABLE borrows;