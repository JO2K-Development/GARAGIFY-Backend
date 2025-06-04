package com.jo2k.garagify.parking.api;

import com.jo2k.dto.UserWithSpotsDTO;

import java.util.List;
import java.util.UUID;

public interface AdminService {
    void assignSpotToUser(Integer parkingId, UUID spotId, UUID userId);

    List<UserWithSpotsDTO> getAllUsersWithSpots();
}
