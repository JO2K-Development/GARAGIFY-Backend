package com.jo2k.garagify.borrow.service;

import com.jo2k.dto.BorrowGET;
import com.jo2k.garagify.borrow.model.Borrow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface IBorrowService {
    // Define methods that will be implemented by the service classes
    // For example:
    // void borrowItem(UUID userId, UUID itemId);
    // void returnItem(UUID userId, UUID itemId);
    Page<BorrowGET> getAllBorrows(Pageable pageable, OffsetDateTime start, OffsetDateTime end, UUID borrowerId);
    Borrow createBorrow(Borrow borrow);
}
