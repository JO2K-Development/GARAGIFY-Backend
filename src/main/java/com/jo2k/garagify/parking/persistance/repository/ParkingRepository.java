package com.jo2k.garagify.parking.persistance.repository;

import com.jo2k.garagify.parking.persistance.model.Parking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingRepository extends JpaRepository<Parking, Integer> {
}

