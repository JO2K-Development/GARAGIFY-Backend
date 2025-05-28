package com.jo2k.garagify.borrow.controller;

import com.jo2k.api.BorrowControllerApi;
import com.jo2k.dto.BorrowGET;
import com.jo2k.dto.BorrowPOST;
import com.jo2k.garagify.borrow.service.IBorrowService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
@Validated
@RestController
public class BorrowController implements BorrowControllerApi {

    private final IBorrowService borrowService;

    public BorrowController(IBorrowService borrowService) {
        this.borrowService = borrowService;
    }
    public ResponseEntity<List<BorrowGET>> createBorrows(
            @Valid @RequestBody List<@Valid BorrowPOST> borrowPOSTs) {

        try {

            System.out.println("AAa");
            List<BorrowGET> result = borrowService.createBorrowsIfNotExistsAndAvailable(borrowPOSTs);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (IllegalArgumentException e) {
            System.out.println("AAa1");
            e.printStackTrace();
            return ResponseEntity
                    .badRequest()
                    .body((List<BorrowGET>) Map.of("message", e.toString()));
        }
    }
}
