package com.jo2k.garagify.parking.mapper;

import com.jo2k.dto.ParkingSpotDTO;
import com.jo2k.garagify.parking.persistence.model.ParkingSpot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParkingSpotMapper {
    @Mapping(source = "parking.id", target = "parkingId")
    @Mapping(source = "spotUuid", target = "spotUuid")
    ParkingSpotDTO toDto(ParkingSpot parkingSpot);

    List<ParkingSpotDTO> toList(List<ParkingSpot> spots);

}
