package com.jo2k.garagify.parking.persistence.repository;

import com.jo2k.garagify.parking.persistence.model.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Integer> {

    List<ParkingSpot> findAllByParkingId(Integer parkingId);

    Optional<ParkingSpot> findByParking_IdAndSpotUuid(Integer parkingId, UUID spotUuid);

}
