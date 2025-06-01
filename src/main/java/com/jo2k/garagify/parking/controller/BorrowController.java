package com.jo2k.garagify.parking.controller;

import com.jo2k.api.BorrowControllerApi;
import com.jo2k.dto.BorrowDTO;
import com.jo2k.dto.BorrowListDTO;
import com.jo2k.garagify.parking.api.ParkingBorrowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BorrowController implements BorrowControllerApi {

    private final ParkingBorrowService parkingBorrowService;

    @Override
    public ResponseEntity<Void> deleteParkingBorrow(@PathVariable("id") UUID id) {
        parkingBorrowService.deleteBorrowById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<BorrowListDTO> getMyBorrows(
            @Valid @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @Valid @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
            @Valid @RequestParam(value = "sort", required = false) String sort
    ) {
        Sort sortOrder = (sort != null && !sort.isBlank())
                ? Sort.by(sort).ascending()
                : Sort.by("borrowTime").ascending();

        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Page<BorrowDTO> result = parkingBorrowService.getBorrowsForCurrentUser(pageable);

        BorrowListDTO dto = toDto(result);

        return ResponseEntity.ok(dto);
    }

    private static BorrowListDTO toDto(Page<BorrowDTO> page) {
        BorrowListDTO dto = new BorrowListDTO();
        dto.setContent(page.getContent());
        dto.setTotalElements((long) page.getTotalElements()); // or cast to long if needed
        dto.setTotalPages(page.getTotalPages());
        dto.setPage(page.getNumber());
        dto.setSize(page.getSize());
        return dto;
    }
}
