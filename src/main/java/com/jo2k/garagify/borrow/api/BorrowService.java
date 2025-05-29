package com.jo2k.garagify.borrow.api;

import com.jo2k.dto.BorrowDTO;
import com.jo2k.dto.BorrowForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;


public interface BorrowService {
    List<BorrowDTO> createBorrow(List<BorrowForm> borrowPOSTs);

    Page<BorrowDTO> getBorrowsForCurrentUser(Pageable pageable);

    void deleteBorrowById(UUID id);
}
