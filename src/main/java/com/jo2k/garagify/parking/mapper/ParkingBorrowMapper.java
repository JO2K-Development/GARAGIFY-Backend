package com.jo2k.garagify.parking.mapper;

import com.jo2k.dto.BorrowDTO;
import com.jo2k.garagify.parking.persistence.model.ParkingBorrow;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ParkingBorrowMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "parkingSpot.parking.id", target = "parkingId")
    @Mapping(source = "parkingSpot.spotUuid", target = "spotId")
    @Mapping(source = "borrowTime", target = "startDate")
    @Mapping(source = "returnTime", target = "endDate")
    BorrowDTO toDTO(ParkingBorrow borrow);


}
