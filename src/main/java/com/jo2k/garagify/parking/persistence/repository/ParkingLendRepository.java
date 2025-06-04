package com.jo2k.garagify.parking.persistence.repository;

import com.jo2k.garagify.parking.persistence.model.ParkingLend;
import com.jo2k.garagify.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParkingLendRepository
        extends JpaRepository<ParkingLend, UUID>, JpaSpecificationExecutor<ParkingLend> {

    @Query("""
                SELECT lo FROM ParkingLend lo
                WHERE (:startDate IS NULL OR lo.startDate >= :startDate)
                  AND (:endDate IS NULL OR lo.endDate <= :endDate)
                  AND (:owner IS NULL OR lo.owner = :owner)
            """)
    List<ParkingLend> findByFilters(
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate,
            @Param("owner") User owner
    );


    @Query("""
                SELECT lo FROM ParkingLend lo
                WHERE lo.parkingSpot.parking.id = :parkingId
                    AND lo.parkingSpot.spotUuid = :spotUuid
                  AND lo.startDate <= :endDate
                  AND lo.endDate >= :startDate
            """)
    List<ParkingLend> findOverlappingOffers(
            @Param("parkingId") Integer parkingId,
            @Param("parkingSpotId") UUID parkingSpotId,
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate
    );

    List<ParkingLend> findByParkingSpot_Parking_IdAndParkingSpot_SpotUuid(Integer parkingId, UUID spotUuid);

    Page<ParkingLend> findAllByOwner(User owner, Pageable pageable);

    boolean existsByParkingSpot_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Integer parkingSpotId,
            OffsetDateTime endDate,
            OffsetDateTime startDate
    );

    Optional<ParkingLend> findFirstByParkingSpot_SpotUuidAndParkingSpot_Parking_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            UUID spotUuid, Integer parkingId, OffsetDateTime startDate, OffsetDateTime endDate);
}
