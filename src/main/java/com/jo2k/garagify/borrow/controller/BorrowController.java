package com.jo2k.garagify.borrow.controller;

import com.jo2k.api.BorrowControllerApi;
import com.jo2k.dto.BorrowGET;
import com.jo2k.dto.BorrowPOST;
import com.jo2k.dto.PageBorrowGET;
import com.jo2k.garagify.borrow.api.BorrowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Validated
@RequiredArgsConstructor
@RestController
public class BorrowController implements BorrowControllerApi {

    private final BorrowService borrowService;

    @Override
    public ResponseEntity<List<BorrowGET>> createBorrow(@Valid @RequestBody List<@Valid BorrowPOST> borrowPOSTs) {
        List<BorrowGET> result = borrowService.createBorrow(borrowPOSTs);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }


    @Override
    public ResponseEntity<PageBorrowGET> getMyBorrows(
            @Valid @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @Valid @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
            @Valid @RequestParam(value = "sort", required = false) String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Optional.ofNullable(sort).orElse("borrowTime")).ascending());
        Page<BorrowGET> result = borrowService.getBorrowsForCurrentUser(pageable);
        PageBorrowGET dto = toDto(result);
        return ResponseEntity.ok(dto);
    }

    private static PageBorrowGET toDto(Page<BorrowGET> page) {
        PageBorrowGET dto = new PageBorrowGET();
        dto.setContent(page.getContent());
        dto.setTotalElements((long) page.getTotalElements()); // or cast to long if needed
        dto.setTotalPages(page.getTotalPages());
        dto.setPage(page.getNumber());
        dto.setSize(page.getSize());
        return dto;
    }
}
