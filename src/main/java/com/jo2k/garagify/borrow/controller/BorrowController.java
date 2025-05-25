package com.jo2k.garagify.borrow.controller;

import com.jo2k.api.BorrowControllerApi;
import com.jo2k.dto.BorrowGET;
import com.jo2k.dto.BorrowPOST;
import com.jo2k.garagify.borrow.model.Borrow;
import com.jo2k.garagify.borrow.service.IBorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class BorrowController implements BorrowControllerApi {

    private final IBorrowService borrowService;

    @Override
    public ResponseEntity<List<BorrowGET>> getAllBorrows(
            Integer page,
           @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "start_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDate,
            @RequestParam(value = "end_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDate,
            @RequestParam(value = "borrower_id", required = false) String borrowerId
    ) {
        Pageable pageable = Pageable.ofSize(size != null ? size : 10).withPage(page != null ? page : 0);
        var result = borrowService.getAllBorrows(pageable, startDate, endDate, UUID.fromString(borrowerId));
        return ResponseEntity.ok((List<BorrowGET>) result);
    }

    @Override
    public ResponseEntity<BorrowGET> createBorrow(BorrowPOST borrowPOST) {
        // Implementation of the method to handle borrowing a parking spot
        // This is where you would add your business logic
        Borrow created = borrowService.createBorrow(mapToEntity(borrowPOST));
        return ResponseEntity.ok(mapToDTO(created));
    }

    private Borrow mapToEntity(BorrowPOST dto) {
        Borrow b = new Borrow();
        b.setBorrowTime(dto.getStartDate().toLocalDateTime());
        b.setReturnTime(dto.getEndDate() != null ? dto.getEndDate().toLocalDateTime() : null);
        b.setParkingSpotId(dto.getSpotId());
        b.setUserId(dto.getBorrowerId());
        // add other fields if needed
        return b;
    }

    // Mapper from Borrow entity to BorrowGET DTO (your existing one)
    private BorrowGET mapToDTO(Borrow b) {
        BorrowGET dto = new BorrowGET();
        dto.setId(String.valueOf(b.getId()));
        dto.setStartDate(b.getBorrowTime().atOffset(OffsetDateTime.now().getOffset()));
        dto.setEndDate(b.getReturnTime() != null ? b.getReturnTime().atOffset(OffsetDateTime.now().getOffset()) : null);
        dto.setSpotId(String.valueOf(b.getParkingSpotId()));
        dto.setBorrowerId(String.valueOf(b.getUserId()));
        dto.setOwnerId(null); // set if available
        return dto;
    }
}
