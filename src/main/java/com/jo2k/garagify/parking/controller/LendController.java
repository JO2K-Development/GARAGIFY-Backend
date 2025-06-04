package com.jo2k.garagify.parking.controller;

import com.jo2k.api.LendApi;
import com.jo2k.dto.*;
import com.jo2k.garagify.parking.api.ParkingActionService;
import com.jo2k.garagify.parking.api.ParkingInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class LendController implements LendApi {

    private final ParkingActionService<LendOfferDTO> parkingLendService;
    private final ParkingInfoService<LendOfferInfoDTO> parkingLendOfferInfoService;


    @Override
    public ResponseEntity<LendOfferDTO> createLendForSpot(
            @PathVariable("parking_id") Integer parkingId,
            @PathVariable("spot_id") UUID spotId,
            @RequestBody TimeRangeRequest body) {
        return ResponseEntity.ok(parkingLendService.create(parkingId, spotId, body));
    }

    @Override
    public ResponseEntity<Void> deleteParkingLend(@PathVariable("id") UUID id) {
        parkingLendService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<LendOfferInfoListDTO> getMyLends(
            @Valid @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @Valid @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
            @Valid @RequestParam(value = "sort", required = false) String sort
    ) {
        Sort sortOrder = (sort != null && !sort.isBlank())
                ? Sort.by(sort).ascending()
                : Sort.by("startDate").ascending();

        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Page<LendOfferInfoDTO> result = parkingLendOfferInfoService.getForCurrentUser(pageable);

        LendOfferInfoListDTO dto = toDto(result);

        return ResponseEntity.ok(dto);
    }

    private static LendOfferInfoListDTO toDto(Page<LendOfferInfoDTO> page) {
        LendOfferInfoListDTO dto = new LendOfferInfoListDTO();
        dto.setContent(page.getContent());
        dto.setTotalElements((long) page.getTotalElements()); // or cast to long if needed
        dto.setTotalPages(page.getTotalPages());
        dto.setPage(page.getNumber());
        dto.setSize(page.getSize());
        return dto;
    }
}
