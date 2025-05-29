package com.jo2k.garagify.borrow.persistance.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "borrows")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Borrow {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "borrow_time", nullable = false, updatable = false)
    private OffsetDateTime borrowTime;

    @Column(name = "return_time")
    private OffsetDateTime returnTime;

    @Column(name = "parking_spot_id", nullable = false)
    private UUID parkingSpotId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = OffsetDateTime.now();
    }
}
