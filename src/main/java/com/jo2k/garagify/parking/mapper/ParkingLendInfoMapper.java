package com.jo2k.garagify.parking.mapper;

import com.jo2k.dto.LendOfferInfoDTO;
import com.jo2k.garagify.parking.persistence.model.ParkingBorrow;
import com.jo2k.garagify.parking.persistence.model.ParkingLend;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public abstract class ParkingLendInfoMapper {
    @Autowired
    protected UserMapper userMapper;

    @Mapping(source = "parkingSpot.parking.id", target = "parkingId")
    @Mapping(source = "parkingSpot.spotUuid", target = "spotId")
    @Mapping(target = "borrowers", ignore = true)
    public abstract LendOfferInfoDTO toDTO(ParkingLend lendOffer);

    public LendOfferInfoDTO toDTOWithBorrowers(ParkingLend lendOffer) {
        LendOfferInfoDTO dto = toDTO(lendOffer);
        dto.setBorrowers(
                userMapper.toUserListDTO(
                        lendOffer.getParkingBorrows().stream().map(ParkingBorrow::getUser).toList()
                )
        );
        return dto;
    }
}