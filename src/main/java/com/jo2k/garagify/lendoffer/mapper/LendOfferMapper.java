package com.jo2k.garagify.lendoffer.mapper;

import com.jo2k.dto.LendOfferDTO;
import com.jo2k.garagify.lendoffer.persistence.model.LendOffer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LendOfferMapper {

    @Mapping(target = "id", expression = "java(String.valueOf(lendOffer.getId()))")
    @Mapping(target = "spotId", expression = "java(String.valueOf(lendOffer.getParkingSpotId()))")
    @Mapping(target = "ownerId", expression = "java(String.valueOf(lendOffer.getOwnerId()))")
    LendOfferDTO toDto(LendOffer lendOffer);
}