package com.jo2k.garagify.parking.controller;

import com.jo2k.api.AdminApi;
import com.jo2k.dto.AssignParkingSpot200Response;
import com.jo2k.dto.AssignParkingSpotFormDTO;
import com.jo2k.dto.UserWithSpotsDTO;
import com.jo2k.dto.UserWithSpotsListDTO;
import com.jo2k.garagify.parking.api.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AdminController implements AdminApi {

    private final AdminService adminService;

    @Override
    public ResponseEntity<AssignParkingSpot200Response> assignParkingSpot(
            Integer parkingId,
            UUID spotId,
            AssignParkingSpotFormDTO assignParkingSpotRequest
    ) {
        UUID userId = assignParkingSpotRequest.getUserId() != null
                ? assignParkingSpotRequest.getUserId().orElse(null)
                : null;
        adminService.assignSpotToUser(parkingId, spotId, userId);
        AssignParkingSpot200Response response = new AssignParkingSpot200Response();
        response.setMessage("Parking spot assigned successfully");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserWithSpotsListDTO> getAllUsersWithSpots(
            @PathVariable("parking_id") Integer parkingId,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {

        List<UserWithSpotsDTO> allUsersWithSpots = adminService.getAllUsersWithSpots();

        int totalElements = allUsersWithSpots.size();
        int fromIndex = Math.min(page * size, totalElements);
        int toIndex = Math.min(fromIndex + size, totalElements);
        List<UserWithSpotsDTO> pagedUsers = allUsersWithSpots.subList(fromIndex, toIndex);

        UserWithSpotsListDTO dto = new UserWithSpotsListDTO()
                .content(pagedUsers)
                .totalElements((long) totalElements)
                .totalPages((int) Math.ceil((double) totalElements / size))
                .page(page)
                .size(size);

        return ResponseEntity.ok(dto);
    }

}