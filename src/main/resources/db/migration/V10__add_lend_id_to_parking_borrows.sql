ALTER TABLE parking_borrows
    ADD COLUMN lend_id UUID NOT NULL,
ADD CONSTRAINT fk_lend_id FOREIGN KEY (lend_id) REFERENCES parking_lends(id);