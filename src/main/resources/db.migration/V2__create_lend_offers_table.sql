CREATE TABLE lend_offers (
                             id UUID PRIMARY KEY,
                             start_date TIMESTAMP WITH TIME ZONE NOT NULL,
                             end_date TIMESTAMP WITH TIME ZONE NOT NULL,
                             parking_spot_id UUID NOT NULL,
                             owner_id UUID NOT NULL,
                             created_at TIMESTAMP WITH TIME ZONE
);
