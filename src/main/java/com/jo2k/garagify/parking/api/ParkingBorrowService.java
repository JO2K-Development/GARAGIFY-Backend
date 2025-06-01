package com.jo2k.garagify.parking.api;

import com.jo2k.dto.BorrowDTO;
import com.jo2k.dto.TimeRangeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ParkingBorrowService {
    BorrowDTO createBorrowForSpot(Integer parkingId, UUID spotUuid, TimeRangeRequest timeRange);

    void deleteBorrowById(UUID id);

    Page<BorrowDTO> getBorrowsForCurrentUser(Pageable pageable);
}
