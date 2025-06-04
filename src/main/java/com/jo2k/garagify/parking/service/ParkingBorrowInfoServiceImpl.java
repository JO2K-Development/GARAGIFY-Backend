package com.jo2k.garagify.parking.service;

import com.jo2k.dto.BorrowInfoDTO;
import com.jo2k.garagify.parking.api.ParkingInfoService;
import com.jo2k.garagify.parking.mapper.ParkingBorrowInfoMapper;
import com.jo2k.garagify.parking.persistence.model.ParkingBorrow;
import com.jo2k.garagify.parking.persistence.repository.ParkingBorrowRepository;
import com.jo2k.garagify.user.model.User;
import com.jo2k.garagify.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service("parkingBorrowInfoService")
@RequiredArgsConstructor
public class ParkingBorrowInfoServiceImpl implements ParkingInfoService<BorrowInfoDTO> {
    private final ParkingBorrowRepository parkingBorrowRepository;
    private final UserService userService;
    private final ParkingBorrowInfoMapper parkingBorrowInfoMapper;


    @Override
    public Page<BorrowInfoDTO> getForCurrentUser(Pageable pageable) {
        User currentUser = userService.getCurrentUser();

        Page<ParkingBorrow> result = parkingBorrowRepository.findAllByUser(currentUser, pageable);


        return parkingBorrowRepository.findAllByUser(currentUser, pageable)
                .map(parkingBorrowInfoMapper::toDTO);
    }

}
