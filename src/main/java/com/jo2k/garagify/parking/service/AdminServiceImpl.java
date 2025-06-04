package com.jo2k.garagify.parking.service;

import com.jo2k.dto.ParkingSpotDTO;
import com.jo2k.dto.UserWithSpotsDTO;
import com.jo2k.garagify.parking.api.AdminService;
import com.jo2k.garagify.parking.mapper.ParkingSpotMapper;
import com.jo2k.garagify.parking.persistence.model.ParkingSpot;
import com.jo2k.garagify.parking.persistence.repository.ParkingRepository;
import com.jo2k.garagify.parking.persistence.repository.ParkingSpotRepository;
import com.jo2k.garagify.user.model.User;
import com.jo2k.garagify.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("adminService")
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final ParkingSpotRepository parkingSpotRepository;
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
    public List<UserWithSpotsDTO> getAllUsersWithSpots() {
        // 1. Get all users
        List<User> allUsers = userRepository.findAll();
        // 2. Get all parking spots
        List<ParkingSpot> allSpots = parkingSpotRepository.findAll();

        // 3. Group spots by user ID
        Map<UUID, List<ParkingSpot>> spotsByUser = new HashMap<>();
        for (ParkingSpot spot : allSpots) {
            UUID ownerId = spot.getOwner() != null ? spot.getOwner().getId() : null;
            if (ownerId != null) {
                spotsByUser.computeIfAbsent(ownerId, k -> new ArrayList<>()).add(spot);
            }
        }

        // 4. Build result for all users, including those with no spots
        List<UserWithSpotsDTO> allUsersWithSpots = new ArrayList<>();
        for (User user : allUsers) {
            List<ParkingSpot> userSpots = spotsByUser.getOrDefault(user.getId(), Collections.emptyList());
            List<ParkingSpotDTO> spotDTOs = parkingSpotMapper.toList(userSpots);

            UserWithSpotsDTO dto = new UserWithSpotsDTO();
            dto.setUserId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setSpots(spotDTOs);
            allUsersWithSpots.add(dto);
        }

        return allUsersWithSpots;
    }
}