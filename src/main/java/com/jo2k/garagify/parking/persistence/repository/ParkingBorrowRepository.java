package com.jo2k.garagify.parking.persistence.repository;

import com.jo2k.garagify.parking.persistence.model.ParkingBorrow;
import com.jo2k.garagify.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface ParkingBorrowRepository extends JpaRepository<ParkingBorrow, UUID> {

    @Query("""
    SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
    FROM ParkingBorrow b
    WHERE b.parkingSpot.parking.id = :parkingId
      AND b.parkingSpot.spotUuid = :spotUuid
      AND (
            (:startDate < b.returnTime AND :endDate > b.borrowTime)
          )
    """)
    boolean existsOverlap(
            @Param("parkingId") Integer parkingId,
            @Param("spotUuid") UUID spotUuid,
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate
    );



    Page<ParkingBorrow> findAllByUser(User user, Pageable pageable);

}