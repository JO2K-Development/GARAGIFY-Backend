package com.jo2k.garagify.parking.persistence.model;

import com.jo2k.garagify.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "parking_lends")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ParkingLend {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "start_date", nullable = false)
    private OffsetDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private OffsetDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_spot_id", nullable = false)
    private ParkingSpot parkingSpot;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "created_at", updatable = false, insertable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = OffsetDateTime.now();
    }

    @OneToMany(mappedBy = "parkingLendOffer")
    private List<ParkingBorrow> parkingBorrows;

}