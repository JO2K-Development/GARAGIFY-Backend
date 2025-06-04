package com.jo2k.garagify.parking.api;

import com.jo2k.dto.ParkingDTO;
import com.jo2k.dto.ParkingSpotDTO;
import com.jo2k.dto.UserWithSpotsDTO;
import com.jo2k.dto.UserWithSpotsListDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ParkingService {
    ParkingDTO getParkingById(Integer parkingId);
    List<ParkingSpotDTO> getParkingSpotsByParkingIdNotOwnedByUser(Integer parkingId, UUID userId);
    List<ParkingSpotDTO> getParkingSpotsByParkingIdForCurrentUser(Integer parkingId, UUID userId);
    List<UserWithSpotsDTO> getAllUsersWithSpots(Integer parkingId);
}
