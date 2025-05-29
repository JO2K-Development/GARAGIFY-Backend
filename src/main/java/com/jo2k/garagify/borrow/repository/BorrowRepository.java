package com.jo2k.garagify.borrow.repository;

import com.jo2k.garagify.borrow.model.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface BorrowRepository extends JpaRepository<Borrow, UUID>, JpaSpecificationExecutor<Borrow> {
    @Query("""
      select case when count(b)>0 then true else false end
      from Borrow b
      where b.parkingSpotId = :spotId
        and b.borrowTime  < :endDate      
        and (b.returnTime is null 
             or b.returnTime > :startDate) 
    """)
    boolean existsOverlap(
            @Param("spotId") UUID spotId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate")   LocalDateTime endDate
    );
}
