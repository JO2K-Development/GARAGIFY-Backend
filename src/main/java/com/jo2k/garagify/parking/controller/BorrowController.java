package com.jo2k.garagify.parking.controller;

import com.jo2k.api.BorrowApi;
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
public class BorrowController implements BorrowApi {

    private final ParkingActionService<BorrowDTO> parkingBorrowService;
    private final ParkingInfoService<BorrowInfoDTO> parkingBorrowInfoService;



    @Override
    public ResponseEntity<BorrowDTO> createBorrowForSpot(
            @PathVariable("parking_id") Integer parkingId,
            @PathVariable("spot_id") UUID spotId,
            @RequestBody TimeRangeRequest body) {
        return ResponseEntity.ok(parkingBorrowService.create(parkingId, spotId, body));
    }

    @Override
    public ResponseEntity<Void> deleteParkingBorrow(@PathVariable("id") UUID id) {
        parkingBorrowService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<BorrowInfoListDTO> getMyBorrows(
            @Valid @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @Valid @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
            @Valid @RequestParam(value = "sort", required = false) String sort
    ) {
        Sort sortOrder = (sort != null && !sort.isBlank())
                ? Sort.by(sort).ascending()
                : Sort.by("borrowTime").ascending();

        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Page<BorrowInfoDTO> result = parkingBorrowInfoService.getForCurrentUser(pageable);

        BorrowInfoListDTO dto = toDto(result);

        return ResponseEntity.ok(dto);
    }

    private static BorrowInfoListDTO toDto(Page<BorrowInfoDTO> page) {
        BorrowInfoListDTO dto = new BorrowInfoListDTO();
        dto.setContent(page.getContent());
        dto.setTotalElements((long) page.getTotalElements()); // or cast to long if needed
        dto.setTotalPages(page.getTotalPages());
        dto.setPage(page.getNumber());
        dto.setSize(page.getSize());
        return dto;
    }
}
