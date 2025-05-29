package com.jo2k.garagify.borrow.service;

import com.jo2k.dto.BorrowGET;
import com.jo2k.dto.BorrowPOST;
import com.jo2k.garagify.borrow.mapper.BorrowMapper;
import com.jo2k.garagify.borrow.persistance.model.Borrow;
import com.jo2k.garagify.borrow.persistance.repository.BorrowRepository;
import com.jo2k.garagify.common.exception.InvalidBorrow;
import com.jo2k.garagify.lendoffer.repository.LendOfferRepository;
import com.jo2k.garagify.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Profile("local")
@Primary
public class BorrowServiceImpl implements com.jo2k.garagify.borrow.api.BorrowService {
    private final BorrowRepository borrowRepository;
    private final UserService userService;
    private final BorrowMapper borrowMapper;

    @Override
    public List<BorrowGET> createBorrow(List<BorrowPOST> borrowPOSTs) {
        UUID currentUserId = userService.getCurrentUser().getId();

        borrowPOSTs.stream()
                .filter(post -> borrowRepository.existsOverlap(post.getSpotId(), post.getStartDate(), post.getEndDate()))
                .findFirst()
                .ifPresent(post -> {
                    throw new InvalidBorrow("Borrow already exists for this parking spot in the specified time range. For borrow: \n{" + post + "}");
                });


        return borrowPOSTs.stream().map(post -> {
            Borrow borrow = borrowMapper.toEntity(post, currentUserId);
            borrowRepository.save(borrow);
            return borrowMapper.toBorrowGET(borrow);
        }).toList();
    }
}