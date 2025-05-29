package com.jo2k.garagify.parking.api;

import com.jo2k.dto.ParkingDTO;
import com.jo2k.dto.ParkingSpotDTO;
import java.util.List;

public interface ParkingService {
    ParkingDTO getParkingById(Integer parkingId);
    List<ParkingSpotDTO> getParkingSpotsByParkingId(Integer parkingId);
}
