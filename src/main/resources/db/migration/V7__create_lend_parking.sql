CREATE TABLE parking_lends
(
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    start_date      TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date        TIMESTAMP WITH TIME ZONE NOT NULL,
    parking_spot_id INTEGER                 NOT NULL,
    owner_id        UUID                    NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parking_spot_id) REFERENCES parking_spot(id)
);