package com.jo2k.garagify.parking.service;

import com.jo2k.dto.LendOfferDTO;
import com.jo2k.dto.LendOfferInfoDTO;
import com.jo2k.dto.TimeRangeRequest;
import com.jo2k.garagify.common.exception.InvalidLendException;
import com.jo2k.garagify.parking.api.ParkingActionService;
import com.jo2k.garagify.parking.api.ParkingInfoService;
import com.jo2k.garagify.parking.mapper.ParkingLendInfoMapper;
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

@Service("parkingLendInfoService")
@RequiredArgsConstructor
public class ParkingLendInfoServiceImpl implements ParkingInfoService<LendOfferInfoDTO> {

    private final ParkingLendRepository parkingLendRepository;
    private final UserService userService;
    private final ParkingLendInfoMapper parkingLendInfoMapper;


    @Override
    public Page<LendOfferInfoDTO> getForCurrentUser(Pageable pageable) {
        User currentUser = userService.getCurrentUser();
        return parkingLendRepository.findAllByOwner(currentUser, pageable)
                .map(parkingLendInfoMapper::toDTO);
    }

}
