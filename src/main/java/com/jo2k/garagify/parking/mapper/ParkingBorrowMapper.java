package com.jo2k.garagify.parking.mapper;

import com.jo2k.dto.BorrowDTO;
import com.jo2k.dto.BorrowForm;
import com.jo2k.garagify.parking.persistence.model.ParkingBorrow;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ParkingBorrowMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "parkingSpot.parking.id", target = "parkingId")
    @Mapping(source = "parkingSpot.spotUuid", target = "spotId")
    @Mapping(source = "userId", target = "borrowerId")
    @Mapping(source = "borrowTime", target = "startDate")
    @Mapping(source = "returnTime", target = "endDate")
    BorrowDTO toDTO(ParkingBorrow borrow);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", expression = "java(userId)")
    @Mapping(target = "borrowTime", source = "startDate")
    @Mapping(target = "returnTime", source = "endDate")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "parkingSpot", ignore = true)
    ParkingBorrow toEntity(BorrowForm dto, @Context UUID userId);

}
