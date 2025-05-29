package com.jo2k.garagify.parking.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jo2k.dto.ParkingDTO;
import com.jo2k.garagify.parking.persistence.model.Parking;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ParkingMapper {

    @Autowired
    protected ObjectMapper objectMapper;

    @Mapping(source = "organisation.id", target = "organisationId")
    @Mapping(target = "uiObject", expression = "java(toUiObject(parking.getUiObject()))")
    public abstract ParkingDTO toDto(Parking parking);

    public abstract List<ParkingDTO> toList(List<Parking> parkings);

    public Object toUiObject(String uiObjectJson) {
        try {
            return objectMapper.readValue(uiObjectJson, Object.class);
        } catch (Exception e) {
            return null;
        }
    }
}
