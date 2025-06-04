package com.jo2k.garagify.parking.controller;

import com.jo2k.api.AdminApi;
import com.jo2k.dto.AssignParkingSpot200Response;
import com.jo2k.dto.AssignParkingSpotRequest;
import com.jo2k.garagify.parking.api.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AdminController implements AdminApi {

    private final AdminService adminService;

    @Override
    public ResponseEntity<AssignParkingSpot200Response> assignParkingSpot(
            Integer parkingId,
            UUID spotId,
            AssignParkingSpotRequest assignParkingSpotRequest
    ) {
        adminService.assignSpotToUser(parkingId, spotId, assignParkingSpotRequest.getUserId());
        AssignParkingSpot200Response response = new AssignParkingSpot200Response();
        response.setMessage("Parking spot assigned successfully");
        return ResponseEntity.ok(response);
    }
}