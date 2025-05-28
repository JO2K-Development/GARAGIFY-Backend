package com.jo2k.garagify.borrow.service;

import com.jo2k.dto.BorrowGET;
import com.jo2k.dto.BorrowPOST;
import com.jo2k.garagify.borrow.model.Borrow;
import com.jo2k.garagify.borrow.repository.BorrowRepository;
import com.jo2k.garagify.lendoffer.repository.LendOfferRepository;
import com.jo2k.garagify.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
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
    private final LendOfferRepository lendOfferRepository;
    private final UserService userService;
    @Override
    public List<BorrowGET> createBorrowsIfNotExistsAndAvailable(List<BorrowPOST> borrowPOSTs) {
        List<BorrowGET> createdBorrows = new ArrayList<>();
        System.out.println("JA PIERDOLE");
        UUID currentUserId = userService.getCurrentUser().getId();
        System.out.println(currentUserId + "idk usera");

        if(borrowPOSTs.getFirst().getEndDate() == null) {
            System.out.println("WHAT THE FUC");
        }

        for (BorrowPOST post : borrowPOSTs) {
            // Check if the borrow time overlaps with existing borrows for the same parking spot
            boolean existsBorrow = borrowRepository.existsOverlap(
                    UUID.fromString(post.getSpotId()),
                    LocalDateTime.from(post.getStartDate()),
                    LocalDateTime.from(post.getEndDate())
            );
            // Check if there is a LendOffer for the parking spots within the specified time range
            boolean existsLendOffer = !lendOfferRepository.existsByParkingSpotIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                    UUID.fromString(post.getSpotId()),
                    LocalDateTime.from(post.getStartDate()),
                    LocalDateTime.from(post.getEndDate())
            );

            if (existsBorrow){
                throw new IllegalArgumentException("Borrow already exists for this parking spot in the specified time range. For borrow: \n{" + post + "}");
            }
            if (existsLendOffer) {
//                throw new IllegalArgumentException("No LendOffer available for this parking spot in the specified time range. For borrow: \n{" + post + "}");
            }
        }


        for (BorrowPOST post : borrowPOSTs) {
            Borrow borrow = new Borrow();
            borrow.setId(UUID.randomUUID());
            borrow.setParkingSpotId(UUID.fromString(post.getSpotId()));
            borrow.setUserId(currentUserId);
            borrow.setBorrowTime(LocalDateTime.from(post.getStartDate()));
            borrow.setReturnTime(LocalDateTime.from(post.getEndDate()));

            // Save the borrow to the repository
            borrowRepository.save(borrow);

            // Map to DTO and add to the result list
            createdBorrows.add(mapToDTO(borrow));
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
