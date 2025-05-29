package com.jo2k.garagify.parking.persistance.repository;

import com.jo2k.garagify.parking.persistance.model.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Integer> {
    boolean existsBySpotUuid(UUID spotUuid);
    List<ParkingSpot> findAllByParkingId(Integer parkingId);

}
