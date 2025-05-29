package com.jo2k.garagify.borrow.service;

import com.jo2k.dto.BorrowGET;
import com.jo2k.dto.BorrowPOST;
import com.jo2k.garagify.borrow.mapper.BorrowMapper;
import com.jo2k.garagify.borrow.persistance.model.Borrow;
import com.jo2k.garagify.borrow.persistance.repository.BorrowRepository;
import com.jo2k.garagify.common.exception.InvalidBorrow;
import com.jo2k.garagify.lendoffer.repository.LendOfferRepository;
import com.jo2k.garagify.parking.persistance.repository.ParkingSpotRepository;
import com.jo2k.garagify.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BorrowServiceImpl implements com.jo2k.garagify.borrow.api.BorrowService {
    private final BorrowRepository borrowRepository;
    private final UserService userService;
    private final BorrowMapper borrowMapper;
    private final ParkingSpotRepository parkingSpotRepository;

    @Override
    public List<BorrowGET> createBorrow(List<BorrowPOST> borrowPOSTs) {
        UUID currentUserId = userService.getCurrentUser().getId();

        borrowPOSTs.forEach(post -> {
            if (!parkingSpotRepository.existsBySpotUuid(post.getSpotId())) {
                throw new InvalidBorrow("Parking spot does not exist: " + post.getSpotId());
            }
            if (borrowRepository.existsOverlap(post.getSpotId(), post.getStartDate(), post.getEndDate())) {
                throw new InvalidBorrow("Borrow already exists for this parking spot in the specified time range. For borrow: \n{" + post + "}");
            }
        });

        return borrowPOSTs.stream()
                .map(post -> {
                    Borrow borrow = borrowMapper.toEntity(post, currentUserId);
                    borrowRepository.save(borrow);
                    return borrowMapper.toBorrowGET(borrow);
                })
                .toList();
    }

    @Override
    public Page<BorrowGET> getBorrowsForCurrentUser(Pageable pageable) {
        UUID userId = userService.getCurrentUser().getId();
        return borrowRepository.findAllByUserId(userId, pageable)
                .map(borrowMapper::toBorrowGET);
    }


}