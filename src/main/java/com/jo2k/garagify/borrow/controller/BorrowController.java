package com.jo2k.garagify.borrow.controller;

import com.jo2k.api.BorrowControllerApi;
import com.jo2k.dto.*;
import com.jo2k.garagify.borrow.api.BorrowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Validated
@RequiredArgsConstructor
@RestController
public class BorrowController implements BorrowControllerApi {

    private final BorrowService borrowService;

    @Override
    public ResponseEntity<List<BorrowDTO>> createBorrow(@Valid @RequestBody List<@Valid BorrowForm> borrowPOSTs) {
        List<BorrowDTO> result = borrowService.createBorrow(borrowPOSTs);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Override
    public ResponseEntity<BorrowListDTO> getMyBorrows(
            @Valid @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @Valid @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
            @Valid @RequestParam(value = "sort", required = false) String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Optional.ofNullable(sort).orElse("borrowTime")).ascending());
        Page<BorrowDTO> result = borrowService.getBorrowsForCurrentUser(pageable);
        BorrowListDTO dto = toDto(result);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<Void> deleteBorrow(@PathVariable UUID id) {
        borrowService.deleteBorrowById(id);
        return ResponseEntity.noContent().build();
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
