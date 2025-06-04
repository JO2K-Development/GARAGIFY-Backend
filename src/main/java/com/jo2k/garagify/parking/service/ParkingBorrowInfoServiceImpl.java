package com.jo2k.garagify.parking.service;

import com.jo2k.dto.BorrowDTO;
import com.jo2k.dto.BorrowInfoDTO;
import com.jo2k.dto.TimeRangeRequest;
import com.jo2k.garagify.common.exception.InvalidBorrowException;
import com.jo2k.garagify.common.exception.ObjectNotFoundException;
import com.jo2k.garagify.parking.api.ParkingActionService;
import com.jo2k.garagify.parking.api.ParkingInfoService;
import com.jo2k.garagify.parking.mapper.ParkingBorrowInfoMapper;
import com.jo2k.garagify.parking.mapper.ParkingBorrowMapper;
import com.jo2k.garagify.parking.persistence.model.ParkingBorrow;
import com.jo2k.garagify.parking.persistence.model.ParkingSpot;
import com.jo2k.garagify.parking.persistence.repository.ParkingBorrowRepository;
import com.jo2k.garagify.parking.persistence.repository.ParkingSpotRepository;
import com.jo2k.garagify.user.model.User;
import com.jo2k.garagify.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("parkingBorrowInfoService")
@RequiredArgsConstructor
public class ParkingBorrowInfoServiceImpl implements ParkingInfoService<BorrowInfoDTO> {
    private final ParkingBorrowRepository parkingBorrowRepository;
    private final UserService userService;
    private final ParkingBorrowInfoMapper parkingBorrowInfoMapper;


    @Override
    public Page<BorrowInfoDTO> getForCurrentUser(Pageable pageable) {
        User currentUser = userService.getCurrentUser();
        return parkingBorrowRepository.findAllByUser(currentUser, pageable)
                .map(parkingBorrowInfoMapper::toDTO);
    }

}
