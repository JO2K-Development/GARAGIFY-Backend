package com.jo2k.garagify.parking.service;

import com.jo2k.dto.ParkingSpotDTO;
import com.jo2k.dto.TimeRangeDto;
import com.jo2k.garagify.parking.api.ParkingAvailabilityService;
import com.jo2k.garagify.parking.api.ParkingService;
import com.jo2k.garagify.parking.mapper.TimeRangeMapper;
import com.jo2k.garagify.parking.persistence.model.ParkingLend;
import com.jo2k.garagify.parking.persistence.repository.ParkingBorrowRepository;
import com.jo2k.garagify.parking.persistence.repository.ParkingLendRepository;
import com.jo2k.garagify.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service("availabilityBorrowService")
@RequiredArgsConstructor
public class AvailabilityBorrowServiceImpl implements ParkingAvailabilityService {

    private final ParkingLendRepository parkingLendRepository;
    private final ParkingBorrowRepository borrowRepository;
    private final ParkingService parkingService;
    private final UserService userService;
    private final TimeRangeMapper timeRangeMapper;

    @Override
    public List<TimeRangeDto> getTimeRanges(Integer parkingId, OffsetDateTime untilWhen) {
        UUID userId = userService.getCurrentUser().getId();
        List<ParkingSpotDTO> spots = parkingService.getParkingSpotsByParkingIdNotOwnedByUser(parkingId, userId);
        List<TimeRangeDto> result = new ArrayList<>();
        for (var spot : spots) {
            UUID spotUuid = spot.getSpotUuid();
            List<ParkingLend> offers = getLendOffersUntil(parkingId, spotUuid, untilWhen);
            for (ParkingLend offer : offers) {
                if (isNotBorrowed(parkingId, spotUuid, offer.getStartDate(), clampEnd(offer.getEndDate(), untilWhen))) {
                    result.add(timeRangeMapper.toDto(offer.getStartDate(), clampEnd(offer.getEndDate(), untilWhen)));
                }
            }
        }
        return mergeTimeRanges(result);
    }


    @Override
    public List<UUID> getSpots(Integer parkingId, OffsetDateTime from, OffsetDateTime until) {
        UUID userId = userService.getCurrentUser().getId();
        List<ParkingSpotDTO> spots = parkingService.getParkingSpotsByParkingIdNotOwnedByUser(parkingId, userId);
        List<UUID> result = new ArrayList<>();
        for (var spot : spots) {
            UUID spotUuid = spot.getSpotUuid();
            if (hasLendOfferInRange(parkingId, spotUuid, from, until) && isNotBorrowed(parkingId, spotUuid, from, until)) {
                result.add(spotUuid);
            }
        }
        return result;
    }


    private List<ParkingLend> getLendOffersUntil(Integer parkingId, UUID spotUuid, OffsetDateTime untilWhen) {
        return parkingLendRepository.findByParkingSpot_Parking_IdAndParkingSpot_SpotUuid(parkingId, spotUuid)
                .stream()
                .filter(lo -> !lo.getStartDate().isAfter(untilWhen))
                .collect(Collectors.toList());
    }

    private boolean hasLendOfferInRange(Integer parkingId, UUID spotUuid, OffsetDateTime from, OffsetDateTime until) {
        return parkingLendRepository.findByParkingSpot_Parking_IdAndParkingSpot_SpotUuid(parkingId, spotUuid)
                .stream()
                .anyMatch(lo -> !lo.getStartDate().isAfter(from) && !lo.getEndDate().isBefore(until));
    }


    private boolean isNotBorrowed(Integer parkingId, UUID spotUuid, OffsetDateTime start, OffsetDateTime end) {
        return !borrowRepository.existsOverlap(parkingId, spotUuid, start, end);
    }

    private OffsetDateTime clampEnd(OffsetDateTime candidate, OffsetDateTime untilWhen) {
        return candidate.isAfter(untilWhen) ? untilWhen : candidate;
    }

    private List<TimeRangeDto> mergeTimeRanges(List<TimeRangeDto> ranges) {
        if (ranges.isEmpty()) return Collections.emptyList();
        ranges.sort(Comparator.comparing(TimeRangeDto::getStart));

        List<TimeRangeDto> merged = new ArrayList<>();
        TimeRangeDto prev = ranges.getFirst();

        for (int i = 1; i < ranges.size(); i++) {
            TimeRangeDto curr = ranges.get(i);
            if (!prev.getEnd().isBefore(curr.getStart())) {
                prev = new TimeRangeDto(
                        prev.getStart(),
                        prev.getEnd().isAfter(curr.getEnd()) ? prev.getEnd() : curr.getEnd()
                );
            } else {
                merged.add(prev);
                prev = curr;
            }
        }
        merged.add(prev);
        return merged;
    }

}

