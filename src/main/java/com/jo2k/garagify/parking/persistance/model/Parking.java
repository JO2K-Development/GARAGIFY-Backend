package com.jo2k.garagify.parking.persistance.model;

import com.jo2k.garagify.organisation.persistance.model.Organisation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "parking")
public class Parking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation_id", nullable = false)
    private Organisation organisation;

    @Column(nullable = false)
    private String name;

    @Column(name = "ui_object", columnDefinition = "jsonb", nullable = false)
    private String uiObject; // you can use String or a mapped object with a converter

    // getters/setters/constructors
}

