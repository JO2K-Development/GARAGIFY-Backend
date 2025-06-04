package com.jo2k.garagify.parking.api;

import java.util.UUID;

public interface AdminService {
    void assignSpotToUser(Integer parkingId, UUID spotId, UUID userId);

}
