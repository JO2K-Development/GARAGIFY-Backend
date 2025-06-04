package com.jo2k.garagify.parking.service;

import com.jo2k.dto.ParkingSpotDTO;
import com.jo2k.dto.UserWithSpotsDTO;
import com.jo2k.garagify.common.exception.ParkingNotFoundException;
import com.jo2k.garagify.parking.api.AdminService;
import com.jo2k.garagify.parking.mapper.ParkingSpotMapper;
import com.jo2k.garagify.parking.persistence.model.ParkingSpot;
import com.jo2k.garagify.parking.persistence.repository.ParkingRepository;
import com.jo2k.garagify.parking.persistence.repository.ParkingSpotRepository;
import com.jo2k.garagify.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("adminService")
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final ParkingSpotRepository parkingSpotRepository;
    private final ParkingRepository parkingRepository;
    private final ParkingSpotMapper parkingSpotMapper;
    private final UserRepository userRepository;

    @Override
    public void assignSpotToUser(Integer parkingId, UUID spotId, UUID userId) {
        ParkingSpot spot = parkingSpotRepository.findByParking_IdAndSpotUuid(parkingId, spotId)
                .orElseThrow(() -> new IllegalArgumentException("Parking spot not found"));
        spot.setOwner(userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found")));
        parkingSpotRepository.save(spot);
    }

    @Override
    public List<UserWithSpotsDTO> getAllUsersWithSpots(Integer parkingId) {
        if (!parkingRepository.existsById(parkingId)) {
            throw new ParkingNotFoundException("Parking not found");
        }

        List<ParkingSpot> parkingSpots = parkingSpotRepository.findAllByParkingId(parkingId);

        Map<UUID, List<ParkingSpot>> spotsByUser = new LinkedHashMap<>();
        for (ParkingSpot spot : parkingSpots) {
            UUID ownerId = spot.getOwner() != null ? spot.getOwner().getId() : null;
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