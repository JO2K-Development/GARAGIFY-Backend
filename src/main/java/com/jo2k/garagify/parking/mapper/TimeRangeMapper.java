package com.jo2k.garagify.parking.mapper;

import com.jo2k.dto.TimeRangeDto;
import org.mapstruct.Mapper;
import java.time.OffsetDateTime;

@Mapper(componentModel = "spring")
public interface TimeRangeMapper {
    default TimeRangeDto toDto(OffsetDateTime start, OffsetDateTime end) {
        TimeRangeDto dto = new TimeRangeDto();
        dto.setStart(start);
        dto.setEnd(end);
        return dto;
    }
}