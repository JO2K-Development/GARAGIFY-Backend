package com.jo2k.garagify.borrow.persistence.repository;

import com.jo2k.garagify.borrow.persistence.model.Borrow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface BorrowRepository extends JpaRepository<Borrow, UUID> {

    @Query("""
                SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
                FROM Borrow b
                WHERE b.parkingSpotId = :spotId
                  AND (
                        (:startDate < b.returnTime AND :endDate > b.borrowTime)
                      )
            """)
    boolean existsOverlap(
            @Param("spotId") UUID spotId,
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate
    );

    Page<Borrow> findAllByUserId(UUID userId, Pageable pageable);

}