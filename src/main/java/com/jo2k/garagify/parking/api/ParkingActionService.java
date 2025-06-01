package com.jo2k.garagify.parking.api;

import com.jo2k.dto.TimeRangeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ParkingActionService<Action> {
    Action create(Integer parkingId, UUID spotUuid, TimeRangeRequest timeRange);

    void delete(UUID id);

    Page<Action> getForCurrentUser(Pageable pageable);
}