package com.jo2k.garagify.parking.api;

import com.jo2k.dto.BorrowDTO;
import com.jo2k.dto.LendOfferDTO;
import com.jo2k.dto.TimeRangeRequest;
import com.jo2k.garagify.lendoffer.persistence.model.LendOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public interface ParkingLendService {
    LendOfferDTO createLendOfferForSpot(Integer parkingId, UUID spotUuid, TimeRangeRequest timeRange);

    Page<LendOfferDTO> getLendsForCurrentUser(Pageable pageable);

    void deleteLendOfferById(UUID lendOfferId);
}