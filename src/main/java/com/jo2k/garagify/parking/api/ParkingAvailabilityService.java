package com.jo2k.garagify.parking.api;

import com.jo2k.dto.TimeRangeDto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface ParkingAvailabilityService {
    List<TimeRangeDto> getTimeRanges(Integer parkingId, OffsetDateTime untilWhen);

    List<UUID> getSpots(Integer parkingId, OffsetDateTime from, OffsetDateTime until);
}
