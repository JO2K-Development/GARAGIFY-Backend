package com.jo2k.garagify.borrow.mapper;

import com.jo2k.dto.BorrowDTO;
import com.jo2k.dto.BorrowForm;
import com.jo2k.garagify.borrow.persistence.model.Borrow;
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
    BorrowDTO toDTO(Borrow borrow);


    @Mapping(target = "id", ignore = true) // or just don't mention it
    @Mapping(target = "parkingSpotId", source = "spotId")
    @Mapping(target = "userId", expression = "java(userId)")
    @Mapping(target = "borrowTime", source = "startDate")
    @Mapping(target = "returnTime", source = "endDate")
    @Mapping(target = "createdAt", ignore = true)
    Borrow toEntity(BorrowForm dto, @Context UUID userId);
}
