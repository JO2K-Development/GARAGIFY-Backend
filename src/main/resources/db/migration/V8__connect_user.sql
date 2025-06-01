ALTER TABLE parking_lends
    ADD CONSTRAINT fk_parking_lends_owner
        FOREIGN KEY (owner_id) REFERENCES users (id);

ALTER TABLE parking_borrows
    ADD CONSTRAINT fk_parking_borrows_user
        FOREIGN KEY (user_id) REFERENCES users (id);
