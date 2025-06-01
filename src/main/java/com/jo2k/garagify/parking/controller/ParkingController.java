package com.jo2k.garagify.parking.controller;

import com.jo2k.api.ParkingApi;
import com.jo2k.dto.*;
import com.jo2k.garagify.parking.api.ParkingAvailability;
import com.jo2k.garagify.parking.api.ParkingBorrowService;
import com.jo2k.garagify.parking.api.ParkingLendService;
import com.jo2k.garagify.parking.api.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
public class ParkingController implements ParkingApi {

    private final ParkingService parkingService;
    private final ParkingAvailability availabilityBorrowService;
    private final ParkingAvailability availabilityLendService;
    private final ParkingBorrowService parkingBorrowService;
    private final ParkingLendService parkingLendService;

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
    public ResponseEntity<BorrowDTO> createBorrowForSpot(
            @PathVariable("parking_id") Integer parkingId,
            @PathVariable("spot_id") UUID spotId,
            @RequestBody TimeRangeRequest body) {
        return ResponseEntity.ok(parkingBorrowService.createBorrowForSpot(parkingId, spotId, body));
    }

    @Override
    public ResponseEntity<LendOfferDTO> createLendForSpot(
            @PathVariable("parking_id") Integer parkingId,
            @PathVariable("spot_id") UUID spotId,
            @RequestBody TimeRangeRequest body) {
        return ResponseEntity.ok(parkingLendService.createLendOfferForSpot(parkingId, spotId, body));
    }
}
