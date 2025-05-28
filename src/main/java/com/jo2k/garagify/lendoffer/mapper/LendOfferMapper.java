package com.jo2k.garagify.lendoffer.mapper;

import com.jo2k.garagify.lendoffer.model.LendOffer;
import com.jo2k.dto.LendOfferGET;
import org.springframework.stereotype.Component;

@Component
public class LendOfferMapper {
    public LendOfferGET toDto(LendOffer lendOffer) {
        System.out.println("Mapping LendOffer to LendOfferGET: " + lendOffer);
        LendOfferGET dto = new LendOfferGET();
        dto.setId(String.valueOf(lendOffer.getId()));
        dto.setSpotId(String.valueOf(lendOffer.getParkingSpotId()));
        dto.setOwnerId(String.valueOf(lendOffer.getOwnerId()));
        dto.setStartDate(lendOffer.getStartDate().atOffset(java.time.ZoneOffset.UTC));
        dto.setEndDate(lendOffer.getEndDate().atOffset(java.time.ZoneOffset.UTC));
        return dto;
    }
}