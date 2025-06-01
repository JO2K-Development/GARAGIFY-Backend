package com.jo2k.garagify.parking.mapper;

import com.jo2k.dto.LendOfferDTO;
import com.jo2k.garagify.parking.persistence.model.ParkingLend;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParkingLendMapper {
    @Mapping(source = "parkingSpot.parking.id", target = "parkingId")
    @Mapping(source = "parkingSpot.spotUuid", target = "spotId")
    LendOfferDTO toDTO(ParkingLend lendOffer);

}