package com.jo2k.garagify.parking.service;

import com.jo2k.dto.TimeRangeDto;
import com.jo2k.garagify.parking.api.ParkingAvailabilityService;
import com.jo2k.garagify.parking.api.ParkingService;
import com.jo2k.garagify.parking.persistence.model.ParkingLend;
import com.jo2k.garagify.parking.persistence.repository.ParkingLendRepository;
import com.jo2k.garagify.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

@Service("availabilityLendService")
@RequiredArgsConstructor
public class AvailabilityLendServiceImpl implements ParkingAvailabilityService {
    private final ParkingLendRepository parkingLendRepository;
    private final ParkingService parkingService;
    private final UserService userService;

    @Override
    public List<TimeRangeDto> getTimeRanges(Integer parkingId, OffsetDateTime untilWhen) {
        OffsetDateTime now = OffsetDateTime.now();
        UUID userId = userService.getCurrentUser().getId();
        var spots = parkingService.getParkingSpotsByParkingIdNotOwnedByUser(parkingId, userId);

        List<TimeRangeDto> allFree = new ArrayList<>();

        for (var spot : spots) {
            UUID spotUuid = spot.getSpotUuid();
            List<ParkingLend> offers = getSortedLendOffers(parkingId, spotUuid);
            allFree.addAll(getFreeTimeRangesForSpot(now, untilWhen, offers));
        }
        return mergeTimeRanges(allFree);
    }

    @Override
    public List<UUID> getSpots(Integer parkingId, OffsetDateTime from, OffsetDateTime until) {
        UUID userId = userService.getCurrentUser().getId();
        var spots = parkingService.getParkingSpotsByParkingIdNotOwnedByUser(parkingId, userId);
        List<UUID> result = new ArrayList<>();

        for (var spot : spots) {
            UUID spotUuid = spot.getSpotUuid();
            if (isSpotFreeForLend(parkingId, spotUuid, from, until)) {
                result.add(spotUuid);
            }
        }
        return result;
    }

    private List<ParkingLend> getSortedLendOffers(Integer parkingId, UUID spotUuid) {
        List<ParkingLend> offers = parkingLendRepository.findByParkingSpot_Parking_IdAndParkingSpot_SpotUuid(parkingId, spotUuid);
        offers.sort(Comparator.comparing(ParkingLend::getStartDate));
        return offers;
    }


    private List<TimeRangeDto> getFreeTimeRangesForSpot(
            OffsetDateTime from, OffsetDateTime untilWhen, List<ParkingLend> offers) {

        List<TimeRangeDto> freeSlots = new ArrayList<>();
        OffsetDateTime windowStart = from;

        for (ParkingLend offer : offers) {
            if (windowStart.isBefore(offer.getStartDate())) {
                freeSlots.add(new TimeRangeDto(windowStart, offer.getStartDate()));
            }
            windowStart = offer.getEndDate().isAfter(windowStart) ? offer.getEndDate() : windowStart;
        }
        if (windowStart.isBefore(untilWhen)) {
            freeSlots.add(new TimeRangeDto(windowStart, untilWhen));
        }
        return freeSlots;
    }

    private boolean isSpotFreeForLend(Integer parkingId, UUID spotUuid, OffsetDateTime from, OffsetDateTime until) {
        return parkingLendRepository.findByParkingSpot_Parking_IdAndParkingSpot_SpotUuid(parkingId, spotUuid)
                .stream()
                .noneMatch(lo -> from.isBefore(lo.getEndDate()) && until.isAfter(lo.getStartDate()));
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
