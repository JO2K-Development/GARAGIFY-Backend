package com.jo2k.garagify.borrow.api;

import com.jo2k.dto.BorrowGET;
import com.jo2k.dto.BorrowPOST;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface BorrowService {
    List<BorrowGET> createBorrow(List<BorrowPOST> borrowPOSTs);

    Page<BorrowGET> getBorrowsForCurrentUser(Pageable pageable);
}
