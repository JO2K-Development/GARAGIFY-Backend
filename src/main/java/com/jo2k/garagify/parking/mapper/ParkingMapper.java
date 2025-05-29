package com.jo2k.garagify.parking.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jo2k.dto.ParkingGET;
import com.jo2k.dto.ParkingSpotGET;
import com.jo2k.garagify.parking.persistance.model.Parking;
import com.jo2k.garagify.parking.persistance.model.ParkingSpot;
import org.mapstruct.*;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class ParkingMapper {

    @Autowired
    protected ObjectMapper objectMapper;

    // --- PARKING ---
    @Mapping(source = "organisation.id", target = "organisationId")
    @Mapping(target = "uiObject", expression = "java(toUiObject(parking.getUiObject()))")
    public abstract ParkingGET toParkingGET(Parking parking);

    // Mapstruct doesn't handle List mapping automatically for abstract mappers, so you must declare this:
    public abstract List<ParkingGET> toParkingGETList(List<Parking> parkings);

    // Convert JSON string to Object for UI Object field
    public Object toUiObject(String uiObjectJson) {
        try {
            return objectMapper.readValue(uiObjectJson, Object.class);
        } catch (Exception e) {
            return null;
        }
    }

    // --- PARKING SPOT ---
    @Mapping(source = "parking.id", target = "parkingId")
    @Mapping(source = "spotUuid", target = "spotUuid")
    @Mapping(target = "ownerId", expression = "java(toNullableOwnerId(parkingSpot.getOwnerId()))")
    public abstract ParkingSpotGET toParkingSpotGET(ParkingSpot parkingSpot);

    public abstract List<ParkingSpotGET> toParkingSpotGETList(List<ParkingSpot> spots);

    // Handle null ownerId
    public JsonNullable<UUID> toNullableOwnerId(UUID ownerId) {
        return ownerId == null ? JsonNullable.undefined() : JsonNullable.of(ownerId);
    }
}
