package com.jo2k.garagify.borrow.service;

import com.jo2k.dto.BorrowGET;
import com.jo2k.dto.BorrowPOST;
import com.jo2k.garagify.auth.service.CurrentUserProvider;
import com.jo2k.garagify.borrow.model.Borrow;
import com.jo2k.garagify.borrow.repository.BorrowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.jo2k.garagify.user.service.UserService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Profile("local")
@Primary
public class BorrowService implements IBorrowService {
    private final BorrowRepository borrowRepository;
//    private final LendOfferRepository lendOfferRepository;
    private final CurrentUserProvider currentUserProvider;
    @PostConstruct
    public void init() {
        System.out.println("BorrowServiceDev loaded!");
    }
    @Override
    public List<BorrowGET> createBorrowsIfNotExistsAndAvailable(List<BorrowPOST> borrowPOSTs) {
        List<BorrowGET> createdBorrows = new ArrayList<>();
        UUID currentUserId = currentUserProvider.getCurrentUserId();
        boolean conditionsCorrect = true;
        for (BorrowPOST post : borrowPOSTs) {
            // Check if the borrow time overlaps with existing borrows for the same parking spot
            boolean existsBorrow = borrowRepository.existsOverlap(
                    UUID.fromString(post.getSpotId()),
                    LocalDateTime.from(post.getStartDate()),
                    LocalDateTime.from(post.getEndDate())
            );
            // Check if there is a LendOffer for the parking spots within the specified time range
//            boolean existsLendOffer = borrowRepository.existsLendOffer(
//                    UUID.fromString(post.getSpotId()),
//                    LocalDateTime.from(post.getStartDate()),
//                    LocalDateTime.from(post.getEndDate())
//            );

//            if (existsBorrow || !existsLendOffer) {
//                conditionsCorrect = false;
//                break; // If any condition is not met, we stop processing further
//            }
        }

        if(conditionsCorrect)
            for (BorrowPOST post : borrowPOSTs) {

                    Borrow borrow = Borrow.builder()
                            .userId(currentUserId)
                            .parkingSpotId(UUID.fromString(post.getSpotId()))
                            .borrowTime(LocalDateTime.from(post.getStartDate()))
                            .returnTime(LocalDateTime.from(post.getEndDate()))
                            .build();

                    Borrow saved = borrowRepository.save(borrow);
                    createdBorrows.add(mapToDTO(saved));

            }

        return createdBorrows;
    }

    public BorrowGET mapToDTO(Borrow borrow) {
        return new BorrowGET()
                .id(borrow.getId().toString())
                .spotId(borrow.getParkingSpotId().toString())
                .borrowerId(borrow.getUserId().toString())
                .startDate(borrow.getBorrowTime().atOffset(ZoneOffset.UTC))
                .endDate(borrow.getReturnTime().atOffset(ZoneOffset.UTC));
    }
}
