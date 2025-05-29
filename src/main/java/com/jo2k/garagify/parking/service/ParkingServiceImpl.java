package com.jo2k.garagify.parking.service;

import com.jo2k.dto.ParkingGET;
import com.jo2k.dto.ParkingSpotGET;
import com.jo2k.garagify.parking.api.ParkingService;
import com.jo2k.garagify.parking.mapper.ParkingMapper;
import com.jo2k.garagify.parking.persistance.repository.ParkingRepository;
import com.jo2k.garagify.parking.persistance.repository.ParkingSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingServiceImpl implements ParkingService {

    private final ParkingRepository parkingRepository;
    private final ParkingSpotRepository parkingSpotRepository;
    private final ParkingMapper parkingMapper;

    @Override
    public ParkingGET getParkingById(Integer parkingId) {
        return parkingRepository.findById(parkingId)
                .map(parkingMapper::toParkingGET)
                .orElse(null);
    }

    @Override
    public List<ParkingSpotGET> getParkingSpotsByParkingId(Integer parkingId) {
        return parkingMapper.toParkingSpotGETList(
                parkingSpotRepository.findAllByParkingId(parkingId)
        );
    }
}
