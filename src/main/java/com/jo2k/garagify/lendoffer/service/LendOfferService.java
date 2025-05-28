package com.jo2k.garagify.lendoffer.service;

import com.jo2k.garagify.common.exception.ResourceNotFoundException;
import com.jo2k.garagify.lendoffer.model.LendOffer;
import com.jo2k.garagify.lendoffer.repository.LendOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LendOfferService implements ILendOfferService {

    private final LendOfferRepository lendOfferRepository;

    @Override
    @Transactional
    public LendOffer createLendOffer(UUID parkingSpotId, UUID ownerId,
                                     OffsetDateTime startDate, OffsetDateTime endDate) {
        validateTimeRange(startDate, endDate);
        checkAvailability(parkingSpotId, startDate, endDate);

        LendOffer offer = new LendOffer();
        offer.setParkingSpotId(parkingSpotId);
        offer.setOwnerId(ownerId);
        offer.setStartDate(startDate);
        offer.setEndDate(endDate);

        return lendOfferRepository.save(offer);
    }

    @Override
    public Page<LendOffer> getAllLendOffers(OffsetDateTime startDate, OffsetDateTime endDate,
                                            UUID ownerId, Pageable pageable) {
        Specification<LendOffer> spec = Specification.where(null);

        if (startDate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("endDate"), startDate));
        }

        if (endDate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("startDate"), endDate));
        }

        if (ownerId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("ownerId"), ownerId));
        }

        return lendOfferRepository.findAll(spec, pageable);
    }

    @Override
    public LendOffer getLendOfferById(UUID id) {
        return lendOfferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LendOffer", "id", id));
    }

    @Override
    @Transactional
    public LendOffer updateLendOffer(UUID id, OffsetDateTime startDate, OffsetDateTime endDate) {
        validateTimeRange(startDate, endDate);

        LendOffer offer = getLendOfferById(id);
        checkAvailability(offer.getParkingSpotId(), startDate, endDate, id);

        offer.setStartDate(startDate);
        offer.setEndDate(endDate);

        return lendOfferRepository.save(offer);
    }

    @Override
    @Transactional
    public LendOffer patchLendOffer(UUID id, Map<String, Object> updates) {
        LendOffer offer = getLendOfferById(id);

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();

            try {
                Field field = LendOffer.class.getDeclaredField(fieldName);
                field.setAccessible(true);

                // Handle date parsing if necessary
                if ((fieldName.equals("startDate") || fieldName.equals("endDate")) && fieldValue instanceof String) {
                    field.set(offer, LocalDateTime.parse((String) fieldValue));
                } else {
                    field.set(offer, fieldValue);
                }

            } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid field: " + fieldName, e);
            }
        }

        // Optional: re-validate and re-check availability if startDate or endDate were patched
        if (updates.containsKey("startDate") || updates.containsKey("endDate")) {
            OffsetDateTime start = offer.getStartDate();
            OffsetDateTime end = offer.getEndDate();
            validateTimeRange(start, end);
            checkAvailability(offer.getParkingSpotId(), start, end, offer.getId());
        }

        return lendOfferRepository.save(offer);
    }

    @Override
    @Transactional
    public void deleteLendOffer(UUID id) {
        if (!lendOfferRepository.existsById(id)) {
            throw new ResourceNotFoundException("LendOffer", "id", id);
        }
        lendOfferRepository.deleteById(id);
    }

    @Override
    public boolean isParkingSpotAvailable(UUID parkingSpotId,
                                          OffsetDateTime startDate,
                                          OffsetDateTime endDate) {
        validateTimeRange(startDate, endDate);
        List<LendOffer> overlapping = lendOfferRepository.findOverlappingOffers(
                parkingSpotId, startDate, endDate);
        return overlapping.isEmpty();
    }

    @Override
    public List<LendOffer> getLendOffersForParkingSpot(UUID parkingSpotId) {
        return lendOfferRepository.findByParkingSpotId(parkingSpotId);
    }

    private void validateTimeRange(OffsetDateTime startDate, OffsetDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
        if (startDate.isBefore(OffsetDateTime.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }
    }

    private void checkAvailability(UUID parkingSpotId, OffsetDateTime startDate,
                                   OffsetDateTime endDate) {
        checkAvailability(parkingSpotId, startDate, endDate, null);
    }

    private void checkAvailability(UUID parkingSpotId, OffsetDateTime startDate,
                                   OffsetDateTime endDate, UUID excludeOfferId) {
        List<LendOffer> overlapping = lendOfferRepository.findOverlappingOffers(
                parkingSpotId, startDate, endDate);

        boolean isAvailable = overlapping.isEmpty() ||
                (excludeOfferId != null && overlapping.stream()
                        .allMatch(offer -> offer.getId().equals(excludeOfferId)));

        if (!isAvailable) {
            throw new IllegalStateException("Parking spot is not available for the requested time range");
        }
    }
}