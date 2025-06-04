package com.jo2k.garagify.parking.service;

import com.jo2k.garagify.parking.api.AdminService;
import com.jo2k.garagify.parking.persistence.model.ParkingSpot;
import com.jo2k.garagify.parking.persistence.repository.ParkingSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("adminService")
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final ParkingSpotRepository parkingSpotRepository;

    @Override
    public void assignSpotToUser(Integer parkingId, UUID spotId, UUID userId) {
        ParkingSpot spot = parkingSpotRepository.findByParking_IdAndSpotUuid(parkingId, spotId)
                .orElseThrow(() -> new IllegalArgumentException("Parking spot not found"));
        spot.setOwnerId(userId);
        parkingSpotRepository.save(spot);
    }
}