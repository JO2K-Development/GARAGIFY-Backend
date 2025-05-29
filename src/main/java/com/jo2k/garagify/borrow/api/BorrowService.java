package com.jo2k.garagify.borrow.api;

import com.jo2k.dto.BorrowGET;
import com.jo2k.dto.BorrowPOST;

import java.util.List;


public interface BorrowService {
    List<BorrowGET> createBorrow(List<BorrowPOST> borrowPOSTs);
}
