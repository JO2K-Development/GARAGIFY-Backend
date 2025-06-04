package com.jo2k.garagify.parking.persistence.model;

import com.jo2k.garagify.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "parking_borrows")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParkingBorrow {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "borrow_time", nullable = false, updatable = false)
    private OffsetDateTime borrowTime;

    @Column(name = "return_time")
    private OffsetDateTime returnTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_spot_id", nullable = false)
    private ParkingSpot parkingSpot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_lend_offer_id")
    private ParkingLend parkingLendOffer;

    @Column(name = "created_at", updatable = false, insertable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = OffsetDateTime.now();
    }
}

