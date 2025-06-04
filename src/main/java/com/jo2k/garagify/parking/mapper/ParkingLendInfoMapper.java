package com.jo2k.garagify.parking.mapper;

import com.jo2k.dto.LendOfferInfoDTO;
import com.jo2k.garagify.parking.persistence.model.ParkingLend;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ParkingLendInfoMapper {
    @Mapping(source = "parkingSpot.parking.id", target = "parkingId")
    @Mapping(source = "parkingSpot.spotUuid", target = "spotId")
    @Mapping(source = "owner", target = "owner")
    LendOfferInfoDTO toDTO(ParkingLend lendOffer);
}