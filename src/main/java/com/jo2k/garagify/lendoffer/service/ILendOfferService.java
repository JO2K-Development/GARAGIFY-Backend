package com.jo2k.garagify.lendoffer.service;

import com.jo2k.garagify.lendoffer.model.LendOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Map;


/**
 * Service interface for managing parking spot lend offers
 */
public interface ILendOfferService {

    /**
     * Creates a new lend offer for a parking spot
     * @param parkingSpotId ID of the parking spot being offered
     * @param ownerId ID of the user creating the offer
     * @param startDate Start date of availability
     * @param endDate End date of availability
     * @return The created lend offer
     */
    LendOffer createLendOffer(UUID parkingSpotId,
                              UUID ownerId,
                              OffsetDateTime startDate,
                              OffsetDateTime endDate);

    /**
     * Gets all lend offers with optional filters
     * @param startDate Minimum start date filter (inclusive)
     * @param endDate Maximum end date filter (inclusive)
     * @param ownerId Filter by owner ID
     * @param pageable Pagination information
     * @return Page of lend offers matching criteria
     */
    Page<LendOffer> getAllLendOffers(OffsetDateTime startDate,
                                     OffsetDateTime endDate,
                                     UUID ownerId,
                                     Pageable pageable);

    /**
     * Gets a lend offer by ID
     * @param id ID of the lend offer
     * @return The requested lend offer
     * @throws com.jo2k.garagify.common.exception.ResourceNotFoundException if not found
     */
    LendOffer getLendOfferById(UUID id);

    /**
     * Updates a lend offer's time range
     * @param id ID of the lend offer to update
     * @param startDate New start date
     * @param endDate New end date
     * @return The updated lend offer
     * @throws com.jo2k.garagify.common.exception.ResourceNotFoundException if not found
     */
    LendOffer updateLendOffer(UUID id,
                              OffsetDateTime startDate,
                              OffsetDateTime endDate);

    /**
     * Partially updates a lend offer
     * @param id ID of the lend offer to patch
     * @param updates Map of fields to update
     * @return The patched lend offer
     */
    LendOffer patchLendOffer(UUID id, Map<String, Object> updates);

    /**
     * Deletes a lend offer
     * @param id ID of the lend offer to delete
     * @throws com.jo2k.garagify.common.exception.ResourceNotFoundException if not found
     */
    void deleteLendOffer(UUID id);

    /**
     * Checks if a parking spot is available for given time range
     * @param parkingSpotId ID of the parking spot
     * @param startDate Start date to check
     * @param endDate End date to check
     * @return True if available, false if already booked or offered
     */
    boolean isParkingSpotAvailable(UUID parkingSpotId,
                                   OffsetDateTime startDate,
                                   OffsetDateTime endDate);

    /**
     * Gets all lend offers for a specific parking spot
     * @param parkingSpotId ID of the parking spot
     * @return List of lend offers for the spot
     */
    List<LendOffer> getLendOffersForParkingSpot(UUID parkingSpotId);
}