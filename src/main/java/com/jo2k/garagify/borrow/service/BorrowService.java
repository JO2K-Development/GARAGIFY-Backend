package com.jo2k.garagify.borrow.service;

import com.jo2k.dto.BorrowGET;
import com.jo2k.garagify.borrow.model.Borrow;
import com.jo2k.garagify.borrow.repository.BorrowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Profile("prod")
public class BorrowService implements IBorrowService {
    private final BorrowRepository borrowRepository;

    @Override
    public Page<BorrowGET> getAllBorrows(Pageable pageable, OffsetDateTime start, OffsetDateTime end, UUID borrowerId) {
        Specification<Borrow> spec = Specification.where(null);

        if (start != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("borrowTime"), start.toLocalDateTime()));
        }

        if (end != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("returnTime"), end.toLocalDateTime()));
        }

        if (borrowerId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("userId"), borrowerId));
        }

        return borrowRepository.findAll(spec, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public Borrow createBorrow(Borrow borrow) {
        // You can add validation/business logic here before saving
        return borrowRepository.save(borrow);
    }

    private BorrowGET mapToDTO(Borrow b) {
        return new BorrowGET()
                .id(String.valueOf(b.getId()))
                .startDate(b.getBorrowTime().atOffset(OffsetDateTime.now().getOffset()))
                .endDate(b.getReturnTime() != null ? b.getReturnTime().atOffset(OffsetDateTime.now().getOffset()) : null)
                .spotId(String.valueOf(b.getParkingSpotId()))
                .borrowerId(String.valueOf(b.getUserId()))
                .ownerId(null);  // fill if available
    }
}
