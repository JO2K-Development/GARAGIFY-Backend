package com.jo2k.garagify.borrow.mapper;

import com.jo2k.dto.BorrowGET;
import com.jo2k.dto.BorrowPOST;
import com.jo2k.garagify.borrow.persistance.model.Borrow;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface BorrowMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "parkingSpotId", target = "spotId")
    @Mapping(source = "userId", target = "borrowerId")
    @Mapping(source = "borrowTime", target = "startDate")
    @Mapping(source = "returnTime", target = "endDate")
    BorrowGET toBorrowGET(Borrow borrow);


    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "parkingSpotId", source = "spotId")
    @Mapping(target = "userId", expression = "java(userId)")
    @Mapping(target = "borrowTime", source = "startDate")
    @Mapping(target = "returnTime", source = "endDate")
    @Mapping(target = "createdAt", ignore = true)
    Borrow toEntity(BorrowPOST dto, @Context UUID userId);
}
