package com.jo2k.garagify.parking.service;

import com.jo2k.dto.ParkingDTO;
import com.jo2k.dto.ParkingSpotDTO;
import com.jo2k.garagify.common.exception.ParkingNotFoundException;
import com.jo2k.garagify.parking.api.ParkingService;
import com.jo2k.garagify.parking.mapper.ParkingMapper;
import com.jo2k.garagify.parking.mapper.ParkingSpotMapper;
import com.jo2k.garagify.parking.persistence.repository.ParkingRepository;
import com.jo2k.garagify.parking.persistence.repository.ParkingSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingServiceImpl implements ParkingService {

    private final ParkingRepository parkingRepository;
    private final ParkingSpotRepository parkingSpotRepository;
    private final ParkingMapper parkingMapper;
    private final ParkingSpotMapper parkingSpotMapper;

    @Override
    public ParkingDTO getParkingById(Integer parkingId) {
        return parkingRepository.findById(parkingId)
                .map(parkingMapper::toDto)
                .orElseThrow(() -> new ParkingNotFoundException("Parking not found"));
    }

    @Override
    public List<ParkingSpotDTO> getParkingSpotsByParkingId(Integer parkingId) {
        if (!parkingRepository.existsById(parkingId)) {
            throw new ParkingNotFoundException("Parking not found");
        }
        return parkingSpotMapper.toList(
                parkingSpotRepository.findAllByParkingId(parkingId)
        );
    }
}
