ALTER TABLE parking_borrows
DROP CONSTRAINT IF EXISTS fk_lend_id;

ALTER TABLE parking_borrows
DROP COLUMN IF EXISTS lend_id;

ALTER TABLE parking_borrows
    ADD COLUMN parking_lend_offer_id UUID NOT NULL,
    ADD CONSTRAINT fk_parking_lend_offer_id FOREIGN KEY (parking_lend_offer_id) REFERENCES parking_lends(id);