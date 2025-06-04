package com.jo2k.garagify.parking.controller;

import com.jo2k.api.ParkingApi;
import com.jo2k.dto.*;
import com.jo2k.garagify.parking.api.ParkingAvailabilityService;
import com.jo2k.garagify.parking.api.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
public class ParkingController implements ParkingApi {

    private final ParkingService parkingService;
    private final ParkingAvailabilityService availabilityBorrowService;
    private final ParkingAvailabilityService availabilityLendService;

    @Override
    public ResponseEntity<ParkingDTO> getParking(@PathVariable("parking_id") Integer parkingId) {
        ParkingDTO parkingGET = parkingService.getParkingById(parkingId);
        if (parkingGET == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(parkingGET);
    }

    @Override
    public ResponseEntity<List<TimeRangeDto>> getAvailableLendTimeRanges(
            @PathVariable("parking_id") Integer parkingId,
            @RequestParam OffsetDateTime untilWhen) {
        return ResponseEntity.ok(availabilityLendService.getTimeRanges(parkingId, untilWhen));
    }

    @Override
    public ResponseEntity<List<UUID>> getAvailableLendSpots(
            @PathVariable("parking_id") Integer parkingId,
            @RequestParam OffsetDateTime from,
            @RequestParam OffsetDateTime until) {
        return ResponseEntity.ok(availabilityLendService.getSpots(parkingId, from, until));
    }

    @Override
    public ResponseEntity<List<TimeRangeDto>> getAvailableBorrowTimeRanges(
            @PathVariable("parking_id") Integer parkingId,
            @RequestParam OffsetDateTime untilWhen) {
        return ResponseEntity.ok(availabilityBorrowService.getTimeRanges(parkingId, untilWhen));
    }

    @Override
    public ResponseEntity<List<UUID>> getAvailableBorrowSpots(
            @PathVariable("parking_id") Integer parkingId,
            @RequestParam OffsetDateTime from,
            @RequestParam OffsetDateTime until) {
        return ResponseEntity.ok(availabilityBorrowService.getSpots(parkingId, from,until));
    }

    @Override
    public ResponseEntity<UserWithSpotsListDTO> getAllUsersWithSpots(
            @PathVariable("parking_id") Integer parkingId,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {

        List<UserWithSpotsDTO> allUsersWithSpots = parkingService.getAllUsersWithSpots(parkingId);

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
