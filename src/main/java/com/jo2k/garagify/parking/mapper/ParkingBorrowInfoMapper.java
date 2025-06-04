package com.jo2k.garagify.parking.mapper;

import com.jo2k.dto.BorrowInfoDTO;
import com.jo2k.garagify.parking.persistence.model.ParkingBorrow;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ParkingBorrowInfoMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "parkingSpot.parking.id", target = "parkingId")
    @Mapping(source = "parkingSpot.spotUuid", target = "spotId")
    @Mapping(source = "borrowTime", target = "startDate")
    @Mapping(source = "returnTime", target = "endDate")
    @Mapping(source = "user", target = "borrower")
    BorrowInfoDTO toDTO(ParkingBorrow borrow);
}
