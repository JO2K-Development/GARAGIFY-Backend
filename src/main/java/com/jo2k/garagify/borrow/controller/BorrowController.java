package com.jo2k.garagify.borrow.controller;

import com.jo2k.api.BorrowControllerApi;
import com.jo2k.dto.BorrowGET;
import com.jo2k.dto.BorrowPOST;
import com.jo2k.garagify.borrow.api.BorrowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
