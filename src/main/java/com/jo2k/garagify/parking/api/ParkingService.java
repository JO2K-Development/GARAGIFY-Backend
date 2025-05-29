package com.jo2k.garagify.parking.api;

import com.jo2k.dto.ParkingGET;
import com.jo2k.dto.ParkingSpotGET;
import java.util.List;

public interface ParkingService {
    ParkingGET getParkingById(Integer parkingId);
    List<ParkingSpotGET> getParkingSpotsByParkingId(Integer parkingId);
}
