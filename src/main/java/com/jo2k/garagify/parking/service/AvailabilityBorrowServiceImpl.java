package com.jo2k.garagify.parking.service;

import com.jo2k.dto.ParkingSpotDTO;
import com.jo2k.dto.TimeRangeDto;
import com.jo2k.garagify.lendoffer.persistence.model.LendOffer;
import com.jo2k.garagify.lendoffer.persistence.repository.LendOfferRepository;
import com.jo2k.garagify.parking.api.ParkingAvailability;
import com.jo2k.garagify.parking.api.ParkingService;
import com.jo2k.garagify.parking.mapper.TimeRangeMapper;
import com.jo2k.garagify.parking.persistence.repository.ParkingBorrowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service("availabilityBorrowService")
@RequiredArgsConstructor
public class AvailabilityBorrowServiceImpl implements ParkingAvailability {

    private final LendOfferRepository lendOfferRepository;
    private final ParkingBorrowRepository borrowRepository;
    private final ParkingService parkingService;
    private final TimeRangeMapper timeRangeMapper;

    @Override
    public List<TimeRangeDto> getTimeRanges(Integer parkingId, OffsetDateTime untilWhen) {
        List<ParkingSpotDTO> spots = parkingService.getParkingSpotsByParkingId(parkingId);
        List<TimeRangeDto> result = new ArrayList<>();
        for (var spot : spots) {
            UUID spotUuid = spot.getSpotUuid();
            List<LendOffer> offers = getLendOffersUntil(spotUuid, untilWhen);
            for (LendOffer offer : offers) {
                if (isNotBorrowed(parkingId, spotUuid, offer.getStartDate(), clampEnd(offer.getEndDate(), untilWhen))) {
                    result.add(timeRangeMapper.toDto(offer.getStartDate(), clampEnd(offer.getEndDate(), untilWhen)));
                }
            }
        }
        return mergeTimeRanges(result);
    }


    @Override
    public List<UUID> getSpots(Integer parkingId, OffsetDateTime from, OffsetDateTime until) {
        List<ParkingSpotDTO> spots = parkingService.getParkingSpotsByParkingId(parkingId);
        List<UUID> result = new ArrayList<>();
        for (var spot : spots) {
            UUID spotUuid = spot.getSpotUuid();
            if (hasLendOfferInRange(spotUuid, from, until) && isNotBorrowed(parkingId, spotUuid, from, until)) {
                result.add(spotUuid);
            }
        }
        return result;
    }


    private List<LendOffer> getLendOffersUntil(UUID spotUuid, OffsetDateTime untilWhen) {
        return lendOfferRepository.findByParkingSpotId(spotUuid).stream()
                .filter(lo -> !lo.getStartDate().isAfter(untilWhen))
                .collect(Collectors.toList());
    }

    private boolean hasLendOfferInRange(UUID spotUuid, OffsetDateTime from, OffsetDateTime until) {
        return lendOfferRepository.findByParkingSpotId(spotUuid).stream()
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
        TimeRangeDto prev = ranges.get(0);

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

