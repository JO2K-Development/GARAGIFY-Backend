package com.jo2k.garagify.lendoffer.repository;

import com.jo2k.garagify.lendoffer.model.LendOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface LendOfferRepository
        extends JpaRepository<LendOffer, UUID>, JpaSpecificationExecutor<LendOffer> {

    @Query("SELECT lo FROM LendOffer lo WHERE " +
            "(:startDate IS NULL OR lo.startDate >= :startDate) AND " +
            "(:endDate IS NULL OR lo.endDate <= :endDate) AND " +
            "(:ownerId IS NULL OR lo.ownerId = :ownerId)")
    List<LendOffer> findByFilters(
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate,
            @Param("ownerId") UUID ownerId
    );

    @Query("SELECT lo FROM LendOffer lo WHERE " +
            "lo.parkingSpotId = :parkingSpotId AND " +
            "lo.startDate <= :endDate AND " +
            "lo.endDate >= :startDate")
    List<LendOffer> findOverlappingOffers(
            @Param("parkingSpotId") UUID parkingSpotId,
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate
    );

    List<LendOffer> findByParkingSpotId(UUID parkingSpotId);

    boolean existsByParkingSpotIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            UUID parkingSpotId,
            OffsetDateTime endDate,
            OffsetDateTime startDate
    );

}