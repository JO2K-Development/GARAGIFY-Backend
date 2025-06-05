package com.jo2k.garagify.parking.service;

import com.jo2k.dto.ParkingSpotDTO;
import com.jo2k.dto.TimeRangeDto;
import com.jo2k.garagify.parking.api.ParkingAvailabilityService;
import com.jo2k.garagify.parking.api.ParkingService;
import com.jo2k.garagify.parking.mapper.TimeRangeMapper;
import com.jo2k.garagify.parking.persistence.model.ParkingBorrow;
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
        List<TimeRangeDto> allRanges = new ArrayList<>();
        for (var spot : spots) {
            UUID spotUuid = spot.getSpotUuid();
            List<ParkingLend> offers = getLendOffersUntil(parkingId, spotUuid, untilWhen);
            for (ParkingLend offer : offers) {
                OffsetDateTime offerStart = offer.getStartDate();
                OffsetDateTime offerEnd = clampEnd(offer.getEndDate(), untilWhen);
                List<TimeRangeDto> freeRanges = getFreeRangesForSpot(offer.getId(), offerStart, offerEnd);
                allRanges.addAll(freeRanges);
            }
        }
        return mergeTimeRanges(allRanges);
    }


    @Override
    public List<UUID> getSpots(Integer parkingId, OffsetDateTime from, OffsetDateTime until) {
        UUID userId = userService.getCurrentUser().getId();
        List<ParkingSpotDTO> spots = parkingService.getParkingSpotsByParkingIdNotOwnedByUser(parkingId, userId);
        List<UUID> result = new ArrayList<>();
        for (var spot : spots) {
            UUID spotUuid = spot.getSpotUuid();
            List<ParkingLend> offers = parkingLendRepository.findByParkingSpot_Parking_IdAndParkingSpot_SpotUuid(parkingId, spotUuid);
            boolean available = offers.stream().anyMatch(offer ->
                    !offer.getStartDate().isAfter(from) &&
                            !offer.getEndDate().isBefore(until) &&
                            isNotBorrowedForLendOffer(offer.getId(), from, until)
            );
            if (available) {
                result.add(spotUuid);
            }
        }
        return result;
    }

    private boolean isNotBorrowedForLendOffer(UUID lendOfferId, OffsetDateTime from, OffsetDateTime until) {
        List<ParkingBorrow> borrows = borrowRepository.findAllByParkingLendOffer_Id(lendOfferId);
        for (ParkingBorrow borrow : borrows) {
            if (from.isBefore(borrow.getReturnTime()) && until.isAfter(borrow.getBorrowTime())) {
                return false;
            }
        }
        return true;
    }

    private List<TimeRangeDto> getFreeRangesForSpot(UUID lendOfferId, OffsetDateTime offerStart, OffsetDateTime offerEnd) {
        List<ParkingBorrow> borrows = borrowRepository.findAllByParkingLendOffer_Id(lendOfferId);
        List<TimeRangeDto> freeRanges = new ArrayList<>();
        OffsetDateTime current = offerStart;
        borrows.sort(Comparator.comparing(ParkingBorrow::getBorrowTime));
        for (ParkingBorrow borrow : borrows) {
            if (borrow.getBorrowTime().isAfter(current)) {
                freeRanges.add(new TimeRangeDto(current, borrow.getBorrowTime()));
            }
            current = borrow.getReturnTime().isAfter(current) ? borrow.getReturnTime() : current;
        }
        if (current.isBefore(offerEnd)) {
            freeRanges.add(new TimeRangeDto(current, offerEnd));
        }
        return freeRanges;
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

