package com.jo2k.garagify.parking.persistance.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
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

    @Column(name = "owner_id")
    private UUID ownerId; // Make sure you have the users table mapped if you need relations

    @Column(length = 50)
    private String status;

    // getters/setters/constructors
}
