package com.jo2k.garagify.parking.controller;

import com.jo2k.api.ParkingControllerApi;
import com.jo2k.dto.ParkingGET;
import com.jo2k.dto.ParkingSpotGET;
import com.jo2k.garagify.parking.api.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ParkingController implements ParkingControllerApi {

    private final ParkingService parkingService;

    @Override
    public ResponseEntity<ParkingGET> getParking(@PathVariable("parking_id") Integer parkingId) {
        ParkingGET parkingGET = parkingService.getParkingById(parkingId);
        if (parkingGET == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(parkingGET);
    }

    @Override
    public ResponseEntity<List<ParkingSpotGET>> getParkingSpots(@PathVariable("parking_id") Integer parkingId) {
        return ResponseEntity.ok(parkingService.getParkingSpotsByParkingId(parkingId));
    }
}
