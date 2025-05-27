package com.jo2k.garagify.borrow.service;

import com.jo2k.dto.BorrowGET;
import com.jo2k.dto.BorrowPOST;

import java.util.List;


public interface IBorrowService {
    public List<BorrowGET> createBorrowsIfNotExistsAndAvailable(List<BorrowPOST> borrowPOSTs);
}
