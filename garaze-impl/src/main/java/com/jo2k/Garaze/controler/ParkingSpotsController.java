package com.jo2k.Garaze.controler;


import com.jo2k.Garaze.api.ParkingSpotsApiDelegate;
import com.jo2k.Garaze.model.ParkingSpot;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ParkingSpotsController implements ParkingSpotsApiDelegate {
    @Override
    public ResponseEntity<List<ParkingSpot>> getAllParkingSpots() {
        // Implement your logic to retrieve all parking spots here
        List<ParkingSpot> parkingSpots = new ArrayList<>();
        // Populate the list with parking spots
        return ResponseEntity.ok(parkingSpots);
    }

}
