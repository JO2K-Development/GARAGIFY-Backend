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


}
