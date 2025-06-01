package com.jo2k.garagify.parking.persistence.model;

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

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_spot_id", nullable = false)
    private ParkingSpot parkingSpot;

    @Column(name = "created_at", updatable = false, insertable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = OffsetDateTime.now();
    }
}

