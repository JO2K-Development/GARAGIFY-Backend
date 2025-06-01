package com.jo2k.garagify.parking.service;

import com.jo2k.dto.LendOfferDTO;
import com.jo2k.dto.TimeRangeRequest;
import com.jo2k.garagify.common.exception.InvalidLendException;
import com.jo2k.garagify.parking.api.ParkingLendService;
import com.jo2k.garagify.parking.mapper.ParkingLendMapper;
import com.jo2k.garagify.parking.persistence.model.ParkingLend;
import com.jo2k.garagify.parking.persistence.model.ParkingSpot;
import com.jo2k.garagify.parking.persistence.repository.ParkingLendRepository;
import com.jo2k.garagify.parking.persistence.repository.ParkingSpotRepository;
import com.jo2k.garagify.user.model.User;
import com.jo2k.garagify.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParkingLendServiceImpl implements ParkingLendService {

    private final ParkingLendRepository parkingLendRepository;
    private final UserService userService;
    private final ParkingSpotRepository parkingSpotRepository;
    private final ParkingLendMapper parkingLendMapper;

    @Override
    public LendOfferDTO createLendOfferForSpot(Integer parkingId, UUID spotUuid, TimeRangeRequest timeRange) {
        User currentUser = userService.getCurrentUser();

        ParkingSpot spot = parkingSpotRepository.findByParking_IdAndSpotUuid(parkingId, spotUuid)
                .orElseThrow(() -> new InvalidLendException("Parking spot not found"));

        boolean overlap = parkingLendRepository.existsByParkingSpot_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                spot.getId(), timeRange.getUntilWhen(), timeRange.getFromWhen()
        );
        if (overlap) {
            throw new InvalidLendException("Lend offer already exists in this time range for this spot");
        }

        ParkingLend lendOffer = ParkingLend.builder()
                .startDate(timeRange.getFromWhen())
                .endDate(timeRange.getUntilWhen())
                .owner(currentUser)
                .parkingSpot(spot)
                .build();
        parkingLendRepository.save(lendOffer);

        return parkingLendMapper.toDTO(lendOffer);
    }

    @Override
    public Page<LendOfferDTO> getLendsForCurrentUser(Pageable pageable) {
        User currentUser = userService.getCurrentUser();
        return parkingLendRepository.findAllByOwner(currentUser, pageable)
                .map(parkingLendMapper::toDTO);
    }

    @Override
    public void deleteLendOfferById(UUID lendOfferId) {
        if (!parkingLendRepository.existsById(lendOfferId)) {
            throw new InvalidLendException("Lend offer with id " + lendOfferId + " not found");
        }
        parkingLendRepository.deleteById(lendOfferId);
    }
}
