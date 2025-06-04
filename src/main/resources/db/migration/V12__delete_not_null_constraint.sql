ALTER TABLE parking_borrows
DROP CONSTRAINT IF EXISTS fk_parking_lend_offer_id;

ALTER TABLE parking_borrows
DROP COLUMN IF EXISTS parking_lend_offer_id;

ALTER TABLE parking_borrows
    ADD COLUMN parking_lend_offer_id UUID,
    ADD CONSTRAINT fk_parking_lend_offer_id FOREIGN KEY (parking_lend_offer_id) REFERENCES parking_lends(id);