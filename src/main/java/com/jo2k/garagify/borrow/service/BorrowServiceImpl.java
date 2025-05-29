package com.jo2k.garagify.borrow.service;

import com.jo2k.dto.BorrowDTO;
import com.jo2k.dto.BorrowForm;
import com.jo2k.garagify.borrow.api.BorrowService;
import com.jo2k.garagify.borrow.mapper.BorrowMapper;
import com.jo2k.garagify.borrow.persistence.model.Borrow;
import com.jo2k.garagify.borrow.persistence.repository.BorrowRepository;
import com.jo2k.garagify.common.exception.InvalidBorrowException;
import com.jo2k.garagify.common.exception.ObjectNotFoundException;
import com.jo2k.garagify.parking.persistence.repository.ParkingSpotRepository;
import com.jo2k.garagify.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BorrowServiceImpl implements BorrowService {
    private final BorrowRepository borrowRepository;
    private final UserService userService;
    private final BorrowMapper borrowMapper;
    private final ParkingSpotRepository parkingSpotRepository;

    @Override
    public List<BorrowDTO> createBorrow(List<BorrowForm> borrowPOSTs) {
        UUID currentUserId = userService.getCurrentUser().getId();

        borrowPOSTs.forEach(post -> {
            if (!parkingSpotRepository.existsBySpotUuid(post.getSpotId())) {
                throw new InvalidBorrowException("Parking spot does not exist: " + post.getSpotId());
            }
            if (borrowRepository.existsOverlap(post.getSpotId(), post.getStartDate(), post.getEndDate())) {
                throw new InvalidBorrowException("Borrow already exists for this parking spot in the specified time range. For borrow: \n{" + post + "}");
            }
        });
        return borrowPOSTs.stream()
                .map(post -> {
                    Borrow borrow = borrowMapper.toEntity(post, currentUserId);
                    borrowRepository.save(borrow);
                    return borrowMapper.toDTO(borrow);
                })
                .toList();
    }
    @Override
    public Page<BorrowDTO> getBorrowsForCurrentUser(Pageable pageable) {
        UUID userId = userService.getCurrentUser().getId();
        return borrowRepository.findAllByUserId(userId, pageable)
                .map(borrowMapper::toDTO);
    }
    @Transactional
    public void deleteBorrowById(UUID id) {
        if (!borrowRepository.existsById(id)) {
            throw new ObjectNotFoundException("Borrow with id " + id + " not found");
        }
        borrowRepository.deleteById(id);
    }
}