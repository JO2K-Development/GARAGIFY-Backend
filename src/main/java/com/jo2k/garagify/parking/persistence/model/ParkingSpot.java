package com.jo2k.garagify.parking.persistence.model;

import com.jo2k.garagify.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "parking_spot", uniqueConstraints = @UniqueConstraint(columnNames = {"parking_id", "spot_uuid"}))
public class ParkingSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_id", nullable = false)
    private Parking parking;

    @Column(name = "spot_uuid", nullable = false, columnDefinition = "uuid")
    private UUID spotUuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(length = 50)
    private String status;
}
