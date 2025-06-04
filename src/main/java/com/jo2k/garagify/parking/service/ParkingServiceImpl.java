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
import java.util.UUID;

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
    public List<ParkingSpotDTO> getParkingSpotsByParkingIdNotOwnedByUser(Integer parkingId, UUID userId) {
        if (!parkingRepository.existsById(parkingId)) {
            throw new ParkingNotFoundException("Parking not found");
        }
        return parkingSpotMapper.toList(
                parkingSpotRepository.findAllByParkingIdAndOwnerIdNot(parkingId, userId)
        );
    }

    @Override
    public List<ParkingSpotDTO> getParkingSpotsByParkingIdForCurrentUser(Integer parkingId, UUID userId) {
        if (!parkingRepository.existsById(parkingId)) {
            throw new ParkingNotFoundException("Parking not found");
        }
        return parkingSpotMapper.toList(
                parkingSpotRepository.findAllByParkingIdAndOwnerId(parkingId, userId)
        );
    }
}
