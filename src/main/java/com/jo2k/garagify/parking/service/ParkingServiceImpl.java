package com.jo2k.garagify.parking.service;

import com.jo2k.dto.*;
import com.jo2k.garagify.common.exception.ParkingNotFoundException;
import com.jo2k.garagify.parking.api.ParkingService;
import com.jo2k.garagify.parking.mapper.ParkingMapper;
import com.jo2k.garagify.parking.mapper.ParkingSpotMapper;
import com.jo2k.garagify.parking.persistence.model.ParkingSpot;
import com.jo2k.garagify.parking.persistence.repository.ParkingRepository;
import com.jo2k.garagify.parking.persistence.repository.ParkingSpotRepository;
import com.jo2k.garagify.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ParkingServiceImpl implements ParkingService {

    private final ParkingRepository parkingRepository;
    private final ParkingSpotRepository parkingSpotRepository;
    private final ParkingMapper parkingMapper;
    private final ParkingSpotMapper parkingSpotMapper;
    private final UserRepository userRepository;

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

    @Override
    public List<UserWithSpotsDTO> getAllUsersWithSpots(Integer parkingId) {
        if (!parkingRepository.existsById(parkingId)) {
            throw new ParkingNotFoundException("Parking not found");
        }

        List<ParkingSpot> parkingSpots = parkingSpotRepository.findAllByParkingId(parkingId);

        Map<UUID, List<ParkingSpot>> spotsByUser = new LinkedHashMap<>();
        for (ParkingSpot spot : parkingSpots) {
            UUID ownerId = spot.getOwnerId();
            if (ownerId != null) {
                spotsByUser.computeIfAbsent(ownerId, k -> new ArrayList<>()).add(spot);
            }
        }

        List<UUID> userIds = new ArrayList<>(spotsByUser.keySet());
        Map<UUID, String> userEmails = userRepository.findAllById(userIds).stream()
                .collect(HashMap::new, (map, user) -> map.put(user.getId(), user.getEmail()), HashMap::putAll);

        List<UserWithSpotsDTO> allUsersWithSpots = new ArrayList<>();
        for (Map.Entry<UUID, List<ParkingSpot>> entry : spotsByUser.entrySet()) {
            UUID userId = entry.getKey();
            List<ParkingSpotDTO> spotDTOs = parkingSpotMapper.toList(entry.getValue());

            UserWithSpotsDTO dto = new UserWithSpotsDTO();
            dto.setUserId(userId);
            dto.setEmail(userEmails.getOrDefault(userId, "unknown@example.com"));
            dto.setSpots(spotDTOs);
            allUsersWithSpots.add(dto);
        }

        return allUsersWithSpots;
    }
}
