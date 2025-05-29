CREATE TABLE borrows
(
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    borrow_time     TIMESTAMP WITH TIME ZONE NOT NULL,
    return_time     TIMESTAMP WITH TIME ZONE,
    parking_spot_id UUID      NOT NULL,
    user_id         UUID      NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE        DEFAULT CURRENT_TIMESTAMP
);
