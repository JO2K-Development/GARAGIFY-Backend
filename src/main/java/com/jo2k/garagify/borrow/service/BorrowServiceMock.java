package com.jo2k.garagify.borrow.service;

import com.jo2k.dto.BorrowGET;
import com.jo2k.garagify.borrow.model.Borrow;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Profile("dev")
public class BorrowServiceMock implements IBorrowService {

    @Override
    public Page<BorrowGET> getAllBorrows(Pageable pageable, OffsetDateTime start, OffsetDateTime end, UUID borrowerId) {
        return null;
    }

    @Override
    public Borrow createBorrow(Borrow borrow) {
        return null;
    }
}
